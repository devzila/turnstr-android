package com.ToxicBakery.viewpager.transforms.example;

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.sephiroth.android.library.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Multipart_enttity.AndroidMultiPartEntity;
import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;

public class Edit_profile extends Activity {
    EditText Edtxt_fistname, Email, Usrname, website, bio, phone, gender;
    TextView txt_done, Edit_profile;
    LinearLayout lv_username, Lv_email;
    ProgressDialog pDialog;
    Session_manager session;
    RelativeLayout rlvCancel, rlvdone;
    String accestoken;
    private ProgressBar progressBar;
    String selectedImagePath;
    String device_id;
    String First_name, StrEmail, Str_username, strWebsite, str_bio, str_email, Str_phone, Str_gender, Str_usenamee;
    String FIRST_NAME, BIO, PHONE, GENDER, WEBSITE, PROFILE_IMAGE;
    ImageView profile_imageimg;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    long totalSize = 0;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_profile);
        Edtxt_fistname = (EditText) findViewById(R.id.editText7);
        rlvCancel = (RelativeLayout) findViewById(R.id.rlv_cancel);
        rlvdone = (RelativeLayout) findViewById(R.id.rlv_done);
        txt_done = (TextView) findViewById(R.id.textView29);
        Usrname = (EditText) findViewById(R.id.edttxt_usrname);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        website = (EditText) findViewById(R.id.edttxt_website);
        bio = (EditText) findViewById(R.id.edttxt_Bio);
        Email = (EditText) findViewById(R.id.edttxt_Email);
        Edit_profile = (TextView) findViewById(R.id.textView14);
        phone = (EditText) findViewById(R.id.edttxt_phone);
        gender = (EditText) findViewById(R.id.edttxt_gender);
        profile_imageimg = (ImageView) findViewById(R.id.imageView4);
        lv_username = (LinearLayout) findViewById(R.id.lvusername);
        Lv_email = (LinearLayout) findViewById(R.id.lvEmail);
        session = new Session_manager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);
        device_id = user.get(session.device_id);
        StrEmail = user.get(session.Email);
        Str_usenamee = user.get(session.Username);
        FIRST_NAME = user.get(session.fIrst_name);
        BIO = user.get(session.Bioo);
        PHONE = user.get(session.Phone);
        GENDER = user.get(session.Gender);
        WEBSITE = user.get(session.WEbsite);
        PROFILE_IMAGE = user.get(session.Profile_PIC);
        Email.setText(StrEmail);
        Usrname.setText(Str_usenamee);
        Edtxt_fistname.setText(FIRST_NAME);
        bio.setText(BIO);
        phone.setText(PHONE);
        gender.setText(GENDER);
        website.setText(WEBSITE);
        try {
            Picasso.with(Edit_profile.this).load(PROFILE_IMAGE).placeholder(R.drawable.profile_placeholder).into(profile_imageimg);
        } catch (java.lang.IllegalArgumentException e) {
            e.printStackTrace();
        }
        rlvdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Usrname.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    lv_username.startAnimation(anm);
                } else if (Email.getText().toString().contentEquals("")) {
                    Animation anm = Shake_Animation();
                    Lv_email.startAnimation(anm);
                } else {
                    First_name = Edtxt_fistname.getText().toString();
                    Str_username = Usrname.getText().toString();
                    strWebsite = website.getText().toString();
                    str_bio = bio.getText().toString();
                    str_email = Email.getText().toString();
                    Str_phone = phone.getText().toString();
                    Str_gender = gender.getText().toString();
                    new Editprofile().execute();

                }
            }
        });
        rlvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profile_imageimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        Edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

//{"data":"","message":"Profile updated successfully","status":true,"statusCode":200}
    }

    public Animation Shake_Animation() {
        Animation shake = new TranslateAnimation(0, 5, 0, 0);
        shake.setInterpolator(new CycleInterpolator(5));
        shake.setDuration(300);


        return shake;
    }

    private class Editprofile extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode;
        String str;
        String Deviceid, Token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            // Showing progress dialog
            pDialog = new ProgressDialog(Edit_profile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
            nameValuePairs.add(new BasicNameValuePair("username", Str_username));
            nameValuePairs.add(new BasicNameValuePair("email", str_email));
            nameValuePairs.add(new BasicNameValuePair("name", First_name));
            nameValuePairs.add(new BasicNameValuePair("phone_number", Str_phone));
            nameValuePairs.add(new BasicNameValuePair("gender", Str_gender));
            nameValuePairs.add(new BasicNameValuePair("bio", str_bio));
            nameValuePairs.add(new BasicNameValuePair("website", strWebsite));

            String jsonStr = sh.makeServiceCall_withHeader(Constant.Update_profile,
                    ServiceHandler.POST, nameValuePairs, accestoken, device_id);

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
                    // JSONArray array1 = null;
                    str = jsonObj.getString("status");


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

            if (str.contentEquals("true")) {
                Intent i1 = new Intent(Edit_profile.this,
                        MainActivity.class);

                startActivity(i1);

                finish();
            } else {
                Toast.makeText(Edit_profile.this, "Not Updated", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_profile.this);
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

        profile_imageimg.setImageBitmap(thumbnail);
        new UploadFileToServer().execute();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null,
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

        profile_imageimg.setImageBitmap(bm);
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
            // new User_profile().execute();
            super.onPostExecute(result);
        }

    }
}
