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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/***
 * ADAPTER
 */

public class SampleAdapter extends ArrayAdapter<String> implements
        OnClickListener {
    private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
    private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    private static final String TAG = "SampleAdapter";

    private static Context contexts;
    private Application mAppContext;
    private LayoutInflater mLayoutInflater = null;
    private Random mRandom;
    static String List_img;

    double km, lat_to, lon_to;
    String strlat_to, str_long_to;
    String Str_km = "";

    private LruCache<String, Bitmap> mMemoryCache;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    ArrayList<HashMap<String, String>> imagess = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_imgadpter = new ArrayList<String>();

    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    private int mSelectedItem;

    private PageAdapter mAdapter;


    public SampleAdapter(Context context, int textViewResourceId,
                         ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img, Application app) {
        super(context, textViewResourceId);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRandom = new Random();
        this.imagess = images;
        mAppContext = app;
        //Array_imgadpter.clear();
        this.Array_imgadpter = Array_img;
        contexts = context;
    }

    @SuppressWarnings("static-access")
    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_home_lv,
                    parent, false);
            vh = new ViewHolder();
            //int selectedPage = 0;

            vh.mPager = (ViewPager) convertView.findViewById(R.id.container);
            vh.button2=(Button)convertView.findViewById(R.id.button2);
            vh.mPager.setClickable(true);
            vh.mPager.setEnabled(true);
            vh.mPager.setFocusable(true);
            vh.mPager.setFocusableInTouchMode(true);


            convertView.setTag(vh);
            try {
                vh.mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            final Context context = parent.getContext();
            //vh.mPager.setId(position + 1);
            //vh.mPager.setTag(vh);
            //ViewPager viewPager = vh.mPager;
            vh.mPager.setId(position + 1);
            FragmentManager fm = ((Activity) context).getFragmentManager();

            mAdapter = new PageAdapter(fm);

            vh.mPager.setAdapter(mAdapter);
            //vh.mPager.setCurrentItem(0);
            List_img = Array_imgadpter.get(position);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        //mPager.setCurrentItem(selectedPage);

        vh.mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int positionn = vh.mPager.getId();
                positionn = positionn - 1;
                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
            }

            @Override
            public void onPageSelected(int position) {
                int positionn = vh.mPager.getId();
                positionn = positionn - 1;
                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                int positionn = vh.mPager.getId();
                positionn = positionn - 1;
                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
                //Toast.makeText(mAppContext,postion,Toast.LENGTH_LONG).show();
            }


        });
        vh.mPager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                int positionn = vh.mPager.getId();
//                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
//                List_img = Array_imgadpter.get(positionn);
//                Intent intent = new Intent(mAppContext, Images_comment_screen.class);
//                intent.putExtra("images", List_img);
//
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mAppContext.startActivity(intent);
                Toast.makeText(mAppContext, "clickkkkk", Toast.LENGTH_LONG).show();
            }
        });
//        vh.button2.setOnTouchListener(new View.OnTouchListener() {
//            private float startX;
//            private float startY,endX,endY,diffX,diffY;
//            private long lastTouchDown;
//            private  int CLICK_ACTION_THRESHHOLD = 200;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startX = event.getX();
//                        startY = event.getY();
//
//
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        endX = event.getX();
//                        endY = event.getY();
//
//                        diffX = Math.abs(startX - endX);
//                        diffY = Math.abs(startY - endY);
//
//                        if (diffX <= 5 && diffY <= 5 ) {
//                            //Log.d(LOG_TAG, "A click event!");
//                            Toast.makeText(mAppContext, "cliickk", Toast.LENGTH_LONG).show();
//                        }
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });
//        vh.mPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        Toast.makeText(mAppContext, "down", Toast.LENGTH_LONG).show();
//                        break;
//                    }
//                    case MotionEvent.ACTION_MOVE: {
//                        //Toast.makeText(mAppContext, "move", Toast.LENGTH_LONG).show();
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP: {
//                       // Toast.makeText(mAppContext, "up", Toast.LENGTH_LONG).show();
//                        break;
//                    }
//
//                }
//                return false;
//            }
//
//        });

        // vh.title.setText(imagess.get(position).get("restaurant_name"));


        // vh.imgView.setImageResource(imageHolder.get(position));
//        convertView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mAppContext, "click_convertview", Toast.LENGTH_LONG).show();
//            }
//        });
        return convertView;
    }


    static class ViewHolder {
        ImageView imgView, star1, star2, star3, star4, star5;
        TextView title, adress, fixed_meal, price, rating, distance, TxtVw_Afterdiscount, Txtvw;
        Button BTN_DETAIL;
        private ViewPager mPager;
        Button button2;

    }

    @Override
    public int getCount() {

        return imagess.size();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
//        int positionn = vh.mPager.getId();
//        positionn = positionn - 1;
//        String postion = String.valueOf(positionn);
//        List_img = Array_imgadpter.get(positionn);
//        Intent intent = new Intent(mAppContext, Images_comment_screen.class);
//        intent.putExtra("images", List_img);
//
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mAppContext.startActivity(intent);
        Toast.makeText(mAppContext, "click", Toast.LENGTH_LONG).show();
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

    public static class PlaceholderFragment extends Fragment{

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        private static final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);

            String[] items = List_img.split(",");

            Picasso.with(contexts).load(items[position]).placeholder(R.drawable.placeholder).into(textViewPosition);


            //textViewPosition.setBackgroundResource(COLORS[position]);
            return textViewPosition;
        }


    }

    private static final class PageAdapter extends FragmentStatePagerAdapter {

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
    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
//        if (differenceX > C.CLICK_ACTION_THRESHHOLD/* =5 */ || differenceY > C.CLICK_ACTION_THRESHHOLD) {
//            return false;
//        }
        return true;
    }

}