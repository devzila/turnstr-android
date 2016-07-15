package Fragments;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.example.Follower_following_screen;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.R;
import com.sprylab.android.widget.TextureVideoView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import adapter.Grid_view_adapter;
import adapter.SampleAdapter;
import adapter.explore_gridviewadapter;
import circular_imagview.CircularImageView;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;
import view_pager.viewpager;


public class Explorer_fragment extends Fragment {
    GridView gridView;
    ArrayList<HashMap<String, String>> List_Array = new ArrayList<HashMap<String, String>>();
    ProgressDialog pDialog;
    EditText edt_Text_search;
    ImageView img_cross;
    String str_Search;
    Boolean Search = false;
    RecyclerView Lv_explorer;
    ArrayList<String> Array_img = new ArrayList<String>();
    RelativeLayout rlvExplore, Rlv_search;
    LinearLayout Linear_search;
    Button Btn_back;
    Session_manager session;
    String accestoken;
    SwipeRefreshLayout SRL, SRL_recycle, Srl_other;
    SampleAdapter sample_adapter;
    ArrayList<String> Array_single_image = new ArrayList<String>();
    ArrayList<String> Array_Follow = new ArrayList<String>();
    ArrayList<String> Array_FollowCheck = new ArrayList<String>();
    String follow_status, Userid;
    String Str_UserId, Str_idhome, Str_namehome, Str_EmailHome, Str_genderHome, Str_biohome, Str_websitehome, StrFollowingHome, StrFollowerHome,
            Str_usernameHome, Str_HashWord, StrPost_id;
    String device_id, Str_Response;
    RelativeLayout Rlv_profile;
    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    TextView txt_name, Txt_Username, Txt_post;
    ArrayList<HashMap<String, String>> List_arrayHome = new ArrayList<HashMap<String, String>>();
    GridView gridViewnew;
    ImageView img_profileUser;
    String Str_CheckProfile = "profilehidden";
    String CheckHash = "nohash";
    TextView tXtVw_Explore, Txt_follower, Txvw_following, txtview_follow;
    PopupWindow pwindo;
    int paging_position = 0;
    int Scroll_position = 0;
    String paging = "0";
    String Str_check_response, Str_check_response_other, Str_postcount, Check_follow;
    String Str_check_Refresh = "NoScrolled";
    int paging_position_other = 0;
    int Scroll_position_otheruser;
    RelativeLayout Rlv_follow, Rlv_following,Rlv_follow_other;
    String paging_other = "0";
//Rlveditprofile
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_explorer__screen, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        Srl_other = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_other);
        edt_Text_search = (EditText) rootView.findViewById(R.id.editText6);
        img_cross = (ImageView) rootView.findViewById(R.id.imageView29);
        Lv_explorer = (RecyclerView) rootView.findViewById(R.id.listView2);
        rlvExplore = (RelativeLayout) rootView.findViewById(R.id.Rlv_explore);
        txt_name = (TextView) rootView.findViewById(R.id.textView14);
        Txt_Username = (TextView) rootView.findViewById(R.id.textView11);
        Txt_post = (TextView) rootView.findViewById(R.id.textView18);
        tXtVw_Explore = (TextView) rootView.findViewById(R.id.textView34);
        Rlv_search = (RelativeLayout) rootView.findViewById(R.id.linearLayout8);
        Rlv_profile = (RelativeLayout) rootView.findViewById(R.id.Rlv_profile);
        Btn_back = (Button) rootView.findViewById(R.id.butt_back);
        txtview_follow = (TextView) rootView.findViewById(R.id.textView24);
        img_profileUser = (ImageView) rootView.findViewById(R.id.imageView4);
        Txt_follower = (TextView) rootView.findViewById(R.id.textView19);
        gridViewnew = (GridView) rootView.findViewById(R.id.gridview_otheruser);
        Txvw_following = (TextView) rootView.findViewById(R.id.textView20);
        Rlv_follow = (RelativeLayout) rootView.findViewById(R.id.relativeLayout10);
        Rlv_following = (RelativeLayout) rootView.findViewById(R.id.rlvfollwing);
        Rlv_follow_other = (RelativeLayout) rootView.findViewById(R.id.Rlveditprofile);
        SRL = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        SRL_recycle = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayoutRecycleview);
        session = new Session_manager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);
        device_id = user.get(session.device_id);
        SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Array_single_image.clear();
                Scroll_position = 0;
                Str_check_Refresh = "NoScrolled";
                paging_position = 0;
                paging = "0";
                // adding contact to contact list
                List_Array.clear();
                Array_img.clear();
                Array_Follow.clear();
                new Explorer().execute();
            }

        });
        SRL_recycle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                Array_single_image.clear();
                Scroll_position = 0;
                Str_check_Refresh = "NoScrolled";
                paging_position = 0;
                paging = "0";
                // adding contact to contact list
                List_Array.clear();
                Array_img.clear();
                Array_Follow.clear();
                new Explorer().execute();

            }
        });
        Srl_other.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List_arrayHome.clear();
                new Orher_user().execute();
            }
        });
        Rlv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(getActivity(), Follower_following_screen.class);

                int_follower.putExtra("Status", "Followers");
                int_follower.putExtra("userid", Str_idhome);
                int_follower.putExtra("Screen_check", "other_user");
                getActivity().startActivity(int_follower);

            }
        });
        Rlv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(getActivity(), Follower_following_screen.class);
                int_follower.putExtra("Status", "Following");
                int_follower.putExtra("userid", Str_idhome);
                int_follower.putExtra("Screen_check", "other_user");
                getActivity().startActivity(int_follower);
            }
        });
        Rlv_follow_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        new Explorer().execute();

        edt_Text_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:

                        str_Search = edt_Text_search.getText().toString();
                        img_cross.setVisibility(View.VISIBLE);
                        if (!str_Search.contentEquals("")) {
                            img_cross.setVisibility(View.VISIBLE);
                            Search = true;
                            new Explorer().execute();
                        }
                        //Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG).show();
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_cross.setVisibility(View.INVISIBLE);
                edt_Text_search.setText("");
                str_Search = "";
                Search = false;
                new Explorer().execute();
            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Str_CheckProfile = "isShowing";
                if (Str_CheckProfile.contentEquals("isShowing")) {
                    Lv_explorer.setVisibility(View.VISIBLE);
                    rlvExplore.setVisibility(View.VISIBLE);
                    SRL_recycle.setVisibility(View.VISIBLE);
                    Rlv_profile.setVisibility(View.GONE);
                    Str_CheckProfile = "profilehidden";
                    List_arrayHome.clear();
                    paging_other = "0";
                    // paging = "1";
                    Scroll_position_otheruser = 0;
                    img_profileUser.setImageResource(R.drawable.profile_placeholder);
                    txt_name.setText("Loading..");
                    //Txt_Username.setText(user_name);
                    Txt_Username.setText("Loading..");
                } else if (CheckHash.contentEquals("hash")) {
                    CheckHash = "nohash";
                    tXtVw_Explore.setText("EXPLORE");
                    new Explore_follow().execute();

                } else {
                    gridView.setVisibility(View.VISIBLE);
                    Rlv_search.setVisibility(View.VISIBLE);
                    Lv_explorer.setVisibility(View.GONE);
                    rlvExplore.setVisibility(View.GONE);
                    SRL_recycle.setVisibility(View.GONE);
                }

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String str_position = String.valueOf(position);
                gridView.setVisibility(View.GONE);
                Rlv_search.setVisibility(View.GONE);
                Lv_explorer.setVisibility(View.VISIBLE);
                rlvExplore.setVisibility(View.VISIBLE);
                SRL_recycle.setVisibility(View.VISIBLE);
                // new Explore_follow().execute();


                //Lv_explorer.setSelection(position);
                Lv_explorer.scrollToPosition(position);


            }
        });
