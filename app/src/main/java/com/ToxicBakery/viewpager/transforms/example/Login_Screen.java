package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;


import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import circular_imagview.ProgressHUD;

public class Login_Screen extends Activity {
    RelativeLayout signup;
    Button Login;
    EditText edtx_username, Password;
    LinearLayout Lnear_Usr, lnear_passowrd;
    private ProgressDialog pDialog;
    ProgressHUD mProgressHUD;
    String Str_Email, Str_Password;
    TextView Forgot_login;
    Session_manager session;
    RelativeLayout Rlv_Signup_withFb;
    SocialAuthAdapter Sacial_auth_adapter;
    String userid, Email, username, First_name, bio, phone, gender, website, profile_image;
    private CallbackManager callbackManager;
    private TextView textView;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    LoginButton loginButton;
    String email_fb, Name_fb, fbid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login__screen);
        signup = (RelativeLayout) findViewById(R.id.relativeLayout);
        Login = (Button) findViewById(R.id.button_Login);
        edtx_username = (EditText) findViewById(R.id.etpassword);
        Password = (EditText) findViewById(R.id.editText);
        Lnear_Usr = (LinearLayout) findViewById(R.id.linearLayout);
        Forgot_login = (TextView) findViewById(R.id.textView);
        lnear_passowrd = (LinearLayout) findViewById(R.id.linearLayout2);
        Rlv_Signup_withFb = (RelativeLayout) findViewById(R.id.linearLayout3);

        session = new Session_manager(getApplicationContext());
        //  printKeyHash(Login_Screen.this);
        Sacial_auth_adapter = new SocialAuthAdapter(new ResponseListener());
        callbackManager = CallbackManager.Factory.create();
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email");
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Login_Screen.this,
                        Signup_screen.class);

                startActivity(i1);

                finish();
            }
        });
        Forgot_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Login_Screen.this,
                        Forgot_password.class);

                startActivity(i1);

                finish();


                //  Sacial_auth_adapter.authorize(Login_Screen.this, SocialAuthAdapter.Provider.TWITTER);
                //Sacial_auth_adapter.updateStatus();

            }
        });
        Rlv_Signup_withFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                FacebookSdk.sdkInitialize(getApplicationContext());
