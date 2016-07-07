package adapter;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
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
import com.ToxicBakery.viewpager.transforms.example.R;
import it.sephiroth.android.library.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import circular_imagview.CircularImageView;
import lazyloading.ImageLoader;
import view_pager.viewpager;


/***
 * ADAPTER
 */

public class explorer_lv_adapter extends ArrayAdapter<String> implements
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
    Session_manager session;
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
    ArrayList<String> Array_Follow = new ArrayList<String>();
    static String[] items;
    String accestoken;
    String follow_status, Userid;
    String device_id;

    public explorer_lv_adapter(Context context, int textViewResourceId,
                               ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img, ArrayList<String> Array_follow, Application app) {
        super(context, textViewResourceId);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mRandom = new Random();
        this.imagess = images;
        this.Array_single_image = Array_img;
        this.Array_Follow = Array_follow;
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
        // img_loader = new ImageLoader(mAppContext);
        // if (convertView == null) {
        convertView = mLayoutInflater.inflate(R.layout.explorer_listview,
                parent, false);

        vh = new ViewHolder();
        // vh.mPager_expore.setId(position + 1);
        vh.Name = (TextView) convertView.findViewById(R.id.textView7);
        session = new Session_manager(mAppContext);
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);
        device_id = user.get(session.device_id);
        vh.Follow_Textview = (TextView) convertView.findViewById(R.id.textView24);
        vh.Caption = (TextView) convertView.findViewById(R.id.textView10);
        vh.Circular_imagview = (CircularImageView) convertView.findViewById(R.id.imageView4);
        //  vh.mPager_expore = (viewpager) convertView.findViewById(R.id.container_explore);
       // vh.imgView = (ImageView) convertView.findViewById(R.id.container_explore);
       // vh.Rlv_Follow = (RelativeLayout) convertView.findViewById(R.id.Rlv_follow);
        vh.Follow_Textview.setId(position);
        follow_status = Array_Follow.get(position);
        if (follow_status.contentEquals("0")) {
            vh.Follow_Textview.setText("Follow");
        } else {
            vh.Follow_Textview.setText("Unfollow");
        }
        // imgView
        convertView.setTag(vh);
        //vh.mPager.setId(position + 1);
//         } else {
//            vh = (ViewHolder) convertView.getTag();
////             vh.mPager.setId(position + 1);
//        }
//            vh.Circular_imagview.setBorderColor(mAppContext.getResources().getColor(R.color.Loginbg));
//            vh.Circular_imagview.setBorderWidth(5);
        //vh.mPager_expore.setId(position + 1);
        vh.imgView.setId(position + 1);
        vh.Rlv_Follow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = vh.imgView.getId();
                id = id - 1;
                follow_status = Array_Follow.get(id);
                if (follow_status.contentEquals("0")) {
                    follow_status = "1";
                    Array_Follow.set(id, "1");
                    Userid = imagess.get(id).get("user_id");
                    vh.Follow_Textview.setText("Unfollow");
                    new Follow_unfollow().execute();

                } else if (follow_status.contentEquals("1")) {
                    follow_status = "0";
                    Userid = imagess.get(id).get("user_id");
                    vh.Follow_Textview.setText("Follow");
                    Array_Follow.set(id, "0");
                    new Follow_unfollow().execute();
                    //notifyDataSetChanged();
                }


                //Toast.makeText(mAppContext, postion, Toast.LENGTH_LONG).show();
                //notifyDataSetChanged();
            }
        });
        vh.imgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = vh.imgView.getId();
                id = id - 1;
                String postion = String.valueOf(id);
                List_img = Array_single_image.get(id);
                String str_id = imagess.get(id).get("Post_id");
                //String status_like=imagess.get(id).get("likestatus");
                String caption = imagess.get(id).get("caption");
                //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                Intent lObjIntent = new Intent(mAppContext, Images_comment_screen.class);
                lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                lObjIntent.putExtra("images", List_img);
                lObjIntent.putExtra("Post_id", str_id);
                //lObjIntent.putExtra("status_like",status_like);
                lObjIntent.putExtra("caption", caption);
                mAppContext.startActivity(lObjIntent);
            }
        });