//        Lv_explorer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView txtTechCharacteristic = (TextView) view.findViewById(R.id.textView24);
//                String txt = txtTechCharacteristic.getText().toString();
//                Toast.makeText(getActivity(),txt,Toast.LENGTH_LONG).show();
//            }
//        });
        return rootView;


    }

    private class Explorer extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode;
        String str;
        String Deviceid, Token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mProgressHUD = ProgressHUD.show(Explorer_Screen.this, "Connecting", true, true, this);
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
            // List_Array.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//            nameValuePairs.add(new BasicNameValuePair("access_token", Access_token));

            // Making a request to url and getting response
            // if(Search=true){
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("searchData", str_Search));
            nameValuePairs.add(new BasicNameValuePair("page", paging));
            //nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
//            jsonStr = sh.makeServiceCall(Constant.Explorer,
//                    ServiceHandler.POST, nameValuePairs);
            jsonStr = sh.makeServiceCall_withHeader(Constant.Explorer, ServiceHandler.POST, nameValuePairs, accestoken, device_id);

            Search = false;


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
                    JSONArray Array_image = null;
                    Array_image = jsonObj.getJSONArray("data");
                    int array_size = Array_image.length();
                    Str_check_response = String.valueOf(array_size);
                    for (int i = 0; i < Array_image.length(); i++) {
                        JSONObject c = Array_image.getJSONObject(i);

                        String media_url = c.getString("media1_url");
                        String media_url2 = c.getString("media2_url");
                        String media_url3 = c.getString("media3_url");
                        String media_url4 = c.getString("media4_url");
                        String id = c.getString("id");
                        String user_id = c.getString("user_id");
                        String caption = c.getString("caption");
                        String Name = c.getString("name");

                        // String Str_Follow = c.getString("follow");
                        String username = c.getString("username");
                        String media1_thumb_url = c.getString("media1_thumb_url");
                        String media2_thumb_url = c.getString("media2_thumb_url");
                        String media3_thumb_url = c.getString("media3_thumb_url");
                        String media4_thumb_url = c.getString("media4_thumb_url");
                        if (caption.contentEquals("null")) {
                            caption = "";
                        }
                        // tmp hashmap for single contact
                        HashMap<String, String> Explorer = new HashMap<String, String>();
                        Explorer.put("Post_id", id);
                        Explorer.put("username", username);
                        Explorer.put("user_id", user_id);
                        Explorer.put("Name", Name);
                        Explorer.put("caption", caption);
                        Explorer.put("media1_thumb_url", media1_thumb_url);
                        Explorer.put("media2_thumb_url", media2_thumb_url);
                        Explorer.put("media3_thumb_url", media3_thumb_url);
                        Explorer.put("media4_thumb_url", media4_thumb_url);
                        Explorer.put("Follow_status", "0");
                        // adding each child node to HashMap key => value
                        Explorer.put("media_url", media_url);

                        Array_single_image.add(media1_thumb_url);
                        // adding contact to contact list
                        List_Array.add(Explorer);
                        Array_img.add(media_url + "," + media_url2 + "," + media_url3 + "," + media_url4);
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

            //  pDialog.dismiss();
            explore_gridviewadapter_local adapter = new explore_gridviewadapter_local(getActivity(),
                    android.R.layout.simple_list_item_1, List_Array,
                    getActivity().getApplication());
//            explore_gridviewadapter adapter = new explore_gridviewadapter(getActivity(),
//                    android.R.layout.simple_list_item_1, List_Array,
//                    getActivity().getApplication());
            gridView.setAdapter(adapter);
            gridView.setSelection(Scroll_position);
            SRL.setRefreshing(false);
            new Explore_follow().execute();
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Explore_follow extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SRL_recycle.setRefreshing(true);
            SRL.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("searchData", str_Search));
            nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            nameValuePairs.add(new BasicNameValuePair("page", paging));
            try {
                jsonStr = sh.makeServiceCall_withHeader(Constant.Explorer, ServiceHandler.POST, nameValuePairs, accestoken, device_id);

            } catch (java.lang.OutOfMemoryError e) {
                e.printStackTrace();
            }


            Search = false;


