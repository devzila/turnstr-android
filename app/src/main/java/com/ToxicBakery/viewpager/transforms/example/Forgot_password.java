package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import circular_imagview.ProgressHUD;

public class Forgot_password extends Activity {
    EditText  Email;
    Button Submit;
    LinearLayout Lv_Email;
    ProgressHUD mProgressHUD;
    String StrEmail;
    TextView Back_to_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);
        Email = (EditText) findViewById(R.id.editText5);
        Submit= (Button) findViewById(R.id.button3);
        Lv_Email = (LinearLayout) findViewById(R.id.linearLayout7);
        Back_to_login =(TextView)findViewById(R.id.textView12);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Lv_Email.startAnimation(anm);
                }else if (emailValidator(Email.getText().toString())==false) {
                    Animation anm = Shake_Animation();
                    Lv_Email.startAnimation(anm);
                    Toast.makeText(Forgot_password.this, "Email address not valid", Toast.LENGTH_LONG).show();
                }else{
                    StrEmail=Email.getText().toString();
                    new sign_up().execute();
                }
            }
        });
        Back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Forgot_password.this,
                        Login_Screen.class);

                startActivity(i1);

                finish();

            }
        });
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }
    private class sign_up extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion,Hardware;
        JSONObject jsonnode;
        String str;
        String Deviceid,Token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressHUD = ProgressHUD.show(Forgot_password.this, "Connecting", true, true, this);
            // Showing progress dialog
//            pDialog = new ProgressDialog(Login_Screen.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            android_id = Settings.Secure.getString(Forgot_password.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware= Build.MANUFACTURER;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("email", StrEmail));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constant.forgotpassword,
                    ServiceHandler.POST, nameValuePairs);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    // JSONArray array1 = null;
                    str = jsonObj.getString("status");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            mProgressHUD.dismiss();




        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
