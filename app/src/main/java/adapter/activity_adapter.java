package adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.Other_user_profile;
import com.ToxicBakery.viewpager.transforms.example.R;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Square_imageview.ProportionalImageView;
import Square_imageview.square_viewpager;
import circular_imagview.CircularImageView;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;
import view_pager.viewpager;


/***
 * ADAPTER
 */

public class activity_adapter extends ArrayAdapter<String> implements
        OnClickListener {


    private static Context contexts;
    private Application mAppContext;
    private LayoutInflater mLayoutInflater = null;
    private Random mRandom;
    static String List_img;
    String Liked_name;

    ImageLoader img_loader;


    ArrayList<HashMap<String, String>> imagess = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_imgadpter = new ArrayList<String>();


    private int mSelectedItem;


    ArrayList<String> Array_single_image = new ArrayList<String>();

    public activity_adapter(Context context, int textViewResourceId,
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
            Picasso.with(contexts).load(imagess.get(position).get("Str_Media1_url")).placeholder(R.drawable.placeholderdevzillad).into(vh.Like_picture);
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
        vh.Like_picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i1new = new Intent(mAppContext, Other_user_profile.class);
//                i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i1new.putExtra("User_detail", "@" + Liked_name);
//                mAppContext.startActivity(i1new);
            }
        });
        vh.Img_profilepic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1new = new Intent(mAppContext, Other_user_profile.class);
                i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i1new.putExtra("User_detail", "@" + Liked_name);
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
            Picasso.with(contexts).load(imagess.get(position).get("Str_Profile_image")).placeholder(R.drawable.profile_placeholder).into(vh.Img_profilepic);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }

        //   vh.time.setText(imagess.get(position).get("created_at"));
        return convertView;
    }


    static class ViewHolder {
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