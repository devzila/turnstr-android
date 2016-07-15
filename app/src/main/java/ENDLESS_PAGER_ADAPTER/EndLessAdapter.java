package ENDLESS_PAGER_ADAPTER;

/**
 * Created by Nirmal on 7/7/2016.
 */

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ToxicBakery.viewpager.transforms.example.R;
import com.sprylab.android.widget.TextureVideoView;

import it.sephiroth.android.library.picasso.Picasso;

public class EndLessAdapter extends PagerAdapter {

    FragmentActivity activity;
    //int imageArray[];
    String imgArray;
    String image_array[];
    String[] items;

    public EndLessAdapter(FragmentActivity act, String Image_array) {
        //imageArray = imgArra;
        imgArray = Image_array;
        items = imgArray.split(",");

        activity = act;
    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    private int pos = 0;

    public Object instantiateItem(View collection, int position) {

        ImageView mwebView = new ImageView(activity);
        ((ViewPager) collection).addView(mwebView, 0);
        mwebView.setScaleType(ScaleType.FIT_XY);
        TextureVideoView videoview = new TextureVideoView(activity);
        ((ViewPager) collection).addView(videoview, 0);
        //videoview.set
        String url = items[pos];
        if (url.endsWith(".mp4")) {
            videoview.setVisibility(View.VISIBLE);
            mwebView.setVisibility(View.GONE);
           // placehoderforvideo.setVisibility(View.VISIBLE);

            Uri video = Uri.parse(url);

            videoview.setVideoURI(video);
            //   video_Vw.
            // video_Vw.
            videoview.setMediaController(null);

            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
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
                                    videoview.setVisibility(View.GONE);
                                   // placehoderforvideo.setVisibility(View.VISIBLE);
                                    //  mProgressBar.setVisibility(View.VISIBLE);
                                    return true;
                                }
                                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                    videoview.setVisibility(View.VISIBLE);
                                  //  placehoderforvideo.setVisibility(View.GONE);
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
            videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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
            if (videoview.isPlaying()) {
                videoview.stopPlayback();
            }
            videoview.setVisibility(View.GONE);
            mwebView.setVisibility(View.VISIBLE);
            try {

                Picasso.with(activity).load(items[pos]).placeholder(R.drawable.placeholderdevzillad).resize(400, 400).centerCrop().into(mwebView);
                //  mwebView.setImageResource(imageArray[pos]);
                //Picasso.with(getContext()).load(items[position]).placeholder(R.drawable.placeholderdevzillad).skipMemoryCache().into(textViewPosition);
                //
                //img_loader.DisplayImage(url, textViewPosition);


            } catch (OutOfMemoryError error) {

            }
        }





//        if (pos >= items.length - 1) {
//            pos = 0;
//        }else {
//            ++pos;
//        }

        if (pos >= items.length - 1) {
            pos = 0;
        }else {
            ++pos;
        }

        return null;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}