//            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    JSONArray Array_image = null;
                    Array_image = jsonObj.getJSONArray("data");
                    int array_size = Array_image.length();
                    Str_check_response = String.valueOf(array_size);
                    for (int i = 0; i < Array_image.length(); i++) {
                        JSONObject c = Array_image.getJSONObject(i);

                        String id = c.getString("id");
                        String user_id = c.getString("user_id");
                        String caption = c.getString("caption");
                        String Name = c.getString("name");
                        String username = c.getString("username");
                        String media1_thumb_url = c.getString("media1_thumb_url");
                        String media2_thumb_url = c.getString("media2_thumb_url");
                        String media3_thumb_url = c.getString("media3_thumb_url");
                        String media4_thumb_url = c.getString("media4_thumb_url");
                        String media1_url = c.getString("media1_url");
                        String media2_url = c.getString("media2_url");
                        String media3_url = c.getString("media3_url");
                        String media4_url = c.getString("media4_url");
                        String created_at = c.getString("created_at");
                        String updated_at = c.getString("updated_at");
                        //is_following
                        String isfollowing = c.getString("is_following");
                        //  String like_status = c.getString("liked");
                        String Profile_pic = c.getString("profile_image");
                        String Total_comments = c.getString("comments_count");
                        String Total_like = c.getString("total_likes");

                        //  String follow_status = c.getString("follow");
                        if (caption.contentEquals("null")) {
                            caption = "";
                        }
                        HashMap<String, String> Info = new HashMap<String, String>();
                        Info.put("Post_id", id);
                        Info.put("username", username);
                        Info.put("user_id", user_id);
                        Info.put("Name", Name);
                        Info.put("caption", caption);
                        Info.put("media1_thumb_url", media1_thumb_url);
                        Info.put("media2_thumb_url", media2_thumb_url);
                        Info.put("media3_thumb_url", media3_thumb_url);
                        Info.put("media4_thumb_url", media4_thumb_url);
                        Info.put("media1_url", media1_url);
                        Info.put("media2_url", media2_url);
                        Info.put("media3_url", media3_url);
                        Info.put("media4_url", media4_url);
                        Info.put("created_at", created_at);
                        Info.put("updated_at", updated_at);
                        Info.put("isfollowing", isfollowing);
                        //isfollowing
                        // Info.put("likestatus", like_status);
                        Info.put("profile_picc", Profile_pic);
                        Info.put("Total_comments", Total_comments);
                        Info.put("Total_likes", Total_like);
                        //
                        // nfo.put("followstatus", follow_status);
                        data.add(Info);
                        Array_img.add(media1_url + "," + media2_url + "," + media3_url + "," + media4_url);
                        Array_single_image.add(media1_url);


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

            //  pDialog.dismiss();
//            explorer_lv_adapter adapter = new explorer_lv_adapter(getActivity(),
//                    android.R.layout.simple_list_item_1, List_Array, Array_img,Array_Follow,
//                    getActivity().getApplication());
            MoviesAdapterr mAdapter = new MoviesAdapterr(data, Array_img);
            Lv_explorer.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            Lv_explorer.setLayoutManager(mLayoutManager);

            Lv_explorer.setItemAnimator(new DefaultItemAnimator());
            Lv_explorer.setAdapter(mAdapter);
            SRL.setRefreshing(false);
            SRL_recycle.setRefreshing(false);
            Lv_explorer.scrollToPosition(Scroll_position);

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public class MoviesAdapterr extends RecyclerView.Adapter<MoviesAdapterr.MyViewHolder> {

        // private List<Movie> moviesList;
        ArrayList<HashMap<String, String>> imagess = new ArrayList<HashMap<String, String>>();
        ArrayList<String> Array_imgadpter = new ArrayList<String>();
        String List_img;
        String[] items;
        private Context contexts;
        ImageLoader img_loader;
        private final ArrayList<TransformerItem> TRANSFORM_CLASSES;

        {
            TRANSFORM_CLASSES = new ArrayList<>();


            TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
            //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;
            ImageView imgView, star1, star2, star3, star4;
            TextView Name, Caption, fixed_meal, price, rating, distance, TxtVw_Afterdiscount, Txtvw,
                    Txt_comment, TxvwLike, Follow_Textview;
            Button BTN_DETAIL;
            private viewpager mPager;
            Button button2;
            CircularImageView Circular_imagview;
            RelativeLayout Rlv_Follow;
            Button BtnDelete;

            public MyViewHolder(View view) {
                super(view);
                mPager = (viewpager) view.findViewById(R.id.containerr);
                Name = (TextView) view.findViewById(R.id.textView7);
                Caption = (TextView) view.findViewById(R.id.textView10);
                Circular_imagview = (CircularImageView) view.findViewById(R.id.imageView4);
                BtnDelete = (Button) view.findViewById(R.id.Btn_delete);
                Txt_comment = (TextView) view.findViewById(R.id.textView43);
                Follow_Textview = (TextView) view.findViewById(R.id.textView48);
                Rlv_Follow = (RelativeLayout) view.findViewById(R.id.relativeLayout26);
                TxvwLike = (TextView) view.findViewById(R.id.textView45);
            }
        }


        public MoviesAdapterr(ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img) {
            this.imagess = images;
            this.Array_imgadpter = Array_img;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.explorer_listview, parent, false);

            contexts = parent.getContext();
            img_loader = new ImageLoader(contexts);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            //  Movie movie = moviesList.get(position);
            Resources mResources;

            holder.Name.setText(imagess.get(position).get("username"));
            String username = imagess.get(position).get("username");
            String Check_userFollow_status = imagess.get(position).get("isfollowing");

            if (Check_userFollow_status.contentEquals("1")) {
                holder.Follow_Textview.setText("UnFollow");
            } else if (Check_userFollow_status.contentEquals("0")) {
                // Array_FollowCheck.add(username);
                holder.Follow_Textview.setText("Follow");
            }
//            if (Array_FollowCheck.contains(username)) {
//                holder.Follow_Textview.setText("UnFollow");
//            } else {
//                // Array_FollowCheck.add(username);
//                holder.Follow_Textview.setText("Follow");
//            }
            holder.Rlv_Follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Check_userFollow_status.contentEquals("1")) {
                        // Array_FollowCheck.remove(username);
                        Userid = imagess.get(position).get("user_id");
                        follow_status = "0";
                        List_Array.clear();
                        Array_img.clear();
                        Array_Follow.clear();
                        Scroll_position = position;
                        data.clear();
                        Array_img.clear();
                        Array_single_image.clear();

                        //holder.Follow_Textview.setText("Follow");
                        new Follow_unfollow().execute();
                    } else if (Check_userFollow_status.contentEquals("0")) {
                        //Array_FollowCheck.add(username);
                        Userid = imagess.get(position).get("user_id");
                        follow_status = "1";
                        List_Array.clear();
                        Array_img.clear();
                        Array_Follow.clear();
                        Scroll_position = position;
                        data.clear();
                        Array_img.clear();
                        Array_single_image.clear();

                        //holder.Follow_Textview.setText("Follow");
                        new Follow_unfollow().execute();
                    }
                }
            });

            // String str = "@Kirank ,#nirmal You've gotta #dance like there's nobody watching,#Love like you'll never be #hurt,#Sing like there's @nobody listening,And live like it's #heaven on #earth.";
            String str = imagess.get(position).get("caption");

            ArrayList<int[]> hashtagSpans1 = getSpans(str, '#');
            ArrayList<int[]> calloutSpans1 = getSpans(str, '@');

            SpannableString commentsContent1 =
                    new SpannableString(str);

            setSpanComment(commentsContent1, hashtagSpans1);
            setSpanUname(commentsContent1, calloutSpans1);

            holder.Caption.setMovementMethod(LinkMovementMethod.getInstance());
            holder.Caption.setText(commentsContent1);


            List_img = Array_imgadpter.get(position);
            PageAdapter mAdapter;
            FragmentManager fm = ((Activity) contexts).getFragmentManager();
            holder.mPager.setId(position + 1);
            mAdapter = new PageAdapter(fm);

            holder.mPager.setAdapter(mAdapter);
            try {
                holder.mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Set the ImageView image as drawable object
            //  holder.Circular_imagview.setBorderWidth(5);
            //holder.Circular_imagview.
            //  holder.Circular_imagview.setBorderColor(Color.BLUE);
            //  holder.Circular_imagview.setImageDrawable(roundedBitmapDrawable);
            String Str_profile_pic = imagess.get(position).get("profile_picc");
            //img_loader.DisplayImage(imagess.get(position).get("profile_picc"), holder.Circular_imagview);
            try {
                Picasso.with(contexts).load(Str_profile_pic).placeholder(R.drawable.profile_placeholder).into(holder.Circular_imagview);
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace();
            }
            holder.Txt_comment.setText("Comments " + imagess.get(position).get("Total_comments"));
            holder.TxvwLike.setText("Likes " + imagess.get(position).get("Total_likes"));
            holder.Circular_imagview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(contexts, "click", Toast.LENGTH_LONG).show();
                    Rlv_profile.setVisibility(View.VISIBLE);

                    Lv_explorer.setVisibility(View.GONE);
                    Str_CheckProfile = "isShowing";
                    Str_UserId = imagess.get(position).get("user_id");
                    new Orher_user().execute();
                }
            });
            holder.BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    RlvDelete.setVisibility(View.VISIBLE);