//        vh.mPager_expore.setOnItemClickListener(new viewpager.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                int id = vh.mPager_expore.getId();
//                id = id - 1;
//                String postion = String.valueOf(id);
//                List_img = Array_single_image.get(id);
//                String str_id =imagess.get(id).get("Post_id");
//                //String status_like=imagess.get(id).get("likestatus");
//                String caption=imagess.get(id).get("caption");
//                //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
//                Intent lObjIntent = new Intent(mAppContext, Images_comment_screen.class);
//                lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                lObjIntent.putExtra("images",List_img);
//                lObjIntent.putExtra("Post_id",str_id);
//                //lObjIntent.putExtra("status_like",status_like);
//                lObjIntent.putExtra("caption",caption);
//                mAppContext.startActivity(lObjIntent);
//                //finish();
//            }
//        });
        Paint paint = new Paint();
        mResources = mAppContext.getResources();
        try {
            final Bitmap srcBitmap = BitmapFactory.decodeResource(mResources, R.drawable.profile_placeholder);
            // Get source bitmap width and height

            int srcBitmapWidth = srcBitmap.getWidth();
            int srcBitmapHeight = srcBitmap.getHeight();

            int borderWidth = 20;
            int shadowWidth = 0;


            int dstBitmapWidth = Math.min(srcBitmapWidth, srcBitmapHeight) + borderWidth * 2;


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


            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mResources, dstBitmap);


            roundedBitmapDrawable.setCircular(true);


            roundedBitmapDrawable.setAntiAlias(true);

            // Set the ImageView image as drawable object
            vh.Circular_imagview.setImageDrawable(roundedBitmapDrawable);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        //convertView.setTag(vh);

        //int selectedPage = 0;
//
//        try {
//            vh.mPager_expore.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        //mPager.setCurrentItem(selectedPage);
        //List_img = Array_single_image.get(position);
        vh.Name.setText(imagess.get(position).get("username"));
        vh.Caption.setText(imagess.get(position).get("caption"));
        try {
            Picasso.with(contexts).load(imagess.get(position).get("media_url")).placeholder(R.drawable.placeholderdevzillad).into(vh.imgView);
            //
            // img_loader.DisplayImage(url,textViewPosition);
        } catch (OutOfMemoryError error) {

        }
//        vh.mPager_expore.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                int positionn = vh.mPager_expore.getId();
//                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
//                List_img = Array_single_image.get(positionn);
//               // items = List_img.split(",");
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                int positionn = vh.mPager_expore.getId();
//                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
//                List_img = Array_single_image.get(positionn);
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//                int positionn = vh.mPager_expore.getId();
//                positionn = positionn - 1;
//                String postion = String.valueOf(positionn);
//                List_img = Array_single_image.get(positionn);
//
//
//            }
//
//
//        });
//
//
//
//
//        FragmentManager fm = ((Activity) context).getFragmentManager();
//
//        mAdapter = new PageAdapter(fm);
//
//        vh.mPager_expore.setAdapter(mAdapter);

        return convertView;
    }


    static class ViewHolder {
        ImageView imgView, star1, star2, star3, star4;
        TextView Name, Caption, Follow_Textview, price, rating, distance, TxtVw_Afterdiscount, Txtvw;
        Button BTN_DETAIL;
        private viewpager mPager_expore;
        Button button2;
        CircularImageView Circular_imagview;
        RelativeLayout Rlv_Follow;

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
            final ImageView img_explore = (ImageView) inflater.inflate(R.layout.xplore_layout_imageview, container, false);

            items = List_img.split(",");
            String url = items[position];

            try {
                Picasso.with(contexts).load(url).placeholder(R.drawable.placeholderdevzillad).into(img_explore);
                //
                // img_loader.DisplayImage(url,textViewPosition);
            } catch (OutOfMemoryError error) {

            }


            //textViewPosition.setBackgroundResource(COLORS[position]);
            return img_explore;
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
            nameValuePairs.add(new BasicNameValuePair("following_id", Userid));
            nameValuePairs.add(new BasicNameValuePair("following_status", follow_status));


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


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
    private class Explore_follow extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("access_token", accestoken));
            jsonStr = sh.makeServiceCall(Constant.Explorer,
                    ServiceHandler.POST, nameValuePairs);




            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    JSONArray Array_image = null;
                    Array_image = jsonObj.getJSONArray("data");
                    for (int i = 0; i < Array_image.length(); i++) {
                        JSONObject c = Array_image.getJSONObject(i);



                        String Str_Follow = c.getString("follow");
                        Array_Follow.add(Str_Follow);
                        // tmp hashmap for single contact

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

            //  pDialog.dismiss();


            notifyDataSetChanged();
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

}