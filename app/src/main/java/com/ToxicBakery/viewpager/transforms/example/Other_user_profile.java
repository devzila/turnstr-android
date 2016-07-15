package com.ToxicBakery.viewpager.transforms.example;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import adapter.Grid_view_adapter;
import adapter.explore_gridviewadapter;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Other_user_profile extends Activity {
    GridView gridViewnew;
    TextView Tittle;
    ArrayList<String> Array_img = new ArrayList<String>();
    String Str_UserId, Str_idhome, Str_namehome, Str_EmailHome, Str_genderHome, Str_biohome, Str_websitehome, StrFollowingHome, StrFollowerHome,
            Str_usernameHome, Str_HashWord, accestoken, device_id, StrUserdetail,Str_postcount;
    ArrayList<HashMap<String, String>> List_arrayHome = new ArrayList<HashMap<String, String>>();
    Session_manager session;
    ImageView img_profileUser;
    SwipeRefreshLayout SRL, Srl_other;
    Button BtnBack;
    RelativeLayout Rlv_follow, Rlv_following;
    TextView txtview_follow;
    String Str_check_response_other;
    int paging_position_other = 0;
    String paging_other = "0";
    int Scroll_position_otheruser;
    RelativeLayout Rlv_profile, Rlv_follow_other;
    String userid;

    TextView txt_name, Txt_Username, Txt_post, TxtLogout, Txthash, Txt_follower, Txvw_following;
    String Check_follow="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other_user_profile);
        gridViewnew = (GridView) findViewById(R.id.gridview);
        txt_name = (TextView) findViewById(R.id.textView14);
        Srl_other = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_other);
        Txt_Username = (TextView) findViewById(R.id.textView11);
        Tittle = (TextView) findViewById(R.id.Tittle);
        BtnBack = (Button)  findViewById(R.id.Btn_back);
        img_profileUser = (ImageView) findViewById(R.id.imageView4);
        Txt_post = (TextView) findViewById(R.id.textView18);
        session = new Session_manager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);
        Rlv_following = (RelativeLayout) findViewById(R.id.rlvfollwing);
        Rlv_profile = (RelativeLayout) findViewById(R.id.Rlv_profile);
        device_id = user.get(session.device_id);
        txtview_follow = (TextView) findViewById(R.id.textView24);
        Rlv_follow = (RelativeLayout) findViewById(R.id.relativeLayout10);
        Txt_follower = (TextView)findViewById(R.id.textView19);
        Txvw_following = (TextView)findViewById(R.id.textView20);
        Intent intent = getIntent();
        StrUserdetail = intent.getStringExtra("User_detail");
        userid= intent.getStringExtra("userid");
        Tittle.setText(StrUserdetail);
        new Orher_user().execute();
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Rlv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(Other_user_profile.this, Follower_following_screen.class);

                int_follower.putExtra("Status", "Followers");
                int_follower.putExtra("userid", userid);
                int_follower.putExtra("Screen_check", "other_user");
                startActivity(int_follower);

            }
        });
        Rlv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(Other_user_profile.this, Follower_following_screen.class);
                int_follower.putExtra("Status", "Following");
                int_follower.putExtra("userid", userid);
                int_follower.putExtra("Screen_check", "other_user");
                startActivity(int_follower);
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
            // List_arrayHome.clear();
            Srl_other.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user_id", StrUserdetail));
            nameValuePairs.add(new BasicNameValuePair("page", paging_other));
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
// String Str_UserId,Str_idhome,Str_namehome,Str_EmailHome,Str_genderHome,Str_biohome,Str_websitehome,StrFollowingHome,StrFollowerHome,
                    //        Str_usernameHome;
                    // Getting JSON Array node
                    // JSONArray array1 = null;
//                    str = jsonObj.getString("status");
//                    JSONArray Array_image = null;
//                    if (str.contentEquals("true")) {
                    //jsonnode = jsonObj.getJSONObject("data");
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
                    Str_postcount = json_User.getString("post_count");
                    Check_follow = json_User.getString("is_following");
                    // Info.put("isfollowing", isfollowing);
                    int array_size = array1.length();
                    Str_check_response_other = String.valueOf(array_size);
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject c = array1.getJSONObject(i);
                        String media_url = c.getString("media1_url");
                        String media1_thumb_url = c.getString("media1_thumb_url");
                        String media2_thumb_url = c.getString("media2_thumb_url");
                        String media3_thumb_url = c.getString("media3_thumb_url");
                        String media4_thumb_url = c.getString("media4_thumb_url");
                        String caption = c.getString("caption");

                        // tmp hashmap for single contact
                        HashMap<String, String> Profile_images = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        String grop_url = media1_thumb_url+","+media2_thumb_url+","+media3_thumb_url+","+media4_thumb_url;
                        Profile_images.put("media_url", media1_thumb_url);
                        Profile_images.put("caption", caption);
                        Profile_images.put("allurl", grop_url);

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
            Grid_view_adapter_profile_local adapter = new Grid_view_adapter_profile_local(Other_user_profile.this,
                    android.R.layout.simple_list_item_1, List_arrayHome,Array_img,
                    getApplication());
            gridViewnew.setAdapter(adapter);
            gridViewnew.setSelection(Scroll_position_otheruser);
            txt_name.setText(Str_namehome);
            //Txt_Username.setText(user_name);
            Txt_Username.setText(Str_usernameHome);
            Txt_post.setText(Str_postcount);
            Txt_follower.setText(StrFollowerHome);
            Txvw_following.setText(StrFollowingHome);
