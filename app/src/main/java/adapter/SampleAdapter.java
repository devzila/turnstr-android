package adapter;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import circular_imagview.CircularImageView;
import lazyloading.ImageLoader;
import view_pager.viewpager;


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
    ImageLoader img_loader;
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
    ArrayList<String> Array_single_image = new ArrayList<String>();
    static String[] items;

    public SampleAdapter(Context context, int textViewResourceId,
                         ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img, ArrayList<String> Array_singr, Application app) {
        super(context, textViewResourceId);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRandom = new Random();
        this.imagess = images;
        Array_single_image = Array_singr;
        mAppContext = app;
        //Array_imgadpter.clear();
        this.Array_imgadpter = Array_img;
        contexts = context;
    }

    @SuppressWarnings("static-access")
    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        Resources mResources;
        final ViewHolder vh;
        final Context context = parent.getContext();
         img_loader = new ImageLoader(mAppContext);
         // if (convertView == null) {
        convertView = mLayoutInflater.inflate(R.layout.list_home_lv,
                parent, false);

        vh = new ViewHolder();
        vh.mPager = (viewpager) convertView.findViewById(R.id.containerr);
        vh.Name = (TextView) convertView.findViewById(R.id.textView7);
        vh.Caption = (TextView) convertView.findViewById(R.id.textView10);
        vh.Circular_imagview = (CircularImageView) convertView.findViewById(R.id.imageView4);
        convertView.setTag(vh);
        //vh.mPager.setId(position + 1);
//         } else {
//            vh = (ViewHolder) convertView.getTag();
////             vh.mPager.setId(position + 1);
//        }
//            vh.Circular_imagview.setBorderColor(mAppContext.getResources().getColor(R.color.Loginbg));
//            vh.Circular_imagview.setBorderWidth(5);
        vh.mPager.setId(position + 1);
        vh.mPager.setOnItemClickListener(new viewpager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int id = vh.mPager.getId();
                id = id - 1;
                String postion = String.valueOf(id);
                List_img = Array_imgadpter.get(id);
                String str_id =imagess.get(id).get("Post_id");
                String status_like=imagess.get(id).get("likestatus");
                String caption=imagess.get(id).get("caption");
                //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                Intent lObjIntent = new Intent(mAppContext, Images_comment_screen.class);
                lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                lObjIntent.putExtra("images",List_img);
                lObjIntent.putExtra("Post_id",str_id);
                lObjIntent.putExtra("status_like",status_like);
                lObjIntent.putExtra("caption",caption);
                mAppContext.startActivity(lObjIntent);
                //finish();
            }
        });
        Paint paint = new Paint();
        mResources = mAppContext.getResources();
        try {
            final Bitmap srcBitmap = BitmapFactory.decodeResource(mResources, R.drawable.profile_placeholder);
            // Get source bitmap width and height

            int srcBitmapWidth = srcBitmap.getWidth();
            int srcBitmapHeight = srcBitmap.getHeight();

                /*
                    IMPORTANT NOTE : You should experiment with border and shadow width
                    to get better circular ImageView as you expected.
                    I am confused about those size.
                */
            // Define border and shadow width
            int borderWidth = 20;
            int shadowWidth = 0;

            // destination bitmap width
            int dstBitmapWidth = Math.min(srcBitmapWidth, srcBitmapHeight) + borderWidth * 2;
            //float radius = Math.min(srcBitmapWidth,srcBitmapHeight)/2;

            // Initializing a new bitmap to draw source bitmap, border and shadow

            Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(dstBitmap);




            // Initialize a new canvas

            // Draw a solid color to canvas
            canvas.drawColor(Color.WHITE);

            // Draw the source bitmap to destination bitmap by keeping border and shadow spaces
            canvas.drawBitmap(srcBitmap, (dstBitmapWidth - srcBitmapWidth) / 2, (dstBitmapWidth - srcBitmapHeight) / 2, null);

            // Use Paint to draw border
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth * 2);
            paint.setColor(Color.WHITE);

            // Draw the border in destination bitmap
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, paint);

            // Use Paint to draw shadow
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(shadowWidth);

            // Draw the shadow on circular bitmap
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2, paint);

                /*
                    RoundedBitmapDrawable
                        A Drawable that wraps a bitmap and can be drawn with rounded corners. You
                        can create a RoundedBitmapDrawable from a file path, an input stream, or
                        from a Bitmap object.
                */
            // Initialize a new RoundedBitmapDrawable object to make ImageView circular
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, dstBitmap);

                /*
                    setCircular(boolean circular)
                        Sets the image shape to circular.
                */
            // Make the ImageView image to a circular image
            roundedBitmapDrawable.setCircular(true);

                /*
                    setAntiAlias(boolean aa)
                        Enables or disables anti-aliasing for this drawable.
                */
            roundedBitmapDrawable.setAntiAlias(true);

            // Set the ImageView image as drawable object
            vh.Circular_imagview.setImageDrawable(roundedBitmapDrawable);
        }catch (OutOfMemoryError error){
            error.printStackTrace();
        }
        //convertView.setTag(vh);

        //int selectedPage = 0;
        //vh.mPager.setId(position + 1);
        try {
            vh.mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




        //mPager.setCurrentItem(selectedPage);
        List_img = Array_imgadpter.get(position);
        vh.Name.setText(imagess.get(position).get("username"));
        vh.Caption.setText(imagess.get(position).get("caption"));
        vh.mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int positionn = vh.mPager.getId();
                positionn = positionn - 1;
                String postion = String.valueOf(positionn);
                List_img = Array_imgadpter.get(positionn);
                items = List_img.split(",");

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


            }


        });




        FragmentManager fm = ((Activity) context).getFragmentManager();

        mAdapter = new PageAdapter(fm);

        vh.mPager.setAdapter(mAdapter);

        return convertView;
    }


    static class ViewHolder {
        ImageView imgView, star1, star2, star3, star4;
        TextView Name, Caption, fixed_meal, price, rating, distance, TxtVw_Afterdiscount, Txtvw;
        Button BTN_DETAIL;
        private viewpager mPager;
        Button button2;
        CircularImageView Circular_imagview;

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

    public class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);

            items = List_img.split(",");
            String url = items[position];

            try {
                Picasso.with(contexts).load(items[position]).placeholder(R.drawable.placeholderdevzillad).into(textViewPosition);
                //
               // img_loader.DisplayImage(url,textViewPosition);
            } catch (OutOfMemoryError error) {

            }


            //textViewPosition.setBackgroundResource(COLORS[position]);
            return textViewPosition;
        }


    }

    private class PageAdapter extends FragmentStatePagerAdapter {

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