//
//                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(Login_Screen.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

                //  LoginManager.getInstance().logInWithPublishPermissions(Login_Screen.this, Arrays.asList("publish_actions"));

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                final AccessToken accessToken = loginResult.getAccessToken();

                                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                        email_fb = user.optString("email");
                                        Name_fb = user.optString("name");
                                        fbid = user.optString("id");
                                        try {
                                            URL image_value = new URL("https://graph.facebook.com/"+fbid+"/picture" );
                                            String str_pictureurl = String.valueOf(image_value);
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        new Login_fb().execute();
                                    }
                                }).executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                exception.printStackTrace();
                                // App code
                            }
                        });


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
                    Str_Email = edtx_username.getText().toString();
                    Str_Password = Password.getText().toString();
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

    private void printKeyHash(Login_Screen login_screen) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = login_screen.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = login_screen.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", login_screen.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }


    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private class Login extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str = "nostatus";
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Login_Screen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            android_id = Settings.Secure.getString(Login_Screen.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
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
            String jsonStr = sh.makeServiceCall(Constant.Login,
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
                    if (str.contentEquals("true")) {
                        jsonnode = jsonObj.getJSONObject("data");
                        json_User = jsonnode.getJSONObject("user");
                        String Deviceid = jsonnode.getString("device_id");
                        userid = jsonnode.getString("user_id");
                        Name = json_User.getString("name");
                        access_tocken = jsonnode.getString("access_token");
                        Ostype = jsonnode.getString("device_id");
                        Email = json_User.getString("email");
                        username = json_User.getString("username");
                        First_name = json_User.getString("name");
                        bio = json_User.getString("bio");
                        phone = json_User.getString("phone_number");
                        gender = json_User.getString("gender");
                        website = json_User.getString("website");
                        profile_image = json_User.getString("profile_image");
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
            pDialog.dismiss();
            if (str.contentEquals("nostatus")) {
                Toast.makeText(Login_Screen.this, "oops server error", Toast.LENGTH_LONG).show();
            } else if (str.contentEquals("true")) {

                // looping through All Contacts
                session.createLoginSession(access_tocken, Ostype, userid, Email, username, First_name, bio
                        , phone, gender, website, profile_image);
                Intent i1 = new Intent(Login_Screen.this,
                        MainActivity.class);
                i1.putExtra("Name", Name);
                i1.putExtra("access_tocken", access_tocken);
                i1.putExtra("Acess_Token", access_tocken);
                i1.putExtra("Ostype", Ostype);
                i1.putExtra("device_id", Ostype);
                i1.putExtra("user_id", userid);
                startActivity(i1);


                finish();
            } else {
                Toast.makeText(Login_Screen.this, "invalid username password", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Login_fb extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str = "nostatus";
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // mProgressHUD = ProgressHUD.show(Login_Screen.this, "Connecting", true, true, this);
            // Showing progress dialog
            pDialog = new ProgressDialog(Login_Screen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            android_id = Settings.Secure.getString(Login_Screen.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");


            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            //nameValuePairs.add(new BasicNameValuePair("email", email_fb));
            nameValuePairs.add(new BasicNameValuePair("first_name", Name_fb));
            nameValuePairs.add(new BasicNameValuePair("last_name", ""));
            nameValuePairs.add(new BasicNameValuePair("id", fbid));

            nameValuePairs.add(new BasicNameValuePair("device_id", android_id));
            nameValuePairs.add(new BasicNameValuePair("os_type", "Android"));
            nameValuePairs.add(new BasicNameValuePair("os_version", myVersion));
            nameValuePairs.add(new BasicNameValuePair("hardware", Hardware));
            nameValuePairs.add(new BasicNameValuePair("app_version", "1"));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constant.Fb_login,
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
                    if (str.contentEquals("true")) {
                        jsonnode = jsonObj.getJSONObject("data");
                        json_User = jsonnode.getJSONObject("user");
                        String Deviceid = jsonnode.getString("device_id");
                        userid = jsonnode.getString("user_id");
                        Name = json_User.getString("name");
                        access_tocken = jsonnode.getString("access_token");
                        Ostype = jsonnode.getString("device_id");
                        Email = json_User.getString("email");
                        username = json_User.getString("username");
                        First_name = json_User.getString("name");
                        bio = json_User.getString("bio");
                        phone = json_User.getString("phone_number");
                        gender = json_User.getString("gender");
                        website = json_User.getString("website");
                        profile_image = json_User.getString("profile_image");
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
            pDialog.dismiss();
            if (str.contentEquals("nostatus")) {
                Toast.makeText(Login_Screen.this, "oops server error", Toast.LENGTH_LONG).show();
            } else if (str.contentEquals("true")) {

                // looping through All Contacts
                session.createLoginSession(access_tocken, Ostype, userid, Email, username, First_name, bio
                        , phone, gender, website, profile_image);
                Intent i1 = new Intent(Login_Screen.this,
                        MainActivity.class);
                i1.putExtra("Name", Name);
                i1.putExtra("access_tocken", access_tocken);
                i1.putExtra("Acess_Token", access_tocken);
                i1.putExtra("Ostype", Ostype);
                i1.putExtra("device_id", Ostype);
                i1.putExtra("user_id", userid);
                startActivity(i1);


                finish();
            } else {
                Toast.makeText(Login_Screen.this, "invalid username password", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private final class ResponseListener implements DialogListener {
        public void onComplete(Bundle values) {

            Sacial_auth_adapter.updateStatus("Testing", new MessageListener(), true);

            // Sacial_auth_adapter.getUserProfileAsync(new ProfileDataListener());
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onBack() {

        }
    }

    // To receive the profile response after authentication
    private final class ProfileDataListener implements SocialAuthListener {


        @Override
        public void onExecute(String s, Object o) {

        }

        @Override
        public void onError(SocialAuthError socialAuthError) {

        }

        public String printKeyHash(Activity context) {
            PackageInfo packageInfo;
            String key = null;
            try {
                //getting application package name, as defined in manifest
                String packageName = context.getApplicationContext().getPackageName();

                //Retriving package info
                packageInfo = context.getPackageManager().getPackageInfo(packageName,
                        PackageManager.GET_SIGNATURES);

                Log.e("Package Name=", context.getApplicationContext().getPackageName());

                for (Signature signature : packageInfo.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    key = new String(Base64.encode(md.digest(), 0));

                    // String key = new String(Base64.encodeBytes(md.digest()));
                    Log.e("Key Hash=", key);
                }
            } catch (PackageManager.NameNotFoundException e1) {
                Log.e("Name not found", e1.toString());
            } catch (NoSuchAlgorithmException e) {
                Log.e("No such an algorithm", e.toString());
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

            return key;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private final class MessageListener implements SocialAuthListener {


        @Override
        public void onExecute(String s, Object o) {

        }

        @Override
        public void onError(SocialAuthError socialAuthError) {

        }
    }
}
