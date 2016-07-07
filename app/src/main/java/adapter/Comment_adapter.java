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
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Day_time_calculator.time_calculator;
import Square_imageview.ProportionalImageView;
import Square_imageview.square_viewpager;
import circular_imagview.CircularImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import hashTag.CalloutLink;
import hashTag.Hashtag;
import lazyloading.ImageLoader;
import view_pager.viewpager;


/***
 * ADAPTER
 */

public class Comment_adapter extends ArrayAdapter<String> implements
        OnClickListener {


    private static Context contexts;
    private Application mAppContext;
    private LayoutInflater mLayoutInflater = null;
    private Random mRandom;
    static String List_img;

    ImageLoader img_loader;


    ArrayList<HashMap<String, String>> imagess = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_imgadpter = new ArrayList<String>();


    private int mSelectedItem;


    ArrayList<String> Array_single_image = new ArrayList<String>();

    public Comment_adapter(Context context, int textViewResourceId,
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

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.omment_adapter_layout,
                    parent, false);

            vh = new ViewHolder();
            vh.Name = (TextView) convertView.findViewById(R.id.textView31);
            vh.Comment = (EmojiconTextView) convertView.findViewById(R.id.txtEmojicon);
            vh.time = (TextView) convertView.findViewById(R.id.textView33);
            vh.Img_profilepic = (ImageView) convertView.findViewById(R.id.imageView32);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
       // String str = "@Kirank ,#nirmal You've gotta #dance like there's nobody watching,#Love like you'll never be #hurt,#Sing like there's @nobody listening,And live like it's #heaven on #earth.";
        //String str  =  "the text view ";
        String str= imagess.get(position).get("comments");

        ArrayList<int[]> hashtagSpans1 = getSpans(str, '#');
        ArrayList<int[]> calloutSpans1 = getSpans(str, '@');

        SpannableString commentsContent1 =
                new SpannableString(str);

        setSpanComment(commentsContent1, hashtagSpans1);
        setSpanUname(commentsContent1, calloutSpans1);

        vh.Comment.setMovementMethod(LinkMovementMethod.getInstance());
        vh.Comment.setText(commentsContent1);
       // vh.Comment.setText(imagess.get(position).get("comments"));
        vh.Name.setText(imagess.get(position).get("username"));
        String Str_day_time = imagess.get(position).get("created_at");
        time_calculator time_lass = new time_calculator();
        String Filtertime = time_lass.parseDate(Str_day_time);
        vh.time.setText(Filtertime);


        // Set the ImageView image as drawable object
        // vh.Img_profilepic.setImageDrawable(roundedBitmapDrawable);
        img_loader = new ImageLoader(mAppContext);
        img_loader.DisplayImage(imagess.get(position).get("profile_image"), vh.Img_profilepic);
        vh.Img_profilepic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1new = new Intent(mAppContext, Other_user_profile.class);
                i1new.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i1new.putExtra("User_detail", "@"+imagess.get(position).get("username"));
                mAppContext.startActivity(i1new);
            }
        });
        // Picasso.with(contexts).load(imagess.get(position).get("profile_image")).placeholder(R.drawable.profile_placeholder).into(vh.Img_profilepic);
        //   vh.time.setText(imagess.get(position).get("created_at"));
        return convertView;
    }


    static class ViewHolder {
        ImageView Img_profilepic, star1, star2, star3, star4;
        TextView Name, time, price, rating, distance, TxtVw_Afterdiscount, Txtvw;
        EmojiconTextView Comment;

    }

    @Override
    public int getCount() {

        return imagess.size();
    }


    @Override
    public void onClick(View v) {

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

            commentsContent.setSpan(new Hashtag(mAppContext),
                    hashTagStart,
                    hashTagEnd, 0);

        }


    }

    private void setSpanUname(SpannableString commentsUname, ArrayList<int[]> calloutSpans) {
        for (int i = 0; i < calloutSpans.size(); i++) {
            int[] span = calloutSpans.get(i);
            int calloutStart = span[0];
            int calloutEnd = span[1];
            commentsUname.setSpan(new CalloutLink(mAppContext),
                    calloutStart,
                    calloutEnd, 0);

        }
    }
}