//                    RlvDelete.bringToFront();
                    // Lv.


                    //  Str_UserId = imagess.get(position).get("user_id");
                    StrPost_id = imagess.get(position).get("Post_id");
                    initiatePopupWindow();

                }
            });
            holder.Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int positionn = holder.mPager.getId();
                    positionn = positionn - 1;
//                String postion = String.valueOf(positionn);

                    try {
                        List_img = Array_imgadpter.get(positionn);
                        items = List_img.split(",");

                    } catch (java.lang.IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onPageSelected(int position) {
                    int positionn = holder.mPager.getId();
                    positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
                    try {
                        List_img = Array_imgadpter.get(positionn);
                        items = List_img.split(",");

                    } catch (java.lang.IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    int positionn = holder.mPager.getId();
                    positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
                    try {
                        List_img = Array_imgadpter.get(positionn);
                        items = List_img.split(",");

                    } catch (java.lang.IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }


                }


            });
            final Handler h = new Handler(Looper.getMainLooper());
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        holder.mPager.setCurrentItem(1);
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    // h.postDelayed(r, 5000);
                }
            };
            h.postDelayed(r, 5000);
            holder.mPager.setOnItemClickListener(new viewpager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    int id = holder.mPager.getId();
                    id = id - 1;
                    String postion = String.valueOf(id);
                    List_img = Array_imgadpter.get(id);
                    String str_id = imagess.get(id).get("Post_id");
                    //String status_like = imagess.get(id).get("likestatus");
                    String caption = imagess.get(id).get("caption");
                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("Check_follow", "VISIBLE");
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    // lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("caption", caption);
                    contexts.startActivity(lObjIntent);
                    //finish();
                }
            });
            if(!CheckHash.contentEquals("hash")) {
                if ((position >= getItemCount() - 1)) {
                    if (!Str_check_response.contentEquals("0")) {
                        paging_position++;
                        paging = String.valueOf(paging_position);
                        // paging = "1";
                        Scroll_position = position;
                        Str_check_Refresh = "scrolled";
                        new Explore_follow().execute();
                        //System.out.println("Recycleview page Scrolling...."+position);
                    } else {
                        Str_check_Refresh = "NoScrolled";
                        paging_position = 0;
                        paging = "0";
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return imagess.size();
        }

        public class PlaceholderFragment extends android.app.Fragment {

            private static final String EXTRA_POSITION = "EXTRA_POSITION";
            //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                final int position = getArguments().getInt(EXTRA_POSITION);
                View view = inflater.inflate(R.layout.frame_videoview, container, false); //Contains empty RelativeLayout

                final ImageView textViewPosition = (ImageView) view.findViewById(R.id.Imag_upload_view);
                TextureVideoView video_Vw = (TextureVideoView) view.findViewById(R.id.frame_video_view);

                //final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);

                items = List_img.split(",");

                String url = items[position];
                // if (video_Vw.isPlaying()) {
                // video_Vw.stopPlayback();
                //  }
                if (url.endsWith(".mp4")) {

//                    MediaController media_Controller;
//                    DisplayMetrics dm;
                    video_Vw.setVisibility(View.VISIBLE);
                    textViewPosition.setVisibility(View.GONE);
//                    media_Controller = new MediaController(getActivity());
//                    dm = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//                    int height = dm.heightPixels;
//                    int width = dm.widthPixels;
//                    video_Vw.setMinimumWidth(width);
//                    video_Vw.setMinimumHeight(height);
//                    video_Vw.setMediaController(null);
                    //video_Vw.setVolume(0f, 0f);
                    Uri video = Uri.parse(url);
                    //Uri video = Uri.parse(url);
                    //  video_Vw.setVideoURI(video);
                    // video_Vw.setOnPreparedListener(PreparedListener);
//                    video_Vw.setup(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//                    video_Vw.requestFocus();
//                    video_Vw.setFrameVideoViewListener(new FrameVideoViewListener() {
//                        @Override
//                        public void mediaPlayerPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//                        }
//
//                        @Override
//                        public void mediaPlayerPrepareFailed(MediaPlayer mediaPlayer, String error) {
//
//                        }
//                    });
                    video_Vw.setVideoURI(video);
                    // video_Vw.
                    video_Vw.setMediaController(null);
                    video_Vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(final MediaPlayer mp) {
//                            startVideoPlayback();
//                            startVideoAnimation();
                            mp.setVolume(0f, 0f);
                            mp.start();
                            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    //video_Vw.setBackground(null);
                                    return true;
                                }
                            });
                        }
                    });
                    video_Vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                            // video_Vw.animate().rotationBy(360.0f).setDuration(video_Vw.getDuration()).start();
                        }
                    });
                    //video_Vw.

                    //video_Vw.setZOrderOnTop(true);
                    //video_Vw.seekTo(100);
                } else {
//                    if (video_Vw.isPlaying()) {
//                        video_Vw.stopPlayback();
//                    }
                    video_Vw.setVisibility(View.GONE);
                    textViewPosition.setVisibility(View.VISIBLE);
                    try {

                        // Picasso.with(contexts).load(items[position]).placeholder(R.drawable.placeholderdevzillad).resize(400, 400).centerCrop().into(textViewPosition);
                        //
                        img_loader.DisplayImage(url, textViewPosition);


                    } catch (OutOfMemoryError error) {

                    }
                }
