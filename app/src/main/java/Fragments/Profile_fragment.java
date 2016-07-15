package Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.example.Edit_profile;
import com.ToxicBakery.viewpager.transforms.example.Follower_following_screen;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Multipart_enttity.AndroidMultiPartEntity;
import Service_Handler.Constant;
import Session_handler.Session_manager;
import adapter.Grid_view_adapter;
import dgcam.DgCamActivity;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;


public class Profile_fragment extends Fragment {
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> List_Profile = new ArrayList<HashMap<String, String>>();
    Session_manager session;
    String accestoken;
    String device_id;
    GridView gridView;
    ArrayList<HashMap<String, String>> List_array = new ArrayList<HashMap<String, String>>();
    String user_name, Name, Posts, image_profile;
    ImageView img_profile;
    TextView txt_name, Txt_Username, Txt_post, Txt_follower, Txt_following;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    RelativeLayout rlv_edt_profile, Rlv_follow, Rlv_following;
    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    String userid, Email, username, First_name, bio, phone, gender, website, profile_image, Str_follower, StrFollowing;
    Bitmap bm;
    long totalSize = 0;
    String selectedImagePath;
    ImageLoader img_loader;
    ArrayList<String> Array_imgProfile = new ArrayList<String>();
    SwipeRefreshLayout SRL;
    int paging_position = 0;
    int Scroll_position = 0;
    String paging = "0";
    String Str_check_response,user_id;
    String Str_check_Refresh = "NoScrolled";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.setting_activity, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        session = new Session_manager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        image_profile = user.get(session.Profile_PIC);
        txt_name = (TextView) rootView.findViewById(R.id.textView14);
        Txt_Username = (TextView) rootView.findViewById(R.id.textView11);
        Txt_post = (TextView) rootView.findViewById(R.id.textView18);
        img_profile = (ImageView) rootView.findViewById(R.id.imageView4);
        rlv_edt_profile = (RelativeLayout) rootView.findViewById(R.id.editprofile);
        Txt_follower = (TextView) rootView.findViewById(R.id.textView19);
        Txt_following = (TextView) rootView.findViewById(R.id.textView20);
        Rlv_follow = (RelativeLayout) rootView.findViewById(R.id.relativeLayout10);
        Rlv_following = (RelativeLayout) rootView.findViewById(R.id.rlv_following);
        img_loader = new ImageLoader(getActivity());
        // img_profile.setImageResource(R.drawable.profile_placeholder);
        // access_token
        accestoken = user.get(session.Acess_Token);
        SRL = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Array_imgProfile.clear();
                List_array.clear();
                paging = "0";
                new User_profile().execute();
               //User_profile new Profile_Posts().execute();
            }

        });
        device_id = user.get(session.device_id);
        userid = user.get(session.User_id);

        //  Picasso.with(getActivity()).load(image_profile).placeholder(R.drawable.profile_placeholder).into(img_profile);
        new User_profile().execute();
        //new Profile_Posts().execute();
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                // Here, thisActivity is the current activity
//                if (ContextCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.READ_CONTACTS)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    // Should we show an explanation?
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                            Manifest.permission.CAMERA)) {
//
//                        // Show an expanation to the user *asynchronously* -- don't block
//                        // this thread waiting for the user's response! After the user
//                        // sees the explanation, try again to request the permission.
//
//                    } else {
//
//                        // No explanation needed, we can request the permission.
//
//                        ActivityCompat.requestPermissions(getActivity(),
//                                new String[]{Manifest.permission.CAMERA},
//                                1);
//
//                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                        // app-defined int constant. The callback method gets the
//                        // result of the request.
//                    }
//                }

                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA);
                int permissionCheck_ForGallery = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck_For_Audio = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.RECORD_AUDIO);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
