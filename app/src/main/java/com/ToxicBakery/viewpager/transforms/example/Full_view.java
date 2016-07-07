package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.sprylab.android.widget.TextureVideoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import GalleryWidget.BasePagerAdapter;
import GalleryWidget.GalleryViewPager;
import GalleryWidget.UrlPagerAdapter;
import Square_imageview.ProportionalImageView;
import Zoom_Effect.ImageViewTouch;
import Zoom_Effect.ImageViewTouchBase;


public class Full_view extends Activity {
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    private PageAdapter mAdapter;
    private ViewPager mPager;
    String images;
    String Strposition;
    ProportionalImageView textViewPosition;
    private ScaleGestureDetector scaleGestureDetector;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    Button Btn_back;
    String url;

    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_view);
        Btn_back = (Button) findViewById(R.id.button5);
        Intent intent = getIntent();
        images = intent.getStringExtra("images");
        mPager = (ViewPager) findViewById(R.id.containerr);
        String[] urls = images.split(",");
//        String[] urls = {
//                "http://cs407831.userapi.com/v407831207/18f6/jBaVZFDhXRA.jpg",
//                "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg",
//                "http://cs407831.userapi.com/v407831207/1906/oxoP6URjFtA.jpg",
//                "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
//                "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
//                "http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg"
//        };
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {

            }
        });
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  mPager.setOffscreenPageLimit(6);
        // mPager.setAdapter(pagerAdapter);
        try {
            mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//
        mAdapter = new PageAdapter(getFragmentManager());
//
        mPager.setAdapter(mAdapter);
    }

    public class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        // private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
//            ImageViewTouch imageView = (ImageViewTouch) inflater.inflate(R.layout.view_pager_page, container, false);
            String[] items = images.split(",");

//            // Picasso.with(Images_comment_screen.this).load(items[position]).placeholder(R.drawable.placeholder).into(textViewPosition);
//           // Picasso.with(Full_view.this).load(items[position]).placeholder(R.drawable.placeholderdevzillad).into(imageView);
//            imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            //textViewPosition.setBackgroundResource(COLORS[position - 1]);
            View view = inflater.inflate(R.layout.frame_videoview, container, false); //Contains empty RelativeLayout

            final ImageView textViewPosition = (ImageView) view.findViewById(R.id.Imag_upload_view);
            final ImageView Place_holderr = (ImageView) view.findViewById(R.id.imageView46);
            TextureVideoView video_Vw = (TextureVideoView) view.findViewById(R.id.frame_video_view);
            final ImageView placehoderforvideo = (ImageView) view.findViewById(R.id.imageView46);
            //final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);
            video_Vw.setId(position);
            int pstn_new = video_Vw.getId();
            // items = images.split(",");
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

                    it.sephiroth.android.library.picasso.Picasso.with(Full_view.this).load(items[position]).placeholder(R.drawable.placeholderdevzillad).into(textViewPosition);
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
        //return imageView;


    }

    //    public class PlaceholderFragment extends Fragment {
//
//        private static final String EXTRA_POSITION = "EXTRA_POSITION";
//        private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            final int position = getArguments().getInt(EXTRA_POSITION);
//            ImageViewTouch  imageView = (ImageViewTouch) inflater.inflate(R.layout.view_pager_page, container, false);
//            String[] items = images.split(",");
//
//
//
//            Picasso.with(getApplicationContext())
//                    .load(items[position])
//                    .into((target));
//            Strposition = String.valueOf(position);
//            String fileName = "Image-"+ Strposition +".jpg";
//            String completePath = Environment.getExternalStorageDirectory() + "/Turnstr/" + fileName;
//
//            File file = new File(completePath);
//            Uri imageUri = Uri.fromFile(file);
//            imageView.setImage(ImageSource.uri(imageUri));
//            // Picasso.with(Images_comment_screen.this).load(items[position]).placeholder(R.drawable.placeholder).into(textViewPosition);
//            //Picasso.with(Full_view.this).load(items[position]).placeholder(R.drawable.placeholderdevzillad).into(imageView);
//            //textViewPosition.setBackgroundResource(COLORS[position - 1]);
//
//            return imageView;
//        }
//
//    }
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {

//                    File file = new File(
//                            Environment.getExternalStorageDirectory().getPath()
//                                    + "/saved.jpg");
                    File myDir = new File("/sdcard/Turnstr");
                    myDir.mkdirs();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + Strposition + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists()) file.delete();
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

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

}