//                video_Vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//
//                        video_Vw.start();
//                    }
//                });
//                video_Vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//
//                        try {
//                            if (mp.isPlaying()) {
//                                mp.pause();
//                                mp.release();
//                                mp = new MediaPlayer();
//                            }
//                            mp.setVolume(0f, 0f);
//                            mp.setLooping(false);
//
//                            mp.start();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }
//                        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                            @Override
//                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//                                    video_Vw.setBackground(null);
//
//                                    //textViewPosition.setVisibility(View.VISIBLE);
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });
//                    }
//                });


                return view;
            }


        }

        private class PageAdapter extends FragmentStatePagerAdapter {

            public PageAdapter(FragmentManager fragmentManager) {
                super(fragmentManager);
            }

            @Override
            public android.app.Fragment getItem(int position) {
                final Bundle bundle = new Bundle();
                bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position);

                final PlaceholderFragment fragment = new PlaceholderFragment();
                fragment.setArguments(bundle);

                return fragment;
            }

            @Override
            public int getCount() {
                return 4;
            }

        }

        private final class TransformerItem {

            final String title;
            final Class<? extends ViewPager.PageTransformer> clazz;

            public TransformerItem(Class<? extends ViewPager.PageTransformer> clazz) {
                this.clazz = clazz;
                title = clazz.getSimpleName();
            }

            @Override
            public String toString() {
                return title;
            }

        }
    }

    public ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<int[]>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return spans;
    }

    private void setSpanComment(SpannableString commentsContent, ArrayList<int[]> hashtagSpans) {
        for (int i = 0; i < hashtagSpans.size(); i++) {
            int[] span = hashtagSpans.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            commentsContent.setSpan(new Hashtagg(getActivity()),
                    hashTagStart,
                    hashTagEnd, 0);

        }


    }

    private void setSpanUname(SpannableString commentsUname, ArrayList<int[]> calloutSpans) {
        for (int i = 0; i < calloutSpans.size(); i++) {
            int[] span = calloutSpans.get(i);
            int calloutStart = span[0];
            int calloutEnd = span[1];
            commentsUname.setSpan(new CalloutLinkk(getActivity()),
                    calloutStart,
                    calloutEnd, 0);

        }
    }

    //#TAG
    public class Hashtagg extends ClickableSpan {
        Context context;
        TextPaint textPaint;

        public Hashtagg(Context ctx) {
            super();
            context = ctx;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            textPaint = ds;
            ds.setColor(ds.linkColor);
            ds.setARGB(255, 30, 144, 255);
        }

        @Override
        public void onClick(View widget) {
            TextView tv = (TextView) widget;
            Spanned s = (Spanned) tv.getText();
            int start = s.getSpanStart(this);
            int end = s.getSpanEnd(this);
            Str_HashWord = s.subSequence(start + 1, end).toString();
            CheckHash = "hash";
            tXtVw_Explore.setText("#" + Str_HashWord);
            // you can start another activity here
            // Toast.makeText(context, String.format("Tagg : %s", theWord), Toast.LENGTH_LONG).show();
            new hashSearch().execute();
        }

    }

    //@CLASS
    public class CalloutLinkk extends ClickableSpan {
        Context context;

        public CalloutLinkk(Context ctx) {
            super();
            context = ctx;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setARGB(255, 51, 51, 51);
            ds.setColor(Color.RED);

        }

        @Override
        public void onClick(View widget) {
            TextView tv = (TextView) widget;
            Spanned s = (Spanned) tv.getText();
            int start = s.getSpanStart(this);
            int end = s.getSpanEnd(this);
            String theWord = s.subSequence(start + 1, end).toString();
            Rlv_profile.setVisibility(View.VISIBLE);

            Lv_explorer.setVisibility(View.GONE);
            Str_CheckProfile = "isShowing";
            Str_UserId = "@" + theWord;
            new Orher_user().execute();
            // you can start another activity here

            // Toast.makeText(context, String.format("@"+theWord, "@"+theWord), Toast.LENGTH_LONG).show();
        }
    }

    private class Follow_unfollow extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("following_id", Userid));
            nameValuePairs.add(new BasicNameValuePair("following_status", follow_status));


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall_withHeader(Constant.Follow_unfollow,
                    ServiceHandler.POST, nameValuePairs, accestoken, device_id);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                // try {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONArray Array_image = null;

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            //  new Follow_unfollow().execute();
            new Explore_follow().execute();

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
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
            //List_arrayHome.clear();
            Srl_other.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user_id", Str_UserId));
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
                    int array_size = array1.length();
                    Str_check_response_other = String.valueOf(array_size);
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject c = array1.getJSONObject(i);
                        String media_url = c.getString("media1_url");
                        String caption = c.getString("caption");
                       // String media_url = c.getString("media1_url");
                        String media1_thumb_url = c.getString("media1_thumb_url");
                        String media2_thumb_url = c.getString("media2_thumb_url");
                        String media3_thumb_url = c.getString("media3_thumb_url");
                        String media4_thumb_url = c.getString("media4_thumb_url");
                      //  String caption = c.getString("caption");

                        // tmp hashmap for single contact
                        HashMap<String, String> Profile_images = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        String grop_url = media1_thumb_url+","+media2_thumb_url+","+media3_thumb_url+","+media4_thumb_url;
                        Profile_images.put("media_url", media1_thumb_url);
                        Profile_images.put("caption", caption);
                        Profile_images.put("allurl", grop_url);


                        // tmp hashmap for single contact



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
            //Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile_placeholder).into(img_profileUser);
            Grid_view_adapter_profile_local_explore adapter = new Grid_view_adapter_profile_local_explore(getActivity(),
                    android.R.layout.simple_list_item_1, List_arrayHome, Array_img,
                    getActivity().getApplication());
            gridViewnew.setAdapter(adapter);
            gridViewnew.setSelection(Scroll_position_otheruser);
            txt_name.setText(Str_namehome);
            //Txt_Username.setText(user_name);
            Txt_Username.setText(Str_usernameHome);
            Txt_post.setText(Str_postcount);
            Txt_follower.setText(StrFollowerHome);
            Txvw_following.setText(StrFollowingHome);
            if (Check_follow.contentEquals("0")) {
                txtview_follow.setText("Follow");
            } else {
                txtview_follow.setText("Unfollow");
            }
            // Txt_post.setText(Posts);
            try {
                Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile_placeholder).into(img_profileUser);
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace();
            }

            //img_loader.DisplayImage(image,img_profile);
            Srl_other.setRefreshing(false);
            // Txt_post.setText(Posts);

            //img_loader.DisplayImage(image,img_profile);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class hashSearch extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            SRL.setRefreshing(true);
            android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
            data.clear();
            Array_img.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            // publishProgress("Please wait...");

            //String Response = makeServiceCall("http://stage.turnstr.net/api/tag/" + Str_HashWord + "?page=0");
            //String Response = makeServiceCall("http://turnstr.net/api/tag/" + Str_HashWord + "?page=0");
            String Response = makeServiceCallfor_hashtag("http://turnstr.net/api/tag/" + Str_HashWord + "?page=0");
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
                    try{


                    str = jsonObj.getString("status");
                    if (str.contentEquals("true")) {
                        //jsonnode = jsonObj.getJSONObject("data");
                        JSONArray array1 = null;
                        array1 = jsonObj.getJSONArray("data");

                        // looping through All Contacts
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject c = array1.getJSONObject(i);

                            String id = c.getString("id");
                            String user_id = c.getString("user_id");
                            String caption = c.getString("caption");
                            String Name = c.getString("name");
                            String username = c.getString("username");
                            String media1_thumb_url = c.getString("media1_thumb_url");
                            String media2_thumb_url = c.getString("media2_thumb_url");
                            String media3_thumb_url = c.getString("media3_thumb_url");
                            String media4_thumb_url = c.getString("media4_thumb_url");
                            String media1_url = c.getString("media1_url");
                            String media2_url = c.getString("media2_url");
                            String media3_url = c.getString("media3_url");
                            String media4_url = c.getString("media4_url");
                            String created_at = c.getString("created_at");
                            String updated_at = c.getString("updated_at");
                            String like_status = c.getString("liked");
                            String isfollowing = c.getString("is_following");
                            String Profile_pic = c.getString("profile_image");
                            String Total_comments = c.getString("total_comments");
                            String Total_like = c.getString("total_likes");

                            // String follow_status = c.getString("follow");
                            if (caption.contentEquals("null")) {
                                caption = "";
                            }
                            HashMap<String, String> Info = new HashMap<String, String>();
                            Info.put("Post_id", id);
                            Info.put("username", username);
                            Info.put("user_id", user_id);
                            Info.put("Name", Name);
                            Info.put("caption", caption);
                            Info.put("media1_thumb_url", media1_thumb_url);
                            Info.put("media2_thumb_url", media2_thumb_url);
                            Info.put("media3_thumb_url", media3_thumb_url);
                            Info.put("media4_thumb_url", media4_thumb_url);
                            Info.put("media1_url", media1_url);
                            Info.put("media2_url", media2_url);
                            Info.put("media3_url", media3_url);
                            Info.put("media4_url", media4_url);
                            Info.put("created_at", created_at);
                            Info.put("updated_at", updated_at);
                            Info.put("likestatus", like_status);
                            Info.put("profile_picc", Profile_pic);
                            Info.put("Total_comments", Total_comments);
                            Info.put("Total_likes", Total_like);
                            Info.put("isfollowing", isfollowing);
                            //
                            // nfo.put("followstatus", follow_status);
                            data.add(Info);
                            Array_img.add(media1_url + "," + media2_url + "," + media3_url + "," + media4_url);
                            Array_single_image.add(media1_url);


//						String userid = jsonnode.getString("user_id");
//						Name = json_User.getString("name");
//						access_tocken= jsonnode.getString("access_token");
//						Ostype= jsonnode.getString("device_id");
                        }
                        // looping through All Contacts
                    }
                    }catch (java.lang.RuntimeException e){
                        e.printStackTrace();
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
            // pDialog.dismiss();
//            adapter = new SampleAdapter(getActivity(),
//                    android.R.layout.simple_list_item_1, data, Array_img,Array_single_image,
//                    getActivity().getApplication());
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//            Lv.setLayoutManager(mLayoutManager);
//            MyRecyclerAdapter adapter2 = new MyRecyclerAdapter(getActivity(), data,Array_img);
            MoviesAdapterr mAdapter = new MoviesAdapterr(data, Array_img);
            Lv_explorer.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            Lv_explorer.setLayoutManager(mLayoutManager);

            Lv_explorer.setItemAnimator(new DefaultItemAnimator());
            Lv_explorer.setAdapter(mAdapter);
            SRL.setRefreshing(false);

            //    Lv.setItemAnimator(new DefaultItemAnimator());

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

            HttpPost httpGet = new HttpPost(url);
            httpGet.addHeader("X-TOKEN", accestoken);
            httpGet.addHeader("X-DEVICE", device_id);


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

    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater

            ViewGroup vg;
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popuphome,
                    (ViewGroup) getActivity().findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            TextView close = (TextView) layout.findViewById(R.id.txt_cancel);
            ImageView Divider = (ImageView) layout.findViewById(R.id.imageView30);
            TextView Delete = (TextView) layout.findViewById(R.id.Txt_delete);
            TextView Report = (TextView) layout.findViewById(R.id.textView40);


            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            });
            Report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Reportappropriate().execute();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Reportappropriate extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {

        JSONObject jsonnode, json_User;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            JSONObject jsonnode, json_User;
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;
            //  String User_postid = Str_UserId + "," + StrPost_id;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_id", Str_UserId));
            nameValuePairs.add(new BasicNameValuePair("post_id", StrPost_id));
            nameValuePairs.add(new BasicNameValuePair("report_content", "Inappropriate content"));
            //nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            Str_Response = sh.makeServiceCall_withHeader(Constant.report_inappropriate,
                    ServiceHandler.POST, nameValuePairs, accestoken, device_id);

            if (Str_Response != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(Str_Response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
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
            if (Str_Response.contains("true")) {
                Toast.makeText(getActivity(), "Thanks for Reporting", Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
            pwindo.dismiss();

            // new Login().execute();
            // Lv.scrollToPosition(5);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    /////////////////////////////////////////////////////////////////////
    //gridview adapter here
    public class explore_gridviewadapter_local extends ArrayAdapter<String> {
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
        public explore_gridviewadapter_local(Context context, int textViewResourceId,
                                             ArrayList<HashMap<String, String>> images, Application app) {
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
            String image = images.get(position).get("media_url");
            try {
                // img_loader.DisplayImage(image, vh.imgView);
                // img_loader.clearCache();
                // img_loader.DisplayImage(image, vh.imgView);
                // img_loader.

                Picasso.with(contexts).load(image).placeholder(R.drawable.placeholderdevzillad).resize(130, 130).centerCrop().into(vh.imgView);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            if ((position >= getCount() - 1)) {
                if(images.size()!=1){
                if (!Str_check_response.contentEquals("0")) {
                    paging_position++;
                    paging = String.valueOf(paging_position);
                    // paging = "1";
                    Scroll_position = position;
                    Str_check_Refresh = "scrolled";
                    new Explorer().execute();
                }
                    //System.out.println("Recycleview page Scrolling...."+position);
                } else {
                    Str_check_Refresh = "NoScrolled";
                    paging_position = 0;
                    paging = "0";
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


    public class Grid_view_adapter_profile_local_explore extends ArrayAdapter<String> {
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
        public Grid_view_adapter_profile_local_explore(Context context, int textViewResourceId,
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
                Picasso.with(contexts).load(image).placeholder(R.drawable.placeholderdevzillad).resize(150, 150).centerCrop().into(vh.imgView);
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace();
            }


            vh.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_id = images.get(position).get("Post_id");
                    String status_like = images.get(position).get("Liked");
                    String caption = images.get(position).get("caption");
                    List_img = images.get(position).get("allurl");
                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    lObjIntent.putExtra("Check_follow", "VISIBLE");
                    lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("caption", caption);
                    contexts.startActivity(lObjIntent);
                }
            });

            if ((position >= getCount() - 1)) {
                if(images.size()!=1){
                if (!Str_check_response_other.contentEquals("0")) {
                    paging_position_other++;
                    paging_other = String.valueOf(paging_position_other);
                    // paging = "1";
                    Scroll_position_otheruser = position;
                    // Str_check_Refresh = "scrolled";
                    new Orher_user().execute();
                }
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
    public String makeServiceCallfor_hashtag(String url) {
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

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("X-TOKEN", accestoken);
            httpGet.addHeader("X-DEVICE", device_id);


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
}