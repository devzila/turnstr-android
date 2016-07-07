package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import Service_Handler.Constant;
import adapter.SampleAdapter;
import circular_imagview.CircularImageView;
import circular_imagview.ProgressHUD;


public class HomeActivity extends Activity {

    SampleAdapter adapter;
    private PageAdapter mAdapter;
    private ViewPager mPager;
    TextView TxtVWName;
    ProgressHUD mProgressHUD;
    private static Context contexts;
    String acess_token, device_id;
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    CircularImageView CrclImgv;
    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
    ArrayList<String> Array_img = new ArrayList<String>();
    ArrayList<String> Array_single_image = new ArrayList<String>();
    ProgressDialog pDialog;
    Parcelable state;

    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
//        mPager = (ViewPager) findViewById(R.id.container);
//        TxtVWName = (TextView) findViewById(R.id.textView7);
        Intent intent = getIntent();
        String Name = intent.getStringExtra("Name");
        acess_token = intent.getStringExtra("Acess_Token");
        device_id = intent.getStringExtra("device_id");
        // TxtVWName.setText(Name);
        contexts = this;
//		CrclImgv =(CircularImageView)findViewById(R.id.imageView4);
//		CrclImgv.setBorderColor(getResources().getColor(R.color.Loginbg));
//		CrclImgv.setBorderWidth(10);


     //   Lv = (RecyclerView) findViewById(R.id.listView);



//		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//		HashMap<String,String> temp4 = new HashMap<String,String>();
//		temp4.put("name", "Korigad");
//		temp4.put("heightt", "3000ft");
//		temp4.put("heightt", "3000ft");
//		temp4.put("heightt", "3000ft");
//		temp4.put("heightt", "3000ft");
//		data.add(temp4);
//		adapter = new SampleAdapter(HomeActivity.this,
//				android.R.layout.simple_list_item_1, data,
//				getApplication());
//		Lv.setAdapter(adapter);
//        try {
//            mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        mAdapter = new PageAdapter(getFragmentManager());
//
//        mPager.setAdapter(mAdapter);
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
//        boolean hasPassword = sharedPrefs.contains("list");
//        if(hasPassword==true){
//
//            String arr = sharedPrefs.getString("list",null);
//        }
        new Login().execute();


    }


    public static class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
       // private static final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            final ImageView textViewPosition = (ImageView) inflater.inflate(R.layout.fragment_main, container, false);

            Picasso.with(contexts).load("http://i.imgur.com/DvpvklR.png").into(textViewPosition);
            //textViewPosition.setBackgroundResource(COLORS[position - 1]);

            return textViewPosition;
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

    private static final class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position + 1);

            final PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    private class Login extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            android_id = Settings.Secure.getString(HomeActivity.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
            data.clear();
            Array_img.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            String Response = makeServiceCall(Constant.Fetch_posts);
            if (Response != null) {
                try {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(Response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Getting JSON Array node
                    // JSONArray array1 = null;
                    str = jsonObj.getString("status");
                    if (str.contentEquals("true")) {
                        //jsonnode = jsonObj.getJSONObject("data");
                        JSONArray array1 = null;
                        array1 = jsonObj.getJSONArray("data");

                        // looping through All Contacts
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject c = array1.getJSONObject(i);

                            String id = c.getString("id");
                            String user_id = c.getString("user_id");
                            String caption = c.getString("caption");
                            String media1_thumb_url = c.getString("media1_thumb_url");
                            String media2_thumb_url = c.getString("media2_thumb_url");
                            String media3_thumb_url = c.getString("media3_thumb_url");
                            String media4_thumb_url = c.getString("media4_thumb_url");
                            String media1_url = c.getString("media1_url");
                            String media2_url = c.getString("media2_url");
                            String media3_url = c.getString("media3_url");
                            String media4_url = c.getString("media4_url");
                            String created_at = c.getString("created_at");
                            String updated_at = c.getString("updated_at");

                            HashMap<String, String> Info = new HashMap<String, String>();
                            Info.put("id", id);
                            Info.put("user_id", user_id);
                            Info.put("caption", caption);
                            Info.put("media1_thumb_url", media1_thumb_url);
                            Info.put("media2_thumb_url", media2_thumb_url);
                            Info.put("media3_thumb_url", media3_thumb_url);
                            Info.put("media4_thumb_url", media4_thumb_url);
                            Info.put("media1_url", media1_url);
                            Info.put("media2_url", media2_url);
                            Info.put("media3_url", media3_url);
                            Info.put("media4_url", media4_url);
                            Info.put("created_at", created_at);
                            Info.put("updated_at", updated_at);
                            data.add(Info);
                            Array_img.add(media1_url + "," + media2_url + "," + media3_url + "," + media4_url);
                            Array_single_image.add(media1_url);


//						String userid = jsonnode.getString("user_id");
//						Name = json_User.getString("name");
//						access_tocken= jsonnode.getString("access_token");
//						Ostype= jsonnode.getString("device_id");
                        }
                        // looping through All Contacts
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
            pDialog.dismiss();
            adapter = new SampleAdapter(HomeActivity.this,
                    android.R.layout.simple_list_item_1, data, Array_img,Array_single_image,
                    getApplication());
            //Lv.setAdapter(adapter);

           // Lv.onRestoreInstanceState(state);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            SharedPreferences.Editor editor = sharedPrefs.edit();


            JSONArray araay= new JSONArray(data);
            JSONArray araay2= new JSONArray(Array_img);

            editor.putString("list", araay.toString());
            editor.putString("list2", araay2.toString());
            editor.commit();

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public String makeServiceCall(String url) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type

//				if (params != null) {
//					String paramString = URLEncodedUtils
//							.format(params, "utf-8");
//					url += "?" + paramString;
//				}

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("X-TOKEN", acess_token);
            httpGet.addHeader("X-DEVICE", device_id);


            httpResponse = httpClient.execute(httpGet);


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

}
