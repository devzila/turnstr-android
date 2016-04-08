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

import Service_Handler.Constants;
import Service_Handler.ServiceHandler;
import circular_imagview.ProgressHUD;

public class Signup_screen extends Activity {
    EditText Edtxt_fistname, Email, Usrname, Password, Confirmpassword;
    LinearLayout Lnrlyaout_firstname, Lv_Email, LvUsername, Lv_password, LvConfirmpassword;
    Button Sign_up;
    Boolean email_matcher;
    ProgressHUD mProgressHUD;
    String First_name,StrEmail,Str_username,Str_password,Str_Confirmpassword;
    TextView alreadyRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup_screen);
        Edtxt_fistname = (EditText) findViewById(R.id.etpassword);
        Email = (EditText) findViewById(R.id.editText);
        Usrname = (EditText) findViewById(R.id.editText2);
        Password = (EditText) findViewById(R.id.editText3);
        Confirmpassword = (EditText) findViewById(R.id.editText4);
        Lnrlyaout_firstname = (LinearLayout) findViewById(R.id.linearLayout);
        Lv_Email = (LinearLayout) findViewById(R.id.linearLayout2);
        LvUsername = (LinearLayout) findViewById(R.id.linearLayout4);
        Lv_password = (LinearLayout) findViewById(R.id.linearLayout5);
        LvConfirmpassword = (LinearLayout) findViewById(R.id.linearLayout6);
        alreadyRegister = (TextView)findViewById(R.id.textView5);
        Sign_up = (Button) findViewById(R.id.button);

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Edtxt_fistname.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Lnrlyaout_firstname.startAnimation(anm);
                } else if (Email.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Lv_Email.startAnimation(anm);
                }else if (emailValidator(Email.getText().toString())==false) {
                    Animation anm = Shake_Animation();
                    Lv_Email.startAnimation(anm);
                    Toast.makeText(Signup_screen.this, "Email address not valid", Toast.LENGTH_LONG).show();
                }
                else if (Usrname.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    LvUsername.startAnimation(anm);
                } else if (Password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Lv_password.startAnimation(anm);
                } else if (Confirmpassword.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    LvConfirmpassword.startAnimation(anm);
                } else if (!Confirmpassword.getText().toString().contentEquals(Password.getText().toString())) {
                    Animation anm = Shake_Animation();
                    LvConfirmpassword.startAnimation(anm);
                    Toast.makeText(Signup_screen.this, "Password do not match", Toast.LENGTH_LONG).show();
                } else {
//                    Intent i1 = new Intent(Signup_screen.this,
//                            MainActivity.class);
//
//                    startActivity(i1);
//
//                    finish();
                    First_name=Edtxt_fistname.getText().toString();
                    StrEmail=Email.getText().toString();
                    Str_username=Usrname.getText().toString();
                    Str_Confirmpassword=Confirmpassword.getText().toString();

                    new sign_up().execute();

                }
            }
        });

        alreadyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Signup_screen.this,
                        Login_Screen.class);

                startActivity(i1);

                finish();

            }
        });
    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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

            mProgressHUD = ProgressHUD.show(Signup_screen.this, "Connecting", true, true, this);
            // Showing progress dialog
//            pDialog = new ProgressDialog(Login_Screen.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            android_id = Settings.Secure.getString(Signup_screen.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware= Build.MANUFACTURER;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("name", First_name));
            nameValuePairs.add(new BasicNameValuePair("phone_number", "9914564058"));
            nameValuePairs.add(new BasicNameValuePair("email",StrEmail));
            nameValuePairs.add(new BasicNameValuePair("username", Str_username));
            nameValuePairs.add(new BasicNameValuePair("password", Str_Confirmpassword));
            nameValuePairs.add(new BasicNameValuePair("hardware", Hardware));
            nameValuePairs.add(new BasicNameValuePair("device_id", android_id));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", myVersion));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1"));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constants.Register,
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
                    if(str.contentEquals("true")) {
                        jsonnode = jsonObj.getJSONObject("data");
                         Deviceid = jsonnode.getString("device_id");
                        Token = jsonnode.getString("access_token");
                        // looping through All Contacts
                    }

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
            if(str.contentEquals("true")) {
                Intent i1 = new Intent(Signup_screen.this,
                        MainActivity.class);
                i1.putExtra("Name",First_name);
                i1.putExtra("access_tocken",Deviceid);
                i1.putExtra("Ostype",Deviceid);
                startActivity(i1);

                finish();
            }else{
                Toast.makeText(Signup_screen.this,"Not Registered",Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

}
