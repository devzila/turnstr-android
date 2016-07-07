package com.ToxicBakery.viewpager.transforms.example;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.example.R;

import adapter.FragmentInterface;
import adapter.MyTabFactory;
import adapter.TabsPagerAdapter;

public class Camera_activity extends FragmentActivity implements OnTabChangeListener, OnPageChangeListener {

    private TabsPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageView img1, img2, img3;
    Button b1;
    RelativeLayout Rlv_Phto, Rlv_Camera, Rlv_Video;
    TextView Txt_Phto, Txt_camera, Txt_video;
    ImageView img_close;
    SharedPreferences sharedpreferences;
    int progressStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera_activity);
        sharedpreferences = getSharedPreferences("turnstrr", MODE_PRIVATE);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        img1 = (ImageView) findViewById(R.id.imageView7);
        img2 = (ImageView) findViewById(R.id.imageView13);
        img3 = (ImageView) findViewById(R.id.imageView14);
        Rlv_Phto = (RelativeLayout) findViewById(R.id.photo);
        Rlv_Camera = (RelativeLayout) findViewById(R.id.camera);
        Rlv_Video = (RelativeLayout) findViewById(R.id.video);
        Txt_Phto = (TextView) findViewById(R.id.textView21);
        Txt_camera = (TextView) findViewById(R.id.textView22);
        Txt_video = (TextView) findViewById(R.id.textView23);

        // img_close=(ImageView)findViewById(R.id.imageView15);
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        // b1=(Button)findViewById(R.id.button6);
        // Tab Initialization
        initialiseTabHost();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        // Fragments and ViewPager Initialization


        mViewPager.setAdapter(mAdapter);
        img1.setVisibility(View.VISIBLE);
        img2.setVisibility(View.INVISIBLE);
        img3.setVisibility(View.INVISIBLE);
        Txt_Phto.setTextColor(getResources().getColor(R.color.Loginbg));
        Txt_camera.setTextColor(getResources().getColor(R.color.tabWhite));
        Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
        mViewPager.setCurrentItem(0);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("Photo", "photto");

        editor.commit();
        mViewPager.setOnPageChangeListener(Camera_activity.this);
        Rlv_Phto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.INVISIBLE);
                Txt_Phto.setTextColor(getResources().getColor(R.color.Loginbg));
                Txt_camera.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
                mViewPager.setCurrentItem(0);

            }
        });
        Rlv_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);
                img3.setVisibility(View.INVISIBLE);
                Txt_Phto.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_camera.setTextColor(getResources().getColor(R.color.Loginbg));
                Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("Photo", "photto_camera");

                editor.commit();
                mViewPager.setCurrentItem(0);
                mViewPager.setCurrentItem(1);
            }
        });
        Rlv_Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.VISIBLE);
                Txt_Phto.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_camera.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_video.setTextColor(getResources().getColor(R.color.Loginbg));

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("Photo", "video");

                editor.commit();
                mViewPager.setCurrentItem(2);
                mViewPager.setCurrentItem(1);
            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mViewPager.setOffscreenPageLimit(3);
    }

    // Method to add a TabHost
    private static void AddTab(Camera_activity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        //String position = String.valueOf(pos);
        //Toast.makeText(Camera_activity.this,position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
        if (pos == 0) {
//            img1.setVisibility(View.VISIBLE);
//            img2.setVisibility(View.INVISIBLE);
//            img3.setVisibility(View.INVISIBLE);
//            Txt_Phto.setTextColor(getResources().getColor(R.color.Loginbg));
//            Txt_camera.setTextColor(getResources().getColor(R.color.tabWhite));
//            Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
            // mAdapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Photo", "photto");

            editor.commit();

        } else if (pos == 1) {
//            img1.setVisibility(View.INVISIBLE);
//            img2.setVisibility(View.VISIBLE);
//            img3.setVisibility(View.INVISIBLE);
//            Txt_Phto.setTextColor(getResources().getColor(R.color.tabWhite));
//            Txt_camera.setTextColor(getResources().getColor(R.color.Loginbg));
//            Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
//            mAdapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Photo", "photto_camera");

            editor.commit();
        } else if (pos == 2) {
//            img1.setVisibility(View.INVISIBLE);
//            img2.setVisibility(View.INVISIBLE);
//            img3.setVisibility(View.VISIBLE);
//            Txt_Phto.setTextColor(getResources().getColor(R.color.tabWhite));
//            Txt_camera.setTextColor(getResources().getColor(R.color.tabWhite));
//            Txt_video.setTextColor(getResources().getColor(R.color.Loginbg));
//           // this.mTabHost.setCurrentTab(1);
//            mAdapter.notifyDataSetChanged();
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Photo", "video");

            editor.commit();
        }

    }

    @Override
    public void onPageSelected(int arg0) {
        if (arg0 == 2) {
            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.VISIBLE);
            Txt_Phto.setTextColor(getResources().getColor(R.color.tabWhite));
            Txt_camera.setTextColor(getResources().getColor(R.color.tabWhite));
            Txt_video.setTextColor(getResources().getColor(R.color.Loginbg));
        }
        FragmentInterface fragment = (FragmentInterface) mAdapter.instantiateItem(mViewPager, arg0);
        if (fragment != null) {
            fragment.fragmentBecameVisible();
        }
    }


    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs


        Camera_activity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("ButtonTab").setIndicator("tabIndicator"));
        Camera_activity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("ImageTab").setIndicator("ImageTab"));
        Camera_activity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("TextTab").setIndicator("TextTab"));

        mTabHost.setOnTabChangedListener(this);
    }
}