//                    Intent i1 = new Intent(MainActivity.this,
//                            DgCamActivity.class);
//
//                    startActivity(i1);
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            1);

                    Toast.makeText(getActivity(), "you need to enable camera permission", Toast.LENGTH_LONG).show();
                } else if (permissionCheck_ForGallery == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);
                    Toast.makeText(getActivity(), "you need to enable External Storage permission", Toast.LENGTH_LONG).show();
                } else {
                    selectImage();
                }

            }
        });
        rlv_edt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Edit_profile.class);
                getActivity().startActivity(intent);
            }
        });
        Rlv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(getActivity(), Follower_following_screen.class);

                int_follower.putExtra("Status", "Followers");
                int_follower.putExtra("userid", user_id);
                int_follower.putExtra("Screen_check", "me");

                getActivity().startActivity(int_follower);

            }
        });
        Rlv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(getActivity(), Follower_following_screen.class);
                int_follower.putExtra("Status", "Following");
                int_follower.putExtra("userid", user_id);
                int_follower.putExtra("Screen_check", "me");
                getActivity().startActivity(int_follower);
            }
        });
        return rootView;
    }

    private class User_profile extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype, image;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            JSONObject jsonnode, json_User;
            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
            android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;

            List_Profile.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            String Response = makeServiceCall_get(Constant.MyProfile);
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
//                    str = jsonObj.getString("status");
//                    JSONArray Array_image = null;
//                    if (str.contentEquals("true")) {
                    //jsonnode = jsonObj.getJSONObject("data");
                    //JSONArray array1 = null;
                    jsonnode = jsonObj.getJSONObject("data");
                    json_User = jsonnode.getJSONObject("userData");
                    image = json_User.getString("profile_image");
                    //  user_name = json_User.getString("name");
                    Name = json_User.getString("name");
                    user_id = json_User.getString("id");
                    user_name = json_User.getString("username");
                    Posts = json_User.getString("postCount");
                    Email = json_User.getString("email");
                    username = json_User.getString("username");
                    First_name = json_User.getString("name");
                    bio = json_User.getString("bio");
                    phone = json_User.getString("phone_number");
                    gender = json_User.getString("gender");
                    website = json_User.getString("website");
                    profile_image = json_User.getString("profile_image");
                    Str_follower = json_User.getString("followers");
                    StrFollowing = json_User.getString("following");
                    // looping through All Contacts
                    //}
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
            //pDialog.dismiss();
            session.createLoginSession(accestoken, device_id, userid, Email, username, First_name, bio
                    , phone, gender, website, profile_image);
            txt_name.setText(user_name);
            //Txt_Username.setText(user_name);
            Txt_Username.setText(Name);
            Txt_post.setText(Posts);
            Txt_follower.setText(Str_follower);
            Txt_following.setText(StrFollowing);
            try {
                Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile_placeholder).into(img_profile);
            }catch (java.lang.IllegalArgumentException e){
                e.printStackTrace();
            }
            //img_loader.DisplayImage(image, img_profile);
            List_array.clear();
            new Profile_Posts().execute();
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    private class Profile_Posts extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode, json_User;
        String str;
        String Name, access_tocken, Ostype;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SRL.setRefreshing(true);
