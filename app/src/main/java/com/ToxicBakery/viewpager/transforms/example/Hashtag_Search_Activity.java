package com.ToxicBakery.viewpager.transforms.example;

import android.app.FragmentManager;
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
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.sprylab.android.widget.TextureVideoView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Session_handler.Session_manager;
import circular_imagview.CircularImageView;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;
import view_pager.viewpager;


public class Hashtag_Search_Activity extends Activity {
    RecyclerView Lv_explorer;
    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_img = new ArrayList<String>();
    ArrayList<String> Array_single_image = new ArrayList<String>();
    Session_manager session;
    String Str_HashWord,accestoken,device_id;
    Button Btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hashtag__search_);
        Lv_explorer = (RecyclerView) findViewById(R.id.listView2);
        Btn_back = (Button) findViewById(R.id.button10);
        Intent intent = getIntent();
        Str_HashWord = intent.getStringExtra("User_detail");

        session = new Session_manager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);


        device_id = user.get(session.device_id);
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new hashSearch().execute();
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


            android_id = Settings.Secure.getString(Hashtag_Search_Activity.this.getContentResolver(),
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

            MoviesAdapterr mAdapter = new MoviesAdapterr(data, Array_img);
            Lv_explorer.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            Lv_explorer.setLayoutManager(mLayoutManager);

            Lv_explorer.setItemAnimator(new DefaultItemAnimator());
            Lv_explorer.setAdapter(mAdapter);


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

           //body watching,#Love like you'll never be #hurt,#Sing like there's @nobody listening,And live like it's #heaven on #earth.";
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
//                    Rlv_profile.setVisibility(View.VISIBLE);
//
//                    Lv_explorer.setVisibility(View.GONE);
//                    Str_CheckProfile = "isShowing";
//                    Str_UserId = imagess.get(position).get("user_id");
//                    new Orher_user().execute();
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


                    video_Vw.setVisibility(View.VISIBLE);
                    textViewPosition.setVisibility(View.GONE);

                    Uri video = Uri.parse(url);

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

            commentsContent.setSpan(new Hashtagg(Hashtag_Search_Activity.this),
                    hashTagStart,
                    hashTagEnd, 0);

        }


    }

    private void setSpanUname(SpannableString commentsUname, ArrayList<int[]> calloutSpans) {
        for (int i = 0; i < calloutSpans.size(); i++) {
            int[] span = calloutSpans.get(i);
            int calloutStart = span[0];
            int calloutEnd = span[1];
            commentsUname.setSpan(new CalloutLinkk(Hashtag_Search_Activity.this),
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

            // you can start another activity here
            // Toast.makeText(context, String.format("Tagg : %s", theWord), Toast.LENGTH_LONG).show();
           //
            // new hashSearch().execute();
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


            // Toast.makeText(context, String.format("@"+theWord, "@"+theWord), Toast.LENGTH_LONG).show();
        }
    }

}
