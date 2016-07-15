package Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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

import Day_time_calculator.time_calculator;
import ENDLESS_PAGER_ADAPTER.EndLessAdapter;
import Endless_scroll.EndlessRecyclerOnScrollListener;
import Endless_scroll.EndlessScrollListener;
import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;

import adapter.Grid_view_adapter;
import adapter.SampleAdapter;
import circular_imagview.CircularImageView;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;
import view_pager.viewpager;

public class home_fragment extends Fragment {
    Session_manager session;
    ProgressDialog pDialog;
    RelativeLayout RlvDelete;
    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_img = new ArrayList<String>();
    ArrayList<String> Array_single_image = new ArrayList<String>();
    SampleAdapter adapter;
    MoviesAdapterr.PlaceholderFragment fragment;
    String accestoken;
    int Scroll_position = 0;
    String device_id;
    CircularImageView img_profile;
    Resources mResources;
    RecyclerView Lv;
    PopupWindow pwindo;
    String StrPost_id;
    SwipeRefreshLayout SRL, Srl_other;
    RelativeLayout Rlv_profile, Rlv_follow_other;
    TextView Txt_trnst;
    ImageView img_profileUser;
    String Str_Response;
    String paging = "0";
    ImageView Img_back;
    String CheckHash = "nohash";
    TextView txt_name, Txt_Username, Txt_post, TxtLogout, Txthash, Txt_follower, Txvw_following;
    String Str_UserId, Str_idhome, Str_namehome, Str_EmailHome, Str_genderHome, Str_biohome, Str_websitehome, StrFollowingHome, StrFollowerHome,
            Str_usernameHome, Str_HashWord, Str_postcount;
    ArrayList<HashMap<String, String>> List_arrayHome = new ArrayList<HashMap<String, String>>();
    GridView gridView;
    private LinearLayoutManager mLayoutManager;
    int paging_position = 1;
    int paging_position_other = 0;
    int Scroll_position_otheruser;
    String Str_username, Str_chaeck_username;
    //    private boolean loading = true;
//    int pastVisiblesItems, visibleItemCount, totalItemCount,firstVisibleItem,previousTotal;
    MediaPlayer mp_Full;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    String Response, Str_check_response, Str_check_response_other, Check_follow;
    String Str_check_Refresh = "NoScrolled";
    String paging_other = "0";
    RelativeLayout Rlv_follow, Rlv_following;
    TextView txtview_follow;
    int firstVisibleItem, visibleItemCount, totalItemCount;
//    public home_fragment() {
//      //  home_fragment hm =  new home_fragment();
//        // empty constructor
//    }

    /////////////////////


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_activity, container, false);
        Lv = (RecyclerView) rootView.findViewById(R.id.listView);
        SRL = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        Srl_other = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout_other);
        Rlv_profile = (RelativeLayout) rootView.findViewById(R.id.Rlv_profile);
        RlvDelete = (RelativeLayout) rootView.findViewById(R.id.Rlv_dlt);
        Txt_trnst = (TextView) rootView.findViewById(R.id.txtVw_trnstr);
        Img_back = (ImageView) rootView.findViewById(R.id.img_back);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        txt_name = (TextView) rootView.findViewById(R.id.textView14);
        Txt_Username = (TextView) rootView.findViewById(R.id.textView11);
        Txt_post = (TextView) rootView.findViewById(R.id.textView18);
        Txt_follower = (TextView) rootView.findViewById(R.id.textView19);
        Txvw_following = (TextView) rootView.findViewById(R.id.textView20);
        Txthash = (TextView) rootView.findViewById(R.id.Txt_hash);
        TxtLogout = (TextView) rootView.findViewById(R.id.Txtlogout);
        Rlv_follow_other = (RelativeLayout) rootView.findViewById(R.id.Rlveditprofile);
        Rlv_follow = (RelativeLayout) rootView.findViewById(R.id.relativeLayout10);
        Rlv_following = (RelativeLayout) rootView.findViewById(R.id.rlvfollwing);
        img_profileUser = (ImageView) rootView.findViewById(R.id.imageView4);
        txtview_follow = (TextView) rootView.findViewById(R.id.textView24);
        mResources = getActivity().getResources();
        mLayoutManager = new LinearLayoutManager(getActivity());
        Lv.setLayoutManager(mLayoutManager);


        //  Lv.seto
        SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                Array_img.clear();
                Scroll_position = 0;
                Str_check_Refresh = "NoScrolled";
                paging_position = 0;
                paging = "0";
                new Login().execute();

            }

        });
        Srl_other.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List_arrayHome.clear();
                new Orher_user().execute();
            }
        });