//            if (Str_usernameHome.contentEquals(Str_username)) {
//                Rlv_follow_other.setVisibility(View.GONE);
//                //Rlv_follow_other.setVisibility(View.VISIBLE);
//            } else {
//                Rlv_follow_other.setVisibility(View.VISIBLE);
//                //Divider.setVisibility(View.GONE);
//            }
            if (Check_follow.contentEquals("0")) {
                txtview_follow.setText("Follow");
            } else {
                txtview_follow.setText("Unfollow");
            }
            // Txt_post.setText(Posts);
            try {
                Picasso.with(Other_user_profile.this).load(image).placeholder(R.drawable.profile_placeholder).into(img_profileUser);
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace();
            }

            //img_loader.DisplayImage(image,img_profile);
            Srl_other.setRefreshing(false);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
    public class Grid_view_adapter_profile_local extends ArrayAdapter<String> {
        private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
        private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
        ArrayList<String> Array_img = new ArrayList<String>();
        private static final String TAG = "SampleAdapter";

        private Context contexts;
        private Application mAppContext;
        private LayoutInflater mLayoutInflater = null;
        private Random mRandom;
        String List_img;

        double km, lat_to, lon_to;
        String strlat_to, str_long_to;
        String Str_km = "";

        ArrayList<HashMap<String, String>> images = new ArrayList<HashMap<String, String>>();

        ImageLoader img_loader;

        //img_loader.DisplayImage(url,textViewPosition);
        public Grid_view_adapter_profile_local(Context context, int textViewResourceId,
                                               ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img, Application app) {
            super(context, textViewResourceId);
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mRandom = new Random();
            this.images = images;
            this.Array_img = Array_img;
            mAppContext = app;


            contexts = context;
        }

        @SuppressWarnings("static-access")
        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {

            final ViewHolder vh;
            String image = null;
            img_loader = new ImageLoader(mAppContext);
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.gridview_item,
                        parent, false);
                vh = new ViewHolder();
                //int selectedPage = 0;


//       b }
                vh.imgView = (ImageView) convertView.findViewById(R.id.picture);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            try {
                image = images.get(position).get("media_url");
            } catch (java.lang.IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            // img_loader.DisplayImage(image,vh.imgView);
            try {
                Picasso.with(contexts).load(image).placeholder(R.drawable.placeholderdevzillad).resize(150, 150).into(vh.imgView);
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace();
            }


//            vh.imgView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String str_id = images.get(position).get("Post_id");
//                    String status_like = images.get(position).get("Liked");
//                    String caption = images.get(position).get("caption");
//                    List_img = Array_img.get(position);
//                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
//                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
//                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    lObjIntent.putExtra("images", List_img);
//                    lObjIntent.putExtra("Post_id", str_id);
//                    lObjIntent.putExtra("status_like", status_like);
//                    lObjIntent.putExtra("caption", caption);
//                    contexts.startActivity(lObjIntent);
//                }
//            });

            vh.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Profile_images.put("allurl", grop_url);
                    String str_id = images.get(position).get("Post_id");
                    String status_like = images.get(position).get("Liked");
                    String caption = images.get(position).get("caption");
                    List_img = images.get(position).get("allurl");
                    String str_followfornextscreen;

                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("Check_follow", "Visible");
                    lObjIntent.putExtra("caption", caption);
                    contexts.startActivity(lObjIntent);
                }
            });


            if ((position >= getCount() - 1)) {
                if (!Str_check_response_other.contentEquals("0")) {
                    paging_position_other++;
                    paging_other = String.valueOf(paging_position_other);
                    // paging = "1";
                    Scroll_position_otheruser = position;
                    // Str_check_Refresh = "scrolled";
                    new Orher_user().execute();
                    //System.out.println("Recycleview page Scrolling...."+position);
                } else {
                    // Str_check_Refresh = "NoScrolled";
                    paging_position_other = 0;
                    paging_other = "0";
                }
            }

            //convertView.setTag(vh);
            return convertView;
        }


        class ViewHolder {
            ImageView imgView;


        }

        @Override
        public int getCount() {

            return images.size();
        }


    }
}
