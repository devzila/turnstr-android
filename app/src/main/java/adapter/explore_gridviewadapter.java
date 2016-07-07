package adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.R;

import it.sephiroth.android.library.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import lazyloading.ImageLoader;


/***
 * ADAPTER
 */

public class explore_gridviewadapter extends ArrayAdapter<String> {
    private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
    private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
    ArrayList<String> Array_img = new ArrayList<String>();
    private static final String TAG = "SampleAdapter";

    private static Context contexts;
    private Application mAppContext;
    private LayoutInflater mLayoutInflater = null;
    private Random mRandom;
    static String List_img;

    double km, lat_to, lon_to;
    String strlat_to, str_long_to;
    String Str_km = "";

    ArrayList<HashMap<String, String>> images = new ArrayList<HashMap<String, String>>();

    ImageLoader img_loader;

    //img_loader.DisplayImage(url,textViewPosition);
    public explore_gridviewadapter(Context context, int textViewResourceId,
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
        // img_loader.DisplayImage(image,vh.imgView);
        Picasso.with(contexts).load(image).placeholder(R.drawable.placeholderdevzillad).into(vh.imgView);


        //convertView.setTag(vh);
        return convertView;
    }


    static class ViewHolder {
        ImageView imgView;


    }

    @Override
    public int getCount() {

        return images.size();
    }


}