//        if (Str_usernameHome.contentEquals(Str_username)) {
//            Rlv_follow_other.setVisibility(View.GONE);
//            //Rlv_follow_other.setVisibility(View.VISIBLE);
//        } else {
//            Rlv_follow_other.setVisibility(View.VISIBLE);
//            //Divider.setVisibility(View.GONE);
//        }
//        if(Check_follow.contentEquals("0")){
//            txtview_follow.setText("Follow");
//        }else{
//            txtview_follow.setText("Unfollow");
//        }
        Rlv_follow_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Check_follow.contentEquals("0")) {
                    txtview_follow.setText("Unfollow");
                    Check_follow = "1";
                    new Follow_unfollow().execute();

                } else if (Check_follow.contentEquals("1")) {
                    txtview_follow.setText("Follow");
                    Check_follow = "0";
                    new Follow_unfollow().execute();

                }

                // follow_status
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
//        Lv.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                System.out.println("Recycleview page...."+current_page);
//                //Toast.makeText(getActivity(),"Triggered",Toast.LENGTH_LONG);
//            }
//        });
//        Lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                //System.out.println("Recycleview page Scrolling....");
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
        //Lv.addOnScrollListener(new );
//        Lv.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                visibleItemCount = Lv.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
//
//                if (loading) {
//                    if (totalItemCount > previousTotal) {
//                        loading = false;
//                        previousTotal = totalItemCount;
//                    }
//                }
//                if (!loading && (totalItemCount - visibleItemCount)
//                        <= (firstVisibleItem + visibleThreshold)) {
//                    // End has been reached
//                                        Log.i("Yaeye!", "end called");
//                    System.out.println("end has been reached....");
//
//                    // Do something
//
//                    loading = true;
//                }
//            }
//        });
        session = new Session_manager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // img_profile.setImageResource(R.drawable.profile_placeholder);
        // access_token
        accestoken = user.get(session.Acess_Token);

        Str_username = user.get(session.Username);
        device_id = user.get(session.device_id);
        new Login().execute();

//        Rlv_profile.setVisibility(View.VISIBLE);
//        SRL.setVisibility(View.GONE);
//        Txt_trnst.setVisibility(View.GONE);
//        Img_back.setVisibility(View.VISIBLE);
        TxtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });
        Img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rlv_profile.setVisibility(View.GONE);
                SRL.setVisibility(View.VISIBLE);
                Txt_trnst.setVisibility(View.VISIBLE);
                Img_back.setVisibility(View.GONE);
                Txthash.setVisibility(View.GONE);
                img_profileUser.setImageResource(R.drawable.profile_placeholder);
                txt_name.setText("Loading..");
                //Txt_Username.setText(user_name);
                Txt_Username.setText("Loading..");
                if (CheckHash.contentEquals("hash")) {
                    CheckHash = "nohash";
                    new Login().execute();

                }
            }
        });
        //Lv.seton


        return rootView;
    }

    class Login extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype;
        //Str_check_Refresh = "NoScrolled";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            SRL.setRefreshing(true);
            android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
            if (Str_check_Refresh.contentEquals("NoScrolled")) {
                data.clear();
                Array_img.clear();

            }
