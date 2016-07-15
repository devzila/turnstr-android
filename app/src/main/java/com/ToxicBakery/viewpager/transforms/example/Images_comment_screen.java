package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.sprylab.android.widget.TextureVideoView;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hashTag.CalloutLink;
import hashTag.Hashtag;
import it.sephiroth.android.library.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import Square_imageview.ProportionalImageView;
import adapter.Comment_adapter;
import lazyloading.ImageLoader;
import view_pager.viewpager;

public class Images_comment_screen extends Activity {
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    private PageAdapter mAdapter;
    private viewpager mPager;
    String images, Post_id;
    Session_manager session;
    String accestoken;
    String device_id;
    ImageView img_comment;
    Comment_adapter adapter;
    ImageView Img_Cross;
    RelativeLayout rlv;
    EmojiconEditText edt_post_comment;
    ArrayList<HashMap<String, String>> Comment_array = new ArrayList<HashMap<String, String>>();
    String Str_comment,userid;
    ListView lv_comment;
    ImageView img_like, Back_img, Img_share;
    String like_status = "0";
    String user_id, Str_caption,Follow_Status;
    TextView txt_caption;
    String[] items;
    Button btn_back;
    String url;
    TextView Txt_follow_unfollow;
    RelativeLayout rlv_follow_unfollow;
    String Share_url,Check_followStatus;
//Rlveditprofile
    //ImageView img_cross;
    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_images_comment_screen);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        rlv = (RelativeLayout) findViewById(R.id.relativeLayout18);
        rlv_follow_unfollow= (RelativeLayout) findViewById(R.id.relativeLayout38);
        Txt_follow_unfollow = (TextView)findViewById(R.id.textView55);
        edt_post_comment = (EmojiconEditText) findViewById(R.id.editText8);
        img_like = (ImageView) findViewById(R.id.imageView8);
        Img_Cross = (ImageView) findViewById(R.id.imageView34);
        Img_share = (ImageView) findViewById(R.id.imageView10);
        Back_img = (ImageView) findViewById(R.id.back_img);
        btn_back = (Button) findViewById(R.id.button2);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_view_header, lv_comment,
                false);

        edt_post_comment.setUseSystemDefault(true);
        img_comment = (ImageView) findViewById(R.id.imageView9);
        Intent intent = getIntent();
        images = intent.getStringExtra("images");
        Post_id = intent.getStringExtra("Post_id");
        Str_caption = intent.getStringExtra("caption");
        Check_followStatus =intent.getStringExtra("Check_follow");
        rlv_follow_unfollow.setVisibility(View.GONE);


        // like_status = intent.getStringExtra("status_like");

        lv_comment.addHeaderView(header);
        mPager = (viewpager) header.findViewById(R.id.containerr);
        txt_caption = (TextView) header.findViewById(R.id.textView10);
        session = new Session_manager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // String str = "@Kirank ,#nirmal You've gotta #dance like there's nobody watching,#Love like you'll never be #hurt,#Sing like there's @nobody listening,And live like it's #heaven on #earth.";
        //String str  =  "the text view ";


        ArrayList<int[]> hashtagSpans1 = getSpans(Str_caption, '#');
        ArrayList<int[]> calloutSpans1 = getSpans(Str_caption, '@');

        SpannableString commentsContent1 =
                new SpannableString(Str_caption);

        setSpanComment(commentsContent1, hashtagSpans1);
        setSpanUname(commentsContent1, calloutSpans1);

        txt_caption.setMovementMethod(LinkMovementMethod.getInstance());
        txt_caption.setText(commentsContent1);
        // img_profile.setImageResource(R.drawable.profile_placeholder);
        // access_token
        //txt_caption.setText(Str_caption);
        accestoken = user.get(session.Acess_Token);


        device_id = user.get(session.device_id);
        user_id = user.get(session.User_id);
        new Comment_list().execute();
        try {
            mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        items = images.split(",");
        url = items[0];
        mAdapter = new PageAdapter(getFragmentManager());

        mPager.setAdapter(mAdapter);

        mPager.setOnItemClickListener(new viewpager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i1 = new Intent(Images_comment_screen.this,
                        Full_view.class);
                i1.putExtra("images", images);

                startActivity(i1);
            }
        });
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // url = items[position];
            }

            @Override
            public void onPageSelected(int position) {
               // url = items[position];
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               // url = items[state];
            }
        });
        Back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Images_comment_screen.this,
                        MainActivity.class);

                startActivity(i1);
                Images_comment_screen.this.finish();
            }
        });
        Img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Share_url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SlideToAbove();
                rlv.setVisibility(View.VISIBLE);
                edt_post_comment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Images_comment_screen.this.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        Img_Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_post_comment.setText("");
                rlv.setVisibility(View.GONE);
            }
        });
        rlv_follow_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Follow_Status.contentEquals("0")) {
                    Txt_follow_unfollow.setText("Unfollow");
                    Follow_Status = "1";
                    new Follow_unfollow().execute();

                } else if (Follow_Status.contentEquals("1")) {
                    Txt_follow_unfollow.setText("Follow");
                    Follow_Status = "0";
                    new Follow_unfollow().execute();

                }
            }
        });
        edt_post_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (edt_post_comment.getText().toString().contentEquals("")) {
                            Animation anm = Shake_Animation();
                            edt_post_comment.startAnimation(anm);
                        }else {
                            Str_comment = edt_post_comment.getText().toString();
                            //String paramString = URLEncodedUtils.format(params, "UTF-8");
                            Str_comment = StringEscapeUtils.escapeJava(Str_comment);
                            //  Str_comment=encodeMessage(Str_comment);
                            // Str_comment=URLEncodedUtils.format(Str_comment, "UTF-8");
                            new Post_comment().execute();
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
        // edt_post_comment.seto
        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like_status.contentEquals("0")) {
                    img_like.setImageResource(R.drawable.like);
                    like_status = "1";
                    new Post_Like().execute();
                } else if (like_status.contentEquals("1")) {
                    img_like.setImageResource(R.drawable.like_grey);
                    like_status = "0";
                    new Post_Like().execute();
                }
            }
        });
    }
    private String encodeMessage(String message) {
        message = message.replaceAll("&", ":and:");
        message = message.replaceAll("\\+", ":plus:");
        return StringEscapeUtils.escapeJava(message);
    }

    private String decodeMessage(String message) {
        message = message.replaceAll(":and:", "&");
        message = message.replaceAll(":plus:", "+");
        return StringEscapeUtils.unescapeJava(message);
    }
    public class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        //  private final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            // final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);
            //final ProportionalImageView textViewPosition = (ProportionalImageView) inflater.inflate(R.layout.square_imageviewlv, container, false);

            //String path_new = items[position];

            View view = inflater.inflate(R.layout.frame_videoview, container, false); //Contains empty RelativeLayout

            final ImageView textViewPosition = (ImageView) view.findViewById(R.id.Imag_upload_view);
            TextureVideoView video_Vw = (TextureVideoView) view.findViewById(R.id.frame_video_view);
            final ImageView placehoderforvideo = (ImageView) view.findViewById(R.id.imageView46);
            //final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);
            video_Vw.setId(position);
            int pstn_new = video_Vw.getId();
            items = images.split(",");
            url = items[pstn_new];

            // if (video_Vw.isPlaying()) {
            // video_Vw.stopPlayback();
            //  }
            if (url.endsWith(".mp4")) {

//                    MediaController media_Controller;
//                    DisplayMetrics dm;
                video_Vw.setVisibility(View.VISIBLE);
                textViewPosition.setVisibility(View.GONE);
                placehoderforvideo.setVisibility(View.VISIBLE);
//                    media_Controller = new MediaController(getActivity());
//                    dm = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//                    int height = dm.heightPixels;
//                    int width = dm.widthPixels;
//                    video_Vw.setMinimumWidth(width);
//                    video_Vw.setMinimumHeight(height);
//                    video_Vw.setMediaController(null);
                // video_Vw.setVolume(0f, 0f);
                Uri video = Uri.parse(url);
                //Uri video = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
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

                    Picasso.with(Images_comment_screen.this).load(items[position]).placeholder(R.drawable.placeholderdevzillad).into(textViewPosition);
                    //
                    // img_loader.DisplayImage(url, textViewPosition);


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

    private static final class TransformerItem {

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

    private final class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
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

    public void SlideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        mPager.startAnimation(slide);

        slide.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mPager.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        mPager.getWidth(), mPager.getHeight());
                // lp.setMargins(0, 0, 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                mPager.setLayoutParams(lp);

            }

        });

    }

    private class Comment_list extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {

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
            nameValuePairs.add(new BasicNameValuePair("post_id", Post_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall_withHeader(Constant.Comments_List,
                    ServiceHandler.POST, nameValuePairs, accestoken, device_id);

        //    Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    JSONObject jsonobject_like = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // like_status=jsonObj.getString("like");
                    // Getting JSON Array node
                    JSONArray Array_image = null;

                    jsonobject_like = jsonObj.getJSONObject("data");
                    Array_image = jsonobject_like.getJSONArray("comments");
                    like_status = jsonobject_like.getString("is_liked");
                    Follow_Status = jsonobject_like.getString("is_Following");
                    Share_url = jsonobject_like.getString("postUrl");
                    userid = jsonobject_like.getString("postUserID");
                    for (int i = 0; i < Array_image.length(); i++) {
                        JSONObject c = Array_image.getJSONObject(i);

                        String id = c.getString("id");
                        String user_id = c.getString("user_id");
                        String post_id = c.getString("post_id");
                        String comments = c.getString("comments");
                        String created_at = c.getString("createdTime");
                        String updated_at = c.getString("updated_at");
                        String Username = c.getString("username");
                        // like_status=c.getString("like");
                        String profile_image = c.getString("profile_image");


                        // tmp hashmap for single contact
                        HashMap<String, String> comment = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        comment.put("id", id);
                        comment.put("user_id", user_id);
                        comment.put("post_id", post_id);
                        comment.put("comments", comments);
                        comment.put("created_at", created_at);
                        comment.put("updated_at", updated_at);
                        comment.put("username", Username);
                        comment.put("profile_image", profile_image);


                        // adding contact to contact list
                        Comment_array.add(comment);
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

            if (like_status.contentEquals("1")) {
                img_like.setImageResource(R.drawable.like);
            } else if (like_status.contentEquals("0")) {
                img_like.setImageResource(R.drawable.like_grey);
            } else {
                img_like.setImageResource(R.drawable.like_grey);
            }
            adapter = new Comment_adapter(Images_comment_screen.this,
                    android.R.layout.simple_list_item_1, Comment_array,
                    getApplication());
            if(Follow_Status.contentEquals("0")){
                Txt_follow_unfollow.setText("Follow");
            }else{
                Txt_follow_unfollow.setText("Unfollow");
            }
            lv_comment.setAdapter(adapter);
            if(Check_followStatus.contentEquals("VISIBLE")){
                rlv_follow_unfollow.setVisibility(View.VISIBLE);
            }else {
                rlv_follow_unfollow.setVisibility(View.GONE);
            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hiddenInputMethod();
    }

    public void hiddenInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Images_comment_screen.this.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class Post_comment extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            // publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("post_id", Post_id));
           nameValuePairs.add(new BasicNameValuePair("comments", Str_comment));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall_withHeader(Constant.Post_comments,
                    ServiceHandler.POST, nameValuePairs, accestoken, device_id);
            // String jsonStr=makeServiceCall(Constants.Post_comments,nameValuePairs);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                // try {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting JSON Array node
                JSONArray Array_image = null;
//                    Array_image = jsonObj.getJSONArray("data");
//                    for (int i = 0; i < Array_image.length(); i++) {
//                        JSONObject c = Array_image.getJSONObject(i);
//
//                        String id = c.getString("id");
//                        String user_id = c.getString("user_id");
//                        String post_id = c.getString("post_id");
//                        String comments = c.getString("comments");
//                        String created_at = c.getString("created_at");
//                        String updated_at = c.getString("updated_at");
//                        String Username = c.getString("username");
//                        String profile_image = c.getString("profile_image");
//
//
//
//
//
//
//                        // tmp hashmap for single contact
//                        HashMap<String, String> comment = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        comment.put("id", id);
//                        comment.put("user_id", user_id);
//                        comment.put("post_id", post_id);
//                        comment.put("comments", comments);
//                        comment.put("created_at", created_at);
//                        comment.put("updated_at", updated_at);
//                        comment.put("username", Username);
//                        comment.put("profile_image", profile_image);
//
//
//                        // adding contact to contact list
//                        Comment_array.add(comment);
                //}

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            Comment_array.clear();
            rlv.setVisibility(View.GONE);
            edt_post_comment.setText("");
            new Comment_list().execute();


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public String makeServiceCall(String url, List<NameValuePair> params) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;


            HttpPost http_post = new HttpPost(url);
            http_post.addHeader("X-TOKEN", accestoken);
            http_post.addHeader("X-DEVICE", device_id);
            if (params != null) {
                http_post.setEntity(new UrlEncodedFormEntity(params));
            }

            httpResponse = httpClient.execute(http_post);


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

    private class Post_Like extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {

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
            nameValuePairs.add(new BasicNameValuePair("post_id", Post_id));
            nameValuePairs.add(new BasicNameValuePair("like_status", like_status));
            nameValuePairs.add(new BasicNameValuePair("post_user", user_id));


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall_withHeader(Constant.LikePost,
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

                // Getting JSON Array node
                JSONArray Array_image = null;
//                    Array_image = jsonObj.getJSONArray("data");
//                    for (int i = 0; i < Array_image.length(); i++) {
//                        JSONObject c = Array_image.getJSONObject(i);
//
//                        String id = c.getString("id");
//                        String user_id = c.getString("user_id");
//                        String post_id = c.getString("post_id");
//                        String comments = c.getString("comments");
//                        String created_at = c.getString("created_at");
//                        String updated_at = c.getString("updated_at");
//                        String Username = c.getString("username");
//                        String profile_image = c.getString("profile_image");
//
//
//
//
//
//
//                        // tmp hashmap for single contact
//                        HashMap<String, String> comment = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        comment.put("id", id);
//                        comment.put("user_id", user_id);
//                        comment.put("post_id", post_id);
//                        comment.put("comments", comments);
//                        comment.put("created_at", created_at);
//                        comment.put("updated_at", updated_at);
//                        comment.put("username", Username);
//                        comment.put("profile_image", profile_image);
//
//
//                        // adding contact to contact list
//                        Comment_array.add(comment);
                //}

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog


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

            commentsContent.setSpan(new Hashtag(Images_comment_screen.this),
                    hashTagStart,
                    hashTagEnd, 0);

        }


    }

    private void setSpanUname(SpannableString commentsUname, ArrayList<int[]> calloutSpans) {
        for (int i = 0; i < calloutSpans.size(); i++) {
            int[] span = calloutSpans.get(i);
            int calloutStart = span[0];
            int calloutEnd = span[1];
            commentsUname.setSpan(new CalloutLink(Images_comment_screen.this),
                    calloutStart,
                    calloutEnd, 0);

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
            nameValuePairs.add(new BasicNameValuePair("following_id", userid));
            nameValuePairs.add(new BasicNameValuePair("following_status", Follow_Status));


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
            //  new Explore_follow().execute();

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }
}
