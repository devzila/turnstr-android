package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Service_Handler.Constants;
import Service_Handler.ServiceHandler;
import circular_imagview.ProgressHUD;

public class Login_Screen extends Activity {
    RelativeLayout signup;
    Button Login;
    EditText edtx_username, Password;
    LinearLayout Lnear_Usr, lnear_passowrd;
    private ProgressDialog pDialog;
    ProgressHUD mProgressHUD;
    String Str_Email,Str_Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login__screen);
        signup = (RelativeLayout) findViewById(R.id.relativeLayout);
        Login = (Button) findViewById(R.id.button_Login);
        edtx_username = (EditText) findViewById(R.id.etpassword);
        Password = (EditText) findViewById(R.id.editText);
        Lnear_Usr = (LinearLayout) findViewById(R.id.linearLayout);
        lnear_passowrd = (LinearLayout) findViewById(R.id.linearLayout2);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Login_Screen.this,
                        Signup_screen.class);

                startActivity(i1);

                finish();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtx_username.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Lnear_Usr.startAnimation(anm);
                } else if (Password.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    lnear_passowrd.startAnimation(anm);
                } else {
//                    Intent i1 = new Intent(Login_Screen.this,
//                            MainActivity.class);
//
//                    startActivity(i1);
//
//                    finish();
                    Str_Email= edtx_username.getText().toString();
                    Str_Password= Password.getText().toString();
                    new Login().execute();
                }
//                Intent i1 = new Intent(Login_Screen.this,
//                        MainActivity.class);
//
//                startActivity(i1);
//
//                finish();
            }
        });
    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }
    private class Login extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion,Hardware;
        JSONObject jsonnode,json_User;
        String str;
        String Name,access_tocken,Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressHUD = ProgressHUD.show(Login_Screen.this,"Connecting", true,true,this);
            // Showing progress dialog
//            pDialog = new ProgressDialog(Login_Screen.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            android_id = Settings.Secure.getString(Login_Screen.this.getContentResolver(),
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
            nameValuePairs.add(new BasicNameValuePair("email", Str_Email));
            nameValuePairs.add(new BasicNameValuePair("password", Str_Password));
            nameValuePairs.add(new BasicNameValuePair("device_id", android_id));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", myVersion));
            nameValuePairs.add(new BasicNameValuePair("hardware", Hardware));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1"));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constants.Login,
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
                        json_User=jsonnode.getJSONObject("user");
                        String Deviceid = jsonnode.getString("device_id");
                        String userid = jsonnode.getString("user_id");
                        Name = json_User.getString("name");
                        access_tocken= jsonnode.getString("access_token");
                        Ostype= jsonnode.getString("device_id");

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

                // looping through All Contacts
                Intent i1 = new Intent(Login_Screen.this,
                        MainActivity.class);
                i1.putExtra("Name",Name);
                i1.putExtra("access_tocken",access_tocken);
                i1.putExtra("Acess_Token",access_tocken);
                i1.putExtra("Ostype",Ostype);
                i1.putExtra("device_id",Ostype);
                startActivity(i1);

                finish();
            }else{
                Toast.makeText(Login_Screen.this,"invalid username password",Toast.LENGTH_LONG).show();
            }



        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }


}
