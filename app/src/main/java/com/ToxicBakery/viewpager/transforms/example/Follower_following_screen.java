package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import adapter.Grid_view_adapter;
import adapter.floower_following_adapter;
import it.sephiroth.android.library.picasso.Picasso;

public class Follower_following_screen extends Activity {
    ListView Lv;
    Button Img_back;
    TextView Txt_tittle;
    Session_manager session;
    ArrayList<HashMap<String, String>> Array_list_Follower_following = new ArrayList<HashMap<String, String>>();
    String accestoken, device_id,Status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_follower_following_screen);
        Lv = (ListView) findViewById(R.id.List_follow_follower);
        Img_back = (Button) findViewById(R.id.button7);
        Txt_tittle = (TextView) findViewById(R.id.textView50);
        session = new Session_manager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // img_profile.setImageResource(R.drawable.profile_placeholder);
        // access_token
        accestoken = user.get(session.Acess_Token);


        device_id = user.get(session.device_id);
        Intent intent = getIntent();
        Status = intent.getStringExtra("Status");
        Txt_tittle.setText(Status);
        new Follower_unfollowing().execute();
        Img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class Follower_unfollowing extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            JSONObject jsonnode, json_User;
            Array_list_Follower_following.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            //nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            if(Status.contentEquals("Follower")) {
                jsonStr = sh.makeServiceCall_withHeader(Constant.Followers_me,
                        ServiceHandler.GET, nameValuePairs, accestoken, device_id);
            }else{
                jsonStr = sh.makeServiceCall_withHeader(Constant.Followings_me,
                        ServiceHandler.GET, nameValuePairs, accestoken, device_id);
            }
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray array1 = null;

                    array1 = jsonObj.getJSONArray("data");

                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject c = array1.getJSONObject(i);
                        String name = c.getString("name");
                        String profile_pic = c.getString("profile_image");


                        // tmp hashmap for single contact
                        HashMap<String, String> Profile_images = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        Profile_images.put("name", name);
                        Profile_images.put("profile_pic", profile_pic);


                        // adding PostUrl to Array list
                        Array_list_Follower_following.add(Profile_images);

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
            //pDialog.dismiss();
            floower_following_adapter adapter = new floower_following_adapter(Follower_following_screen.this,
                    Array_list_Follower_following
            );
            Lv.setAdapter(adapter);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
