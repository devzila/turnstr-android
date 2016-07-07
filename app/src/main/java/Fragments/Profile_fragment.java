package Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.example.Edit_profile;
import com.ToxicBakery.viewpager.transforms.example.Follower_following_screen;
import com.ToxicBakery.viewpager.transforms.example.Images_comment_screen;
import com.ToxicBakery.viewpager.transforms.example.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
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

import Multipart_enttity.AndroidMultiPartEntity;
import Service_Handler.Constant;
import Session_handler.Session_manager;
import adapter.Grid_view_adapter;
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
                new Profile_Posts().execute();
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
                selectImage();
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

                int_follower.putExtra("Status", "Follower");
                getActivity().startActivity(int_follower);

            }
        });
        Rlv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_follower = new Intent(getActivity(), Follower_following_screen.class);
                int_follower.putExtra("Status", "Following");
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
            //   Picasso.with(getActivity()).load(image).placeholder(R.drawable.profile_placeholder).into(img_profile);
            img_loader.DisplayImage(image, img_profile);
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
            Array_imgProfile.clear();
            List_array.clear();

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

                        // looping through All Contacts
                        for (int i = 0; i < array1.length(); i++) {
                            JSONObject c = array1.getJSONObject(i);
                            String media_url = c.getString("media1_url");
                            String media_url2 = c.getString("media2_url");
                            String media_url3 = c.getString("media3_url");
                            String media_url4 = c.getString("media4_url");
                            String Caption = c.getString("caption");
                            String Liked = c.getString("liked");
                            String is_following = c.getString("is_following");
                            String Post_id = c.getString("id");


                            // tmp hashmap for single contact
                            HashMap<String, String> Profile_images = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            Profile_images.put("media_url", media_url);
                            Profile_images.put("Caption", Caption);
                            Profile_images.put("Liked", Liked);
                            Profile_images.put("is_following", is_following);
                            Profile_images.put("Post_id", Post_id);


                            // adding contact to contact list
                            List_array.add(Profile_images);
                            Array_imgProfile.add(media_url + "," + media_url2 + "," + media_url3 + "," + media_url4);

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
            try {
                Grid_view_adapter adapter = new Grid_view_adapter(getActivity(),
                        android.R.layout.simple_list_item_1, List_array, Array_imgProfile,
                        getActivity().getApplication());
                gridView.setAdapter(adapter);
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


            HttpPost http_post = new HttpPost(url);
            http_post.addHeader("X-TOKEN", accestoken);
            http_post.addHeader("X-DEVICE", device_id);


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


}