//            data.clear();
//            Array_img.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            // publishProgress("Please wait...");
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("page", paging));
            //nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            Response = sh.makeServiceCall_withHeader(Constant.Fetch_posts_homepage,
                    ServiceHandler.GET, nameValuePairs, accestoken, device_id);
            //  String Response = makeServiceCall(Constant.Fetch_posts_homepage);
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
//createdTime
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
                            String Profile_pic = c.getString("profile_image");
                            String Total_comments = c.getString("total_comments");
                            String Total_like = c.getString("total_likes");
                            String Created_time = c.getString("createdTime");

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
                            Info.put("Created_timee", Created_time);
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
                } catch (JSONException e) {
                    Response = "";
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
//            adapter = new SampleAdapter(getActivity(),
//                    android.R.layout.simple_list_item_1, data, Array_img,Array_single_image,
//                    getActivity().getApplication());
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//            Lv.setLayoutManager(mLayoutManager);
//            MyRecyclerAdapter adapter2 = new MyRecyclerAdapter(getActivity(), data,Array_img);
            MoviesAdapterr mAdapter = new MoviesAdapterr(data, Array_img);
            Lv.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            Lv.setLayoutManager(mLayoutManager);

            Lv.setItemAnimator(new DefaultItemAnimator());
            Lv.setAdapter(mAdapter);
            SRL.setRefreshing(false);
            Lv.scrollToPosition(Scroll_position);
            // Lv.sets

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
            TextView Name, Caption, Day_time, fixed_meal, price, rating, distance, TxtVw_Afterdiscount, Txtvw,
                    Txt_comment, TxvwLike;
            Button BTN_DETAIL;
            private viewpager mPager;
            Button button2;
            CircularImageView Circular_imagview;

            Button BtnDelete;

            public MyViewHolder(View view) {
                super(view);
                mPager = (viewpager) view.findViewById(R.id.containerr);
                Name = (TextView) view.findViewById(R.id.textView7);
                Caption = (TextView) view.findViewById(R.id.textView10);
                Circular_imagview = (CircularImageView) view.findViewById(R.id.imageView4);
                BtnDelete = (Button) view.findViewById(R.id.Btn_delete);
                Day_time = (TextView) view.findViewById(R.id.time);
                Txt_comment = (TextView) view.findViewById(R.id.textView43);

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
                    .inflate(R.layout.list_home_lv, parent, false);

            contexts = parent.getContext();
            img_loader = new ImageLoader(contexts);
            //   img_loader.clearCache();
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            //  Movie movie = moviesList.get(position);
            Resources mResources;
            //Info.put("Created_timee", Created_time);
            holder.Name.setText(imagess.get(position).get("username"));
            String Str_day_time = imagess.get(position).get("Created_timee");
//            time_calculator time_lass = new time_calculator();
//            String Filtertime = time_lass.parseDate(Str_day_time);
            holder.Day_time.setText(Str_day_time);
            //if (Response != null) {


            String str = imagess.get(position).get("caption");
            //String str = "imagess.get(position).get";


            ArrayList<int[]> hashtagSpans1 = getSpans(str, '#');
            ArrayList<int[]> calloutSpans1 = getSpans(str, '@');

            SpannableString commentsContent1 =
                    new SpannableString(str);

            setSpanComment(commentsContent1, hashtagSpans1);
            setSpanUname(commentsContent1, calloutSpans1);

            holder.Caption.setMovementMethod(LinkMovementMethod.getInstance());
            holder.Caption.setText(commentsContent1);


            List_img = Array_imgadpter.get(position);
//            int[] mImageArray = {R.drawable.video_red_active, R.drawable.imgthree, R.drawable.imgthree,
//                    R.drawable.imgthree};
            PageAdapter mAdapter;
            //FragmentManager fm = ((Activity) contexts).getFragmentManager();
            FragmentManager fm = getFragmentManager();
            holder.mPager.setId(position + 1);
            mAdapter = new PageAdapter(fm);
             //holder.mPager.setAdapter(new EndLessAdapter(getActivity(), List_img));
            holder.mPager.setAdapter(mAdapter);
            holder.mPager.setOffscreenPageLimit(4);
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


                Picasso.with(contexts).load(Str_profile_pic).placeholder(R.drawable.profile_placeholder).resize(80,80).centerCrop().into(holder.Circular_imagview);
            } catch (java.lang.IllegalArgumentException e) {
                holder.Circular_imagview.setImageResource(R.drawable.profile_placeholder);
                e.printStackTrace();
            }
            holder.Txt_comment.setText("Comments " + imagess.get(position).get("Total_comments"));
            holder.TxvwLike.setText("Likes " + imagess.get(position).get("Total_likes"));
            holder.Circular_imagview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(contexts, "click", Toast.LENGTH_LONG).show();
                    Rlv_profile.setVisibility(View.VISIBLE);
                    SRL.setVisibility(View.GONE);
                    Txt_trnst.setVisibility(View.GONE);
                    Img_back.setVisibility(View.VISIBLE);
                    Str_UserId = imagess.get(position).get("user_id");
                    Scroll_position_otheruser = 0;
                    List_arrayHome.clear();
                    new Orher_user().execute();
                }
            });
            holder.Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Rlv_profile.setVisibility(View.VISIBLE);
                    SRL.setVisibility(View.GONE);
                    Txt_trnst.setVisibility(View.GONE);
                    Img_back.setVisibility(View.VISIBLE);
                    Str_UserId = imagess.get(position).get("user_id");
                    new Orher_user().execute();
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
                        // mp_Full.reset();
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
                        // mp_Full.reset();
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
                        // mp_Full.reset();
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    items = List_img.split(",");


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

            //  holder.mPager.setCurrentItem(0);
            holder.BtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    RlvDelete.setVisibility(View.VISIBLE);
//                    RlvDelete.bringToFront();
                    // Lv.

                    Str_chaeck_username = imagess.get(position).get("username");
                    Str_UserId = imagess.get(position).get("user_id");
                    StrPost_id = imagess.get(position).get("Post_id");
                    initiatePopupWindow();

                }
            });
            holder.mPager.setOnItemClickListener(new viewpager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    int id = holder.mPager.getId();
                    id = id - 1;
                    String postion = String.valueOf(id);
                    List_img = Array_imgadpter.get(id);
                    String str_id = imagess.get(id).get("Post_id");
                    String status_like = imagess.get(id).get("likestatus");
                    String caption = imagess.get(id).get("caption");
                    String Check_username = imagess.get(id).get("username");
                    String str_followfornextscreen;
                    if (Str_username.contentEquals(Check_username)) {
                         str_followfornextscreen = "GONE";
                    }else{
                         str_followfornextscreen = "VISIBLE";
                    }
                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("Check_follow", str_followfornextscreen);
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
                        new Login().execute();
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


        private class PageAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

            public PageAdapter(android.support.v4.app.FragmentManager fragmentManager) {
                super(fragmentManager);
            }

            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                //   PlaceholderFragment fragment = null;
                try {
                    fragment = new PlaceholderFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position);
                    //Fragment.instantiate(getActivity(), PlaceholderFragment.class.getName(), bundle);
                    fragment.setArguments(bundle);
                } catch (java.lang.RuntimeException e) {
                    e.printStackTrace();

                }

                return fragment;
            }


            @Override
            public int getCount() {
                return 4;
            }

        }

        public class PlaceholderFragment extends android.support.v4.app.Fragment {
            @SuppressLint("ValidFragment")
            int positionn;

            public PlaceholderFragment() {
                super();
            }

            public PlaceholderFragment newInstance(int positionn) {

                fragment = new PlaceholderFragment();
                final Bundle bundle = new Bundle();
                bundle.putInt(PlaceholderFragment.EXTRA_POSITION, positionn);
                fragment.setArguments(bundle);
                //PlaceholderFragment fragment = new PlaceholderFragment();

                return fragment;
            }

            public static final String EXTRA_POSITION = "EXTRA_POSITION";
            //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};
            TextureVideoView video_Vw;


            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                final int position = getArguments().getInt(EXTRA_POSITION);
                //final int position = 1;
                View view = inflater.inflate(R.layout.frame_videoview, container, false); //Contains empty RelativeLayout

                final ImageView textViewPosition = (ImageView) view.findViewById(R.id.Imag_upload_view);
                video_Vw = (TextureVideoView) view.findViewById(R.id.frame_video_view);

                final ImageView placehoderforvideo = (ImageView) view.findViewById(R.id.imageView46);

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
                    placehoderforvideo.setVisibility(View.VISIBLE);

                    Uri video = Uri.parse(url);

                    video_Vw.setVideoURI(video);
                    //   video_Vw.
                    // video_Vw.
                    video_Vw.setMediaController(null);

                    video_Vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(final MediaPlayer mp) {
//                            startVideoPlayback();
//                            startVideoAnimation();
                            //  mp_Full = mp;
                            mp.setVolume(0f, 0f);
                            //  mp.prepareAsync();

                            mp.start();
                            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                    switch (what) {
                                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                                            //   mProgressBar.setVisibility(View.GONE);
                                            return true;
                                        }
                                        case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                            //video_Vw.setBackgroundResource(R.drawable.placeholderdevzillad);
                                            // video_Vw.setBackgroundResource(R.drawable.placeholderdevzillad);
                                            video_Vw.setVisibility(View.GONE);
                                            placehoderforvideo.setVisibility(View.VISIBLE);
                                            //  mProgressBar.setVisibility(View.VISIBLE);
                                            return true;
                                        }
                                        case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                            video_Vw.setVisibility(View.VISIBLE);
                                            placehoderforvideo.setVisibility(View.GONE);
                                            // video_Vw.setBackgroundResource(null);
                                            //  mProgressBar.setVisibility(View.VISIBLE);
                                            return true;
                                        }
                                    }
                                    //  video_Vw.setBackground(null);
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
                    if (video_Vw.isPlaying()) {
                        video_Vw.stopPlayback();
                    }
                    video_Vw.setVisibility(View.GONE);
                    textViewPosition.setVisibility(View.VISIBLE);
                    try {

                         Picasso.with(contexts).load(items[position]).placeholder(R.drawable.placeholderdevzillad).resize(400, 400).centerCrop().into(textViewPosition);
                        //Picasso.with(getContext()).load(items[position]).placeholder(R.drawable.placeholderdevzillad).skipMemoryCache().into(textViewPosition);
                        //
                        //img_loader.DisplayImage(url, textViewPosition);


                    } catch (OutOfMemoryError error) {

                    }
                }


                return view;
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
            Grid_view_adapter_profile_local adapter = new Grid_view_adapter_profile_local(getActivity(),
                    android.R.layout.simple_list_item_1, List_arrayHome, Array_img,
                    getActivity().getApplication());
            gridView.setAdapter(adapter);
            gridView.setSelection(Scroll_position_otheruser);
            txt_name.setText(Str_namehome);
            //Txt_Username.setText(user_name);
            Txt_Username.setText(Str_usernameHome);
            Txt_post.setText(Str_postcount);
            Txt_follower.setText(StrFollowerHome);
            Txvw_following.setText(StrFollowingHome);
            if (Str_usernameHome.contentEquals(Str_username)) {
                Rlv_follow_other.setVisibility(View.GONE);
                //Rlv_follow_other.setVisibility(View.VISIBLE);
            } else {
                Rlv_follow_other.setVisibility(View.VISIBLE);
                //Divider.setVisibility(View.GONE);
            }
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
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
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
            ImageView Divider2 = (ImageView) layout.findViewById(R.id.imageView39);
            TextView Delete = (TextView) layout.findViewById(R.id.Txt_delete);
            TextView Report = (TextView) layout.findViewById(R.id.textView40);
            if (Str_chaeck_username.contentEquals(Str_username)) {
                Delete.setVisibility(View.VISIBLE);
                Divider.setVisibility(View.VISIBLE);
                Report.setVisibility(View.GONE);
                Divider2.setVisibility(View.GONE);
            } else {
                Delete.setVisibility(View.GONE);
                Divider.setVisibility(View.GONE);
                Report.setVisibility(View.VISIBLE);
                Divider2.setVisibility(View.VISIBLE);
            }
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("")
                            .setMessage("Do you really want to Delete?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    new Post_delete().execute();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });
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

    private class Post_delete extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {

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
            String User_postid = Str_UserId + "," + StrPost_id;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("userIds", User_postid));
            //nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            Str_Response = sh.makeServiceCall_withHeader(Constant.Post_Delete,
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
            //Toast.makeText(getActivity(), Str_Response, Toast.LENGTH_LONG).show();
            pDialog.dismiss();
            pwindo.dismiss();

            new Login().execute();
            // Lv.scrollToPosition(5);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

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
            Txt_trnst.setVisibility(View.GONE);
            Img_back.setVisibility(View.VISIBLE);
            Txthash.setVisibility(View.VISIBLE);
            Txthash.setText("#" + Str_HashWord);
            CheckHash = "hash";
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
            // you can start another activity here
            Rlv_profile.setVisibility(View.VISIBLE);
            SRL.setVisibility(View.GONE);
            Txt_trnst.setVisibility(View.GONE);
            Img_back.setVisibility(View.VISIBLE);
            Str_UserId = "@" + theWord;
            new Orher_user().execute();
            // Toast.makeText(context, String.format("@"+theWord, "@"+theWord), Toast.LENGTH_LONG).show();
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

           // String Response = makeServiceCall("http://stage.turnstr.net/api/tag/" + Str_HashWord + "?page=0");
            String Response = makeServiceCall("http://turnstr.net/api/tag/" + Str_HashWord + "?page=0");

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

            MoviesAdapterr mAdapter = new MoviesAdapterr(data, Array_img);
            Lv.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            Lv.setLayoutManager(mLayoutManager);

            Lv.setItemAnimator(new DefaultItemAnimator());
            Lv.setAdapter(mAdapter);
            SRL.setRefreshing(false);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public String makeServiceCallwithoutheader(String url) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;


            HttpGet httpGet = new HttpGet(url);


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

    //    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener() {
//
//        @Override
//        public void onPrepared(MediaPlayer m) {
//
//            try {
//                if (m.isPlaying()) {
//                    m.pause();
//                    m.release();
//                    m = new MediaPlayer();
//                }
//                m.setVolume(0f, 0f);
//                m.setLooping(true);
//                m.start();
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//        }
//    };
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
//// Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
//            lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            lObjIntent.putExtra("images", List_img);
//            lObjIntent.putExtra("Post_id", str_id);
//            lObjIntent.putExtra("status_like", status_like);
//            lObjIntent.putExtra("Check_follow", str_followfornextscreen);
//            lObjIntent.putExtra("caption", caption);

            vh.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Profile_images.put("allurl", grop_url);
                    String str_id = images.get(position).get("Post_id");
                    String status_like = images.get(position).get("Liked");
                    String caption = images.get(position).get("caption");
                    List_img = images.get(position).get("allurl");
                    String str_followfornextscreen;
                   if (Str_usernameHome.contentEquals(Str_username)) {
                        str_followfornextscreen = "GONE";
                    }else{
                        str_followfornextscreen = "VISIBLE";
                    }
                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("Check_follow", str_followfornextscreen);
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
            nameValuePairs.add(new BasicNameValuePair("following_id", Str_idhome));
            nameValuePairs.add(new BasicNameValuePair("following_status", Check_follow));


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
            List_arrayHome.clear();
            new Orher_user().execute();
            // Dismiss the progress dialog
            //  new Follow_unfollow().execute();
            //  new Explore_follow().execute();

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
