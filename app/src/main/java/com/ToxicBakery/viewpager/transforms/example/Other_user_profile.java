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
import android.widget.GridView;
import android.widget.ImageView;
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
import adapter.explore_gridviewadapter;
import it.sephiroth.android.library.picasso.Picasso;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Other_user_profile extends Activity {
    GridView gridViewnew;
    TextView txt_name, Txt_Username, Txt_post, Tittle;
    String Str_UserId, Str_idhome, Str_namehome, Str_EmailHome, Str_genderHome, Str_biohome, Str_websitehome, StrFollowingHome, StrFollowerHome,
            Str_usernameHome, Str_HashWord, accestoken, device_id, StrUserdetail;
    ArrayList<HashMap<String, String>> List_arrayHome = new ArrayList<HashMap<String, String>>();
    Session_manager session;
    ImageView img_profileUser;
    Button BtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other_user_profile);
        gridViewnew = (GridView) findViewById(R.id.gridview);
        txt_name = (TextView) findViewById(R.id.textView14);
        Txt_Username = (TextView) findViewById(R.id.textView11);
        Tittle = (TextView) findViewById(R.id.Tittle);
        BtnBack = (Button)  findViewById(R.id.Btn_back);
        img_profileUser = (ImageView) findViewById(R.id.imageView4);
        session = new Session_manager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);
        device_id = user.get(session.device_id);
        Intent intent = getIntent();
        StrUserdetail = intent.getStringExtra("User_detail");
        Tittle.setText(StrUserdetail);
        new Orher_user().execute();
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class Orher_user extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype, image;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            JSONObject jsonnode, json_User;
            List_arrayHome.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user_id", StrUserdetail));
            //nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            jsonStr = sh.makeServiceCall_withHeader(Constant.OtherUser,
                    ServiceHandler.POST, nameValuePairs, accestoken, device_id);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray array1 = null;
                    jsonnode = jsonObj.getJSONObject("data");
                    array1 = jsonnode.getJSONArray("post");
                    json_User = jsonnode.getJSONObject("user");
                    image = json_User.getString("profile_image");
                    Str_idhome = json_User.getString("id");
                    Str_namehome = json_User.getString("name");
                    Str_EmailHome = json_User.getString("email");
                    Str_genderHome = json_User.getString("gender");
                    Str_biohome = json_User.getString("bio");
                    Str_websitehome = json_User.getString("website");
                    StrFollowingHome = json_User.getString("following");
                    StrFollowerHome = json_User.getString("followers");
                    Str_usernameHome = json_User.getString("username");

                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject c = array1.getJSONObject(i);
                        String media_url = c.getString("media1_url");


                        // tmp hashmap for single contact
                        HashMap<String, String> Profile_images = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        Profile_images.put("media_url", media_url);


                        // adding PostUrl to Array list
                        List_arrayHome.add(Profile_images);

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
            try {
                Picasso.with(Other_user_profile.this).load(image).placeholder(R.drawable.profile_placeholder).into(img_profileUser);
            }catch (java.lang.IllegalArgumentException e){
                e.printStackTrace();
            }

            explore_gridviewadapter adapter = new explore_gridviewadapter(Other_user_profile.this,
                    android.R.layout.simple_list_item_1, List_arrayHome,
                    getApplication());
            gridViewnew.setAdapter(adapter);
            txt_name.setText(Str_namehome);
            //Txt_Username.setText(user_name);
            Txt_Username.setText(Str_usernameHome);
            // Txt_post.setText(Posts);

            //img_loader.DisplayImage(image,img_profile);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
