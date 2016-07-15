package Fragments;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.Other_user_profile;
import com.ToxicBakery.viewpager.transforms.example.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Service_Handler.Constant;
import Session_handler.Session_manager;
import adapter.activity_adapter;
import adapter.pageradapter_activity;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;


public class Like_fagment extends Fragment {
    ProgressDialog pDialog;
    Session_manager session;
    String accestoken;
    private pageradapter_activity mAdapter;
    String device_id;
    ArrayList<HashMap<String, String>> Activity_array_list = new ArrayList<HashMap<String, String>>();
    private ViewPager mViewPager_Activity;
    ListView Lv_activity;
    int paging_position = 0;
    int Scroll_position = 0;
    String paging = "0";
    String Str_check_response, UserName;
    String Str_check_Refresh = "NoScrolled";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_like_fagment, container, false);
        session = new Session_manager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Lv_activity = (ListView) rootView.findViewById(R.id.listView_Actitivity);
        // viewpager_Activity
        //  mViewPager_Activity = (ViewPager)rootView.findViewById(R.id.viewpager_Activity);
        // img_profile.setImageResource(R.drawable.profile_placeholder);
        // access_token
        accestoken = user.get(session.Acess_Token);
//
//
        device_id = user.get(session.device_id);
        new Activity().execute();
        //  mAdapter = new pageradapter_activity(getActivity().getSupportFragmentManager());
        // Fragments and ViewPager Initialization


        //  mViewPager_Activity.setAdapter(mAdapter);
        // mViewPager_Activity.setOffscreenPageLimit(3);

        return rootView;
    }

    private class Activity extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;

            // data.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            String Response = makeServiceCall(Constant.Activity_List);
            if (Response != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(Response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    // JSONArray array1 = null;
                    str = jsonObj.getString("status");
                    if (str.contentEquals("true")) {
                        //jsonnode = jsonObj.getJSONObject("data");
                        JSONArray array1 = null;
                        array1 = jsonObj.getJSONArray("data");
                        int array_size = array1.length();
                        Str_check_response = String.valueOf(array_size);
                        // looping through All Contacts
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject c = array1.getJSONObject(i);

                            String Str_Activity = c.getString("activity");

                            if (Str_Activity.contentEquals("liked")) {
                                JSONObject c2 = c.getJSONObject("post_info");
                                JSONObject c_userinfo = c.getJSONObject("user_info");
                                String id = c_userinfo.getString("id");
                                UserName = c_userinfo.getString("username");
                                String postid = c2.getString("id");
                                String Caption = c2.getString("caption");
                                String status_like = c.getString("status");
                                String media1_thumb_url = c2.getString("media1_thumb_url");
                                String media2_thumb_url = c2.getString("media2_thumb_url");
                                String media3_thumb_url = c2.getString("media3_thumb_url");
                                String media4_thumb_url = c2.getString("media4_thumb_url");
                                // String Str_follower_name = c.getString("follower_name");
                                String Str_likedby_name = c.getString("likedby_name");
                                String Str_likedof_name = c.getString("likedof_name");
                                String Str_media1_url = c2.getString("media1_thumb_url");
                                String Str_Profile_image = c.getString("likedby_image");

                                String Strurls = media1_thumb_url + "," + media2_thumb_url + "," + media3_thumb_url + "," + media4_thumb_url;

                                HashMap<String, String> activity_info = new HashMap<String, String>();
                                activity_info.put("activityy", Str_Activity);
                                //activity_info.put("str_Follower_name", Str_follower_name);
                                activity_info.put("Str_Likedby_name", Str_likedby_name);
                                activity_info.put("Str_Likedof_name", Str_likedof_name);
                                activity_info.put("Str_Media1_url", Str_media1_url);
                                activity_info.put("Str_Profile_image", Str_Profile_image);
                                activity_info.put("userid", id);
                                activity_info.put("username", UserName);
                                activity_info.put("postid", postid);
                                activity_info.put("caption", Caption);
                                activity_info.put("status_like", status_like);
                                activity_info.put("url", Strurls);


                                Activity_array_list.add(activity_info);
                            } else if (Str_Activity.contentEquals("follow")) {
                                String Str_follower = c.getString("follower_name");
                                String Fillow_image = c.getString("follower_image");
                                JSONObject c_userinfo = c.getJSONObject("user_info");
                                String id = c_userinfo.getString("id");
                                UserName = c_userinfo.getString("username");
                                HashMap<String, String> activity_info = new HashMap<String, String>();
                                activity_info.put("activityy", Str_Activity);
                                //activity_info.put("str_Follower_name", Str_follower_name);
                                activity_info.put("Followername", Str_follower);
                                activity_info.put("userid", id);
                                activity_info.put("username", UserName);
                                activity_info.put("Str_Profile_image", Fillow_image);
                                Activity_array_list.add(activity_info);
                            }
//                            [{"activity":"liked","follower_name":null,"following_name":"Kiran Kumar",
//                                    "likedby_name":"Kiran Kumar",
//                                    "likedof_name":"Kiran Kumar",
//                                    "status":"1",
//                                    "media1_url":"http:\/\/stage.turnstr.net\/media\/d4d6666c-059e-11e6-b9d0-0a8627f3e57d.jpg","profile_image":null},{"activity":"liked","follower_name":null,"following_name":"Kiran Kumar","likedby_name":"bhupindergarg","likedof_name":"Kiran Kumar","status":"0","media1_url":"http:\/\/stage.turnstr.net\/media\/48ec2052-12ef-11e6-aa9c-0a8627f3e57d.mov","profile_image":null}
                        }

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

            activity_adapter_local adapter = new activity_adapter_local(getActivity(),
                    android.R.layout.simple_list_item_1, Activity_array_list,
                    getActivity().getApplication());
            Lv_activity.setAdapter(adapter);
            //Scroll_position
            Lv_activity.setSelection(Scroll_position);

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public String makeServiceCall(String url) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type

//				if (params != null) {
//					String paramString = URLEncodedUtils
//							.format(params, "utf-8");
//					url += "?" + paramString;
//				}
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("page", paging));
            HttpPost httpGet = new HttpPost(url);
            httpGet.addHeader("X-TOKEN", accestoken);
            httpGet.addHeader("X-DEVICE", device_id);
            httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpClient.execute(httpGet);


            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public class activity_adapter_local extends ArrayAdapter<String> implements
            View.OnClickListener {


        private Context contexts;
        private Application mAppContext;
        private LayoutInflater mLayoutInflater = null;
        private Random mRandom;
        String List_img;
        String Liked_name;

        ImageLoader img_loader;


        ArrayList<HashMap<String, String>> imagess = new ArrayList<HashMap<String, String>>();
        ArrayList<String> Array_imgadpter = new ArrayList<String>();


        private int mSelectedItem;


        ArrayList<String> Array_single_image = new ArrayList<String>();

        public activity_adapter_local(Context context, int textViewResourceId,
                                      ArrayList<HashMap<String, String>> images, Application app) {
            super(context, textViewResourceId);
            this.mLayoutInflater = LayoutInflater.from(context);
            this.mRandom = new Random();
            this.imagess = images;

            mAppContext = app;


            contexts = context;
        }

        @SuppressWarnings("static-access")
        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            Resources mResources;
            final ViewHolder vh;
            final Context context = parent.getContext();
            // img_loader = new ImageLoader(getActivity());
            // if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.activity_lv_layout,
                    parent, false);
            img_loader = new ImageLoader(mAppContext);
            vh = new ViewHolder();
            vh.Text_tittle = (TextView) convertView.findViewById(R.id.textView36);

            vh.Img_profilepic = (ImageView) convertView.findViewById(R.id.imageView4);
            vh.Like_picture = (ImageView) convertView.findViewById(R.id.picture);

//        } else {
//            vh = (ViewHolder) convertView.getTag();
//
//        }
            // String follower_namee = imagess.get(position).get("str_Follower_name");
            String Activity = imagess.get(position).get("activityy");
            if (Activity.contentEquals("liked")) {
                String str_liked_by_name = imagess.get(position).get("Str_Likedby_name");
                Liked_name = str_liked_by_name;
                String first = " Liked your";
                String next = "<font color='#ffa500'>" + str_liked_by_name + "</font>";
                String next2 = "<font color='#ffa500'> Turn</font>";
                vh.Text_tittle.setText(Html.fromHtml(next + first + next2));
                //vh.Text_tittle.setText(str_liked_by_name + " Liked your Turn");
                try {
                    try {
                        Picasso.with(contexts).load(imagess.get(position).get("Str_Media1_url")).placeholder(R.drawable.placeholderdevzillad).into(vh.Like_picture);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                } catch (java.lang.IllegalArgumentException e) {
                    e.printStackTrace();
                }
                vh.Like_picture.setVisibility(View.VISIBLE);
            } else if (Activity.contentEquals("follow")) {
                String str_liked_by_name = imagess.get(position).get("Followername");
                Liked_name = str_liked_by_name;
                String first = "  Started Following you";
                String next = "<font color='#ffa500'>" + str_liked_by_name + "</font>";
                // String next2 = "<font color='#FFBF00'> Turn</font>";
                vh.Text_tittle.setText(Html.fromHtml(next + first));
                vh.Like_picture.setVisibility(View.INVISIBLE);
            }
            vh.Like_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Intent i1new = new Intent(mAppContext, Other_user_profile.class);
//                i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i1new.putExtra("User_detail", "@" + Liked_name);
//                mAppContext.startActivity(i1new);

//                    String str_id = images.get(position).get("Post_id");
//                    String status_like = images.get(position).get("Liked");
//                    String caption = images.get(position).get("Caption");
//                    List_img = Array_img.get(position);
//                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
//                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
//                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    lObjIntent.putExtra("images", List_img);
//                    lObjIntent.putExtra("Post_id", str_id);
//                    lObjIntent.putExtra("Check_follow", "GONE");
//                    lObjIntent.putExtra("status_like", status_like);
//                    lObjIntent.putExtra("caption", caption);
//                    contexts.startActivity(lObjIntent);
                    String str_id = imagess.get(position).get("postid");
                    String status_like = imagess.get(position).get("status_like");
                    String caption = imagess.get(position).get("caption");
                    List_img = imagess.get(position).get("url");
                    Intent lObjIntent = new Intent(mAppContext, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    lObjIntent.putExtra("Check_follow", "GONE");
                    lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("caption", caption);
                    mAppContext.startActivity(lObjIntent);
                }
            });
            vh.Img_profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i1new = new Intent(mAppContext, Other_user_profile.class);
                    i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i1new.putExtra("User_detail", "@" + imagess.get(position).get("username"));
                    i1new.putExtra("userid", imagess.get(position).get("userid"));
                    mAppContext.startActivity(i1new);
                }
            });

            // Set the ImageView image as drawable object
            // vh.Img_profilepic.setImageDrawable(roundedBitmapDrawable);
            // vh.Img_profilepic
            String Str_profile_image = imagess.get(position).get("Str_Profile_image");

            try {
                //if(Str_profile_image!=null) {
                //img_loader.DisplayImage(fl,vh.Img_profilepic);
                try {
                    Picasso.with(contexts).load(imagess.get(position).get("Str_Profile_image")).placeholder(R.drawable.profile_placeholder).into(vh.Img_profilepic);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();

            }
            if ((position >= getCount() - 1)) {
                if (!Str_check_response.contentEquals("0")) {
                    paging_position++;
                    paging = String.valueOf(paging_position);
                    // paging = "1";
                    Scroll_position = position;
                    Str_check_Refresh = "scrolled";
                    new Activity().execute();
                    //System.out.println("Recycleview page Scrolling...."+position);
                } else {
                    Str_check_Refresh = "NoScrolled";
                    paging_position = 0;
                    paging = "0";
                }
            }

            //   vh.time.setText(imagess.get(position).get("created_at"));
            return convertView;
        }


        class ViewHolder {
            ImageView Img_profilepic, Like_picture, star2, star3, star4;
            TextView Text_tittle, Comment, time, price, rating, distance, TxtVw_Afterdiscount, Txtvw;


        }

        @Override
        public int getCount() {

            return imagess.size();
        }


        @Override
        public void onClick(View v) {

        }


    }

}