//            Array_imgProfile.clear();
//            List_array.clear();

            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            String Response = makeServiceCall(Constant.Profile_Posts);
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
                        jsonnode = jsonObj.getJSONObject("data");
                        JSONArray array1 = null;
                        array1 = jsonnode.getJSONArray("postDetails");
                        int array_size = array1.length();
                        Str_check_response = String.valueOf(array_size);
                        // looping through All Contacts
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject c = array1.getJSONObject(i);
                            String media_url1 = c.getString("media1_thumb_url");
                            String media_url = c.getString("media1_url");
                            String media_url2 = c.getString("media2_thumb_url");
                            String media_url3 = c.getString("media3_thumb_url");
                            String media_url4 = c.getString("media4_thumb_url");
                            String Caption = c.getString("caption");
                            String Liked = c.getString("liked");
                            String is_following = c.getString("is_following");
                            String Post_id = c.getString("id");


                            // tmp hashmap for single contact
                            HashMap<String, String> Profile_images = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            Profile_images.put("media_url", media_url1);
                            Profile_images.put("Caption", Caption);
                            Profile_images.put("Liked", Liked);
                            Profile_images.put("is_following", is_following);
                            Profile_images.put("Post_id", Post_id);


                            // adding contact to contact list
                            List_array.add(Profile_images);
                            Array_imgProfile.add(media_url1 + "," + media_url2 + "," + media_url3 + "," + media_url4);

                        }

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
            // pDialog.dismiss();
            SRL.setRefreshing(false);
            try {
                Grid_view_adapter_profile_local adapter = new Grid_view_adapter_profile_local(getActivity(),
                        android.R.layout.simple_list_item_1, List_array, Array_imgProfile,
                        getActivity().getApplication());
                gridView.setAdapter(adapter);
                gridView.setSelection(Scroll_position);
                SRL.setRefreshing(false);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }

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
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("page", paging));

            HttpPost http_post = new HttpPost(url);
            http_post.addHeader("X-TOKEN", accestoken);
            http_post.addHeader("X-DEVICE", device_id);
            http_post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpResponse = httpClient.execute(http_post);


            httpEntity = httpResponse.getEntity();
            try {
                response = EntityUtils.toString(httpEntity);
            } catch (java.lang.OutOfMemoryError e) {
                e.printStackTrace();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }

    public String makeServiceCall_get(String url) {
        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;


            HttpGet http_post = new HttpGet(url);
            http_post.addHeader("X-TOKEN", accestoken);
            http_post.addHeader("X-DEVICE", device_id);


            httpResponse = httpClient.execute(http_post);


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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        selectedImagePath = destination.getAbsolutePath();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        img_profile.setImageBitmap(thumbnail);
        new UploadFileToServer().execute();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        selectedImagePath = cursor.getString(column_index);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        img_profile.setImageBitmap(bm);
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constant.ProfileImage_Upload);
            httppost.addHeader("X-TOKEN", accestoken);
            httppost.addHeader("X-DEVICE", device_id);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(selectedImagePath);

                // Adding file data to http body
                entity.addPart("profile_image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //  showAlert(result);
            progressBar.setVisibility(View.GONE);
            new User_profile().execute();
            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class Grid_view_adapter_profile_local extends ArrayAdapter<String> {
        private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
        private static final String KEY_SELECTED_CLASS = "KEY_SELECTED_CLASS";
        ArrayList<String> Array_img = new ArrayList<String>();
        private static final String TAG = "SampleAdapter";

        private Context contexts;
        private Application mAppContext;
        private LayoutInflater mLayoutInflater = null;
        private Random mRandom;
        String List_img;

        double km, lat_to, lon_to;
        String strlat_to, str_long_to;
        String Str_km = "";

        ArrayList<HashMap<String, String>> images = new ArrayList<HashMap<String, String>>();

        ImageLoader img_loader;

        //img_loader.DisplayImage(url,textViewPosition);
        public Grid_view_adapter_profile_local(Context context, int textViewResourceId,
                                               ArrayList<HashMap<String, String>> images, ArrayList<String> Array_img, Application app) {
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
            //String image =  Array_img.get(position);
            // img_loader.DisplayImage(image,vh.imgView);
            try {
                Picasso.with(contexts).load(image).placeholder(R.drawable.placeholderdevzillad).resize(150, 150).into(vh.imgView);
            }catch (java.lang.IllegalArgumentException e){
                e.fillInStackTrace();
            }
            vh.imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_id = images.get(position).get("Post_id");
                    String status_like = images.get(position).get("Liked");
                    String caption = images.get(position).get("Caption");
                    List_img = Array_img.get(position);
                    //Toast.makeText(mAppContext,"click "+id,Toast.LENGTH_LONG).show();
                    Intent lObjIntent = new Intent(contexts, Images_comment_screen.class);
                    lObjIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    lObjIntent.putExtra("images", List_img);
                    lObjIntent.putExtra("Post_id", str_id);
                    lObjIntent.putExtra("Check_follow", "GONE");
                    lObjIntent.putExtra("status_like", status_like);
                    lObjIntent.putExtra("caption", caption);
                    contexts.startActivity(lObjIntent);
                }
            });

            if ((position >= getCount() - 1)) {
                if(images.size()!=1){
                if (!Str_check_response.contentEquals("0")) {
                    paging_position++;
                    paging = String.valueOf(paging_position);
                    // paging = "1";
                    Scroll_position = position;
                    Str_check_Refresh = "scrolled";
                    new Profile_Posts().execute();
                }
                    //System.out.println("Recycleview page Scrolling...."+position);
                } else {
                    Str_check_Refresh = "NoScrolled";
                    paging_position = 0;
                    paging = "0";
                }
            }

            //convertView.setTag(vh);
            return convertView;
        }


        class ViewHolder {
            ImageView imgView;


        }

        @Override
        public int getCount() {

            return images.size();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // selectImage();
                    Toast.makeText(getActivity(), "enabled camera", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getActivity(), "you need to enable camera permission", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // selectImage();
                    Toast.makeText(getActivity(), "enabled external Storage", Toast.LENGTH_LONG).show();
                } else {
                    // Toast.makeText(getActivity(), "you need to enable camera permission", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}