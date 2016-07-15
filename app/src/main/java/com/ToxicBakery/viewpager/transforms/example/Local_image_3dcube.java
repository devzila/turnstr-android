package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.aviary.android.feather.library.Constants;
import com.aviary.android.feather.sdk.FeatherActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.sprylab.android.widget.TextureVideoView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import Image_resizer.ImageResizer;
import Multipart_enttity.AndroidMultiPartEntity;
import Service_Handler.Constant;
import Session_handler.Session_manager;
import dgcam.DgCamActivity;
import it.sephiroth.android.library.picasso.Picasso;
import lazyloading.ImageLoader;
import lazyloading.Utils;
import operations.ImageRotation;
import view_pager.viewpager;

public class Local_image_3dcube extends Activity {
    private viewpager mPager, mpager2;
    ArrayList<String> arr;
    ArrayList<String> arr_aviary = new ArrayList<String>();
    Button Btn_upload, Btn_upload_popup;
    private ProgressBar progressBar;
    ProgressDialog pDialog;
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    long totalSize = 0;
    private PageAdapter mAdapter;
    PageAdapter_popup mAdapter_popup;
    ImageView img_filter;
    int Positionnn = 0;
    private CallbackManager callbackManager;
    PopupWindow pwindo;
    Button Btn_skip, Btn_addfilter;

    static {
        TRANSFORM_CLASSES = new ArrayList<>();


        TRANSFORM_CLASSES.add(new TransformerItem(CubeOutTransformer.class));
        //TRANSFORM_CLASSES.add(new TransformerItem(CubeInTransformer.class));
    }

    String img_1, Str_img2, Str_img3, Str_img4;
    Session_manager session;
    String accestoken;
    String device_id;
    ImageView Btn_cross, Img_cross;
    EditText Edttext_caption;
    String Caption;
    String responseString = null;
    String Url_path_local;
    String Thumb_path1, Thumb_path_2, Thumb_path3, thumb_path4, Image_path4filter;
    int Array_postion = 0;
    RelativeLayout Rlv_popupp;
    //rlvpopupup

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_local_image_3dcube);
        Btn_upload = (Button) findViewById(R.id.Buttn_upload);
        Btn_upload_popup = (Button) findViewById(R.id.Buttn_uploadd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Btn_cross = (ImageView) findViewById(R.id.img_cross);
        Img_cross = (ImageView) findViewById(R.id.img_crosss);
        Rlv_popupp = (RelativeLayout) findViewById(R.id.rlv_big_popup);
        session = new Session_manager(getApplicationContext());
        Edttext_caption = (EditText) findViewById(R.id.EdtTxt_caption);
        HashMap<String, String> user = session.getUserDetails();
        accestoken = user.get(session.Acess_Token);
        img_filter = (ImageView) findViewById(R.id.img_filter);
        Btn_skip = (Button) findViewById(R.id.Btn_skip);
        Btn_addfilter = (Button) findViewById(R.id.Btn_filter);
        device_id = user.get(session.device_id);
        mpager2 = (viewpager) findViewById(R.id.container);
        Intent intent = getIntent();
        Bundle b = getIntent().getExtras();
        callbackManager = CallbackManager.Factory.create();
        Btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "Turnstr");
//                if (mediaStorageDir.isDirectory()) {
//                    mediaStorageDir.delete();
//                }
                deleteRecursive(mediaStorageDir);
                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/Turnstrr/");
                deleteRecursive(dir);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Turnstr_thumbnail");
                deleteRecursive(myDir);
                Intent i1 = new Intent(Local_image_3dcube.this, DgCamActivity.class);
                Local_image_3dcube.this.startActivity(i1);
                finish();
            }
        });
        Img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rlv_popupp.setVisibility(View.GONE);
            }
        });
        Btn_upload_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Caption = Edttext_caption.getText().toString();
                new UploadFileToServer().execute();
            }
        });
        mPager = (viewpager) findViewById(R.id.containerr);

        if (b != null) {
            arr = (ArrayList<String>) b.getStringArrayList("array_list");
            System.out.println(arr);
        }

        try {
            mpager2.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            mPager.setPageTransformer(true, TRANSFORM_CLASSES.get(0).clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mAdapter = new PageAdapter(getFragmentManager());
        mPager.setAdapter(mAdapter);
        mAdapter_popup = new PageAdapter_popup(getFragmentManager());
        mpager2.setAdapter(mAdapter_popup);
        //     new Bitmap_operation().execute();
        img_1 = arr.get(0);
////
        Str_img2 = arr.get(1);
////
////
        Str_img3 = arr.get(2);
        Str_img4 = arr.get(3);
////
        if (img_1.endsWith(".mp4")) {
            try {
                Bitmap Bmp = getVidioThumbnail(img_1);
                Thumb_path1 = SaveImage(Bmp);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
////
////
        }
        if (Str_img2.endsWith(".mp4")) {
            try {
                Bitmap Bmp = getVidioThumbnail(Str_img2);
                Thumb_path_2 = SaveImage(Bmp);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
////
        }
        if (Str_img3.endsWith(".mp4")) {
            try {
                Bitmap Bmp = getVidioThumbnail(Str_img3);
                Thumb_path3 = SaveImage(Bmp);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
////
        }
        if (Str_img4.endsWith(".mp4")) {
            try {
                Bitmap Bmp = getVidioThumbnail(Str_img4);
                thumb_path4 = SaveImage(Bmp);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
////
        }
        Url_path_local = arr.get(0);
        Image_path4filter = arr.get(0);
        /////////////////////////////

        //mPager.setOffscreenPageLimit(4);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //  Url_path_local=arr.get(position);
            }

            @Override
            public void onPageSelected(int position) {
                //Url_path_local=arr.get(position);
                //Positionnn =position;
                Image_path4filter = arr.get(position);
                Array_postion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Url_path_local=arr.get(state);
            }
        });
        Btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rlv_popupp.setVisibility(View.VISIBLE);
                //mPager.setBackgroundColor(R.drawable.aviary_toast_background);
                //initiatePopupWindow();
            }
        });
        Btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Caption = Edttext_caption.getText().toString();
//                String[] tools = new String[]{"ENHANCE", "EFFECTS",
//                        "TILT_SHIFT", "CROP",
//                        "BRIGHTNESS", "CONTRAST", "SATURATION", "COLORTEMP",
//                        "SHARPNESS", "COLOR_SPLASH",
//                };
//                Intent newIntent = new Intent(Local_image_3dcube.this, FeatherActivity.class);
//                newIntent.setData(Uri.parse(arr.get(0)));
//                newIntent.putExtra(Constants.EXTRA_TOOLS_LIST, tools);
//                // extra-api-key-secret
//                newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET,
//                        "889d9116-68e9-4ba0-8c54-9bc888c8d90a");
//                startActivityForResult(newIntent, 1);
             //   new UploadFileToServer().execute();
                Rlv_popupp.setVisibility(View.VISIBLE);
            }
        });
        Btn_addfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Caption = Edttext_caption.getText().toString();
                String[] tools = new String[]{"ENHANCE", "EFFECTS",
                        "TILT_SHIFT", "CROP",
                        "BRIGHTNESS", "CONTRAST", "SATURATION", "COLORTEMP",
                        "SHARPNESS", "COLOR_SPLASH",
                };
                Intent newIntent = new Intent(Local_image_3dcube.this, FeatherActivity.class);
                newIntent.setData(Uri.parse(Image_path4filter));
                newIntent.putExtra(Constants.EXTRA_TOOLS_LIST, tools);
                // extra-api-key-secret
                newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET,
                        "889d9116-68e9-4ba0-8c54-9bc888c8d90a");
                startActivityForResult(newIntent, 1);
            }
        });

    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
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

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
            pDialog = new ProgressDialog(Local_image_3dcube.this);
            pDialog.setMessage("Uploading...");
            pDialog.setCancelable(false);
            pDialog.show();
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


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constant.upload_image);
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

                File sourceFile1 = new File(img_1);
                File sourceFile2 = new File(Str_img2);
                File sourceFile3 = new File(Str_img3);
                File sourceFile4 = new File(Str_img4);

                // Adding file data to http body
                try {
                    if (img_1.endsWith(".mp4")) {
                        File Thumb1 = new File(Thumb_path1);
                        entity.addPart("image1", new FileBody(sourceFile1));
                        entity.addPart("videoimage1", new FileBody(Thumb1));
                    } else {
                        entity.addPart("image1", new FileBody(sourceFile1));
                    }
                    if (Str_img2.endsWith(".mp4")) {
                        File Thumb2 = new File(Thumb_path_2);
                        entity.addPart("image2", new FileBody(sourceFile2));
                        entity.addPart("videoimage2", new FileBody(Thumb2));
                    } else {
                        entity.addPart("image2", new FileBody(sourceFile2));
                    }
                    if (Str_img3.endsWith(".mp4")) {
                        File Thumb3 = new File(Thumb_path3);
                        entity.addPart("image3", new FileBody(sourceFile3));
                        entity.addPart("videoimage3", new FileBody(Thumb3));
                    } else {
                        entity.addPart("image3", new FileBody(sourceFile3));
                    }
                    if (Str_img4.endsWith(".mp4")) {
                        File Thumb4 = new File(thumb_path4);
                        entity.addPart("image4", new FileBody(sourceFile4));
                        entity.addPart("videoimage4", new FileBody(Thumb4));
                    } else {
                        entity.addPart("image4", new FileBody(sourceFile4));
                    }
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }

//                if (img_1.endsWith(".mp4")) {
//                    entity.addPart("image1", new FileBody(sourceFile1));
//                } else {
//                    entity.addPart("videoimage1", new FileBody(sourceFile1));
//                }
//                if (Str_img2.endsWith(".mp4")) {
//                    entity.addPart("image2", new FileBody(sourceFile2));
//                } else {
//                    entity.addPart("videoimage2", new FileBody(sourceFile2));
//                }
//                if (Str_img3.endsWith(".mp4")) {
//                    entity.addPart("image3", new FileBody(sourceFile3));
//                } else {
//                    entity.addPart("videoimage3", new FileBody(sourceFile3));
//                }
//                if (Str_img4.endsWith(".mp4")) {
//                    entity.addPart("image4", new FileBody(sourceFile4));
//                } else {
//                    entity.addPart("videoimage4", new FileBody(sourceFile4));
//                }

//                entity.addPart("image2", new FileBody(sourceFile2));
//                entity.addPart("image3", new FileBody(sourceFile3));
//                entity.addPart("image4", new FileBody(sourceFile4));
                entity.addPart("caption", new StringBody(Caption));

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
            pDialog.dismiss();
            progressBar.setVisibility(View.GONE);

            for (String a : arr_aviary) {
                Uri myUri = Uri.parse(a);
                new File(myUri.getPath()).delete();

            }
            File mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Turnstr");
//                if (mediaStorageDir.isDirectory()) {
//                    mediaStorageDir.delete();
//                }
            deleteRecursive(mediaStorageDir);
            File dir = new File(Environment.getExternalStorageDirectory()
                    + "/Turnstrr/");
            deleteRecursive(dir);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/Turnstr_thumbnail");
            deleteRecursive(myDir);
//            Intent i1 = new Intent(Local_image_3dcube.this,
//                    Camera_activity.class);
//            Toast.makeText(Local_image_3dcube.this, responseString, Toast.LENGTH_LONG).show();
//            //  i1.putCharSequenceArrayListExtra("array_list", Array_items);
//            startActivity(i1);
//            finish();
            initiatePopupWindow();
            super.onPostExecute(result);
        }

    }

    public class PlaceholderFragment extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};
        View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            view = inflater.inflate(R.layout.frame_videoview, container, false);  //Contains empty RelativeLayout

            final ImageView textViewPosition = (ImageView) view.findViewById(R.id.Imag_upload_view);
            TextureVideoView video_Vw = (TextureVideoView) view.findViewById(R.id.frame_video_view);


            //
            //ArrayUtils.reverse(items);
//            if(position==4){
//                Collections.reverse(Arrays.asList(items));
//            }
            //String url = items[position];
            String url = arr.get(position);
            // Image_path4filter = url;
            if (url.endsWith(".mp4")) {
                video_Vw.setVisibility(View.VISIBLE);
                textViewPosition.setVisibility(View.GONE);
//                    media_Controller = new MediaController(getActivity());
//                    dm = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//                    int height = dm.heightPixels;
//                    int width = dm.widthPixels;
//                    video_Vw.setMinimumWidth(width);
//                    video_Vw.setMinimumHeight(height);
//                    video_Vw.setMediaController(null);
                //video_Vw.setVolume(0f, 0f);
                Uri video = Uri.parse(url);
                //Uri video = Uri.parse(url);
                //  video_Vw.setVideoURI(video);
                // video_Vw.setOnPreparedListener(PreparedListener);
//                    video_Vw.setup(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//                    video_Vw.requestFocus();
//                    video_Vw.setFrameVideoViewListener(new FrameVideoViewListener() {
//                        @Override
//                        public void mediaPlayerPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//                        }
//
//                        @Override
//                        public void mediaPlayerPrepareFailed(MediaPlayer mediaPlayer, String error) {
//
//                        }
//                    });
                video_Vw.setVideoURI(video);
                // video_Vw.
                video_Vw.setMediaController(null);
                video_Vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mp) {
//                            startVideoPlayback();
//                            startVideoAnimation();
                        mp.setVolume(0f, 0f);
                        mp.start();
                        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                // video_Vw.setBackground(null);
                                return true;
                            }
                        });
                    }
                });
                video_Vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();

                        // video_Vw.animate().rotationBy(360.0f).setDuration(video_Vw.getDuration()).start();
                    }
                });
                // video_Vw.start();


            } else {

                video_Vw.setVisibility(View.GONE);
                textViewPosition.setVisibility(View.VISIBLE);
                File file_path = new File(url);

                try {


//                    if (model.contentEquals("GT-I9500")) {
//                        Picasso.with(Local_image_3dcube.this).load(file_path).placeholder(R.drawable.placeholderdevzillad).into(Img_view);
//                    } else {
//                    Uri uri = Uri.fromFile(file_path);
//                    Bitmap bmp = getDownsampledBitmap(Local_image_3dcube.this, uri, 100, 100);
//                    //  Bitmap getDownsampledBitmap(Context ctx, Uri uri, int targetWidth, int targetHeight) {
//                    Img_view.setImageBitmap(bmp);
                    //}

                    Picasso.with(Local_image_3dcube.this).load(file_path).placeholder(R.drawable.placeholderdevzillad).resize(400, 400).into(textViewPosition);
                    //
//                    ImageLoader img_loader =new ImageLoader(Local_image_3dcube.this);
//                    img_loader.DisplayImage(url, Img_view);
                } catch (java.lang.OutOfMemoryError error) {

                }
            }

            //textViewPosition.setBackgroundResource(COLORS[position]);
            return view;
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
            Positionnn = position;
            final PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    MediaPlayer.OnPreparedListener PreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer m) {
            try {
                if (m.isPlaying()) {
                    m.pause();
                    m.release();
                    m = new MediaPlayer();
                }
                m.setVolume(0f, 0f);
                m.setLooping(false);
                m.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static Bitmap getVidioThumbnail(String path) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
            if (bitmap != null) {
                return bitmap;
            }
        }
        // MediaMetadataRetriever is available on API Level 8 but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();
            final Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, path);
            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                bitmap = (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                final byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                if (bitmap == null) {
                    bitmap = (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
                }
            }
        } catch (Exception e) {
            bitmap = null;
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (final Exception ignored) {
            }
        }
        return bitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    private String SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Turnstr_thumbnail");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String fname = "Thumb-" + timeStamp + ".jpg";
        File file = new File(myDir, fname);
        String Thumbnail_path = file.getAbsolutePath();
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Thumbnail_path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // After getting the photo from the camera

        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1) {
            Toast.makeText(Local_image_3dcube.this, "Successfully Shared on your timeline", Toast.LENGTH_LONG).show();
        }
        // Toast.makeText()
        // After getting the image result from Aviary
        if (requestCode == 1) {
            Uri theURI = data.getData();
            //Toast.makeText(this, "Got Aviary!: " + theURI, Toast.LENGTH_SHORT).show();
            String stringUri;
            try {


                stringUri = theURI.toString();
                arr_aviary.add(stringUri);
                //Image_path4filter = arr.get(position);
                arr.set(Array_postion, stringUri);
                mAdapter = new PageAdapter(getFragmentManager());
                mPager.setAdapter(mAdapter);
                mPager.setCurrentItem(Array_postion);
                img_1 = arr.get(0);
                Str_img2 = arr.get(1);
                Str_img3 = arr.get(2);
                Str_img4 = arr.get(3);
            } catch (java.lang.NullPointerException e) {
                e.printStackTrace();
            }

            // mPager.
            // Array_postion = position;
        }
    }

    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            ImageView Img_back_popup, Img_facebook, ImgTwitter, ImgFlicker, Img_tumbler, ImgEmail;

            Button Btn_back, Btn_next;
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.turnstr_caption,
                    (ViewGroup) findViewById(R.id.popup_element));

            pwindo = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Img_facebook = (ImageView) layout.findViewById(R.id.Facebook_share);
            ImgTwitter = (ImageView) layout.findViewById(R.id.Twitter_share);
            ImgFlicker = (ImageView) layout.findViewById(R.id.imageView43);
            Img_tumbler = (ImageView) layout.findViewById(R.id.imageView33);
            ImgEmail = (ImageView) layout.findViewById(R.id.imageView44);
            Btn_back = (Button) layout.findViewById(R.id.button6);
            Btn_next = (Button) layout.findViewById(R.id.Buttn_uploadd);
            Img_back_popup = (ImageView) layout.findViewById(R.id.img_crosss);
            Img_back_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pwindo.dismiss();
                }
            });
            Btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            });
            Btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent int_home = new Intent(Local_image_3dcube.this, MainActivity.class);
                    startActivity(int_home);
                    finish();
                }
            });
            Img_facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    //LoginManager.getInstance().logInWithReadPermissions(Local_image_3dcube.this, Arrays.asList("public_profile", "publish_actions", "email", "user_birthday", "user_friends"));
                    LoginManager.getInstance().logInWithPublishPermissions(Local_image_3dcube.this, Arrays.asList("publish_actions"));

                    LoginManager.getInstance().registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {
                                    final AccessToken accessToken = loginResult.getAccessToken();

                                    GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                            //String email_fb = user.optString("email");
                                            // String Name_fb = user.optString("name");
                                            ShareLinkContent content = new ShareLinkContent.Builder()
                                                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                                                    .build();


                                            ShareApi.share(content, null);
                                        }
                                    }).executeAsync();
                                }

                                @Override
                                public void onCancel() {
                                    // App code
                                }

                                @Override
                                public void onError(FacebookException exception) {
                                    // App code
                                }
                            });
                }
            });
            ImgTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ImgFlicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            Img_tumbler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ImgEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setType("plain/text");
                    //sendIntent.setData(Uri.parse("test@gmail.com"));
                    sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@gmail.com"});
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my demo app :-)");
                    startActivity(sendIntent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PlaceholderFragment_popup extends Fragment {

        private static final String EXTRA_POSITION = "EXTRA_POSITION";
        //private  final int[] COLORS = new int[]{R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree, R.drawable.imgfour};
        View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);
            view = inflater.inflate(R.layout.frame_videoview, container, false);  //Contains empty RelativeLayout

            final ImageView textViewPosition = (ImageView) view.findViewById(R.id.Imag_upload_view);
            TextureVideoView video_Vw = (TextureVideoView) view.findViewById(R.id.frame_video_view);


            //
            //ArrayUtils.reverse(items);
//            if(position==4){
//                Collections.reverse(Arrays.asList(items));
//            }
            //String url = items[position];
            String url = arr.get(position);
            // Image_path4filter = url;
            if (url.endsWith(".mp4")) {
                video_Vw.setVisibility(View.VISIBLE);
                textViewPosition.setVisibility(View.GONE);
//                    media_Controller = new MediaController(getActivity());
//                    dm = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//                    int height = dm.heightPixels;
//                    int width = dm.widthPixels;
//                    video_Vw.setMinimumWidth(width);
//                    video_Vw.setMinimumHeight(height);
//                    video_Vw.setMediaController(null);
                //video_Vw.setVolume(0f, 0f);
                Uri video = Uri.parse(url);
                //Uri video = Uri.parse(url);
                //  video_Vw.setVideoURI(video);
                // video_Vw.setOnPreparedListener(PreparedListener);
//                    video_Vw.setup(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//                    video_Vw.requestFocus();
//                    video_Vw.setFrameVideoViewListener(new FrameVideoViewListener() {
//                        @Override
//                        public void mediaPlayerPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//                        }
//
//                        @Override
//                        public void mediaPlayerPrepareFailed(MediaPlayer mediaPlayer, String error) {
//
//                        }
//                    });
                video_Vw.setVideoURI(video);
                // video_Vw.
                video_Vw.setMediaController(null);
                video_Vw.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mp) {
//                            startVideoPlayback();
//                            startVideoAnimation();
                        mp.setVolume(0f, 0f);
                        mp.start();
                        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                            @Override
                            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                // video_Vw.setBackground(null);
                                return true;
                            }
                        });
                    }
                });
                video_Vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();

                        // video_Vw.animate().rotationBy(360.0f).setDuration(video_Vw.getDuration()).start();
                    }
                });
                // video_Vw.start();


            } else {

                video_Vw.setVisibility(View.GONE);
                textViewPosition.setVisibility(View.VISIBLE);
                File file_path = new File(url);

                try {


//                    if (model.contentEquals("GT-I9500")) {
//                        Picasso.with(Local_image_3dcube.this).load(file_path).placeholder(R.drawable.placeholderdevzillad).into(Img_view);
//                    } else {
//                    Uri uri = Uri.fromFile(file_path);
//                    Bitmap bmp = getDownsampledBitmap(Local_image_3dcube.this, uri, 100, 100);
//                    //  Bitmap getDownsampledBitmap(Context ctx, Uri uri, int targetWidth, int targetHeight) {
//                    Img_view.setImageBitmap(bmp);
                    //}

                    Picasso.with(Local_image_3dcube.this).load(file_path).placeholder(R.drawable.placeholderdevzillad).resize(400, 400).into(textViewPosition);
                    //
//                    ImageLoader img_loader =new ImageLoader(Local_image_3dcube.this);
//                    img_loader.DisplayImage(url, Img_view);
                } catch (java.lang.OutOfMemoryError error) {

                }
            }
            return view;
        }


    }

    private class PageAdapter_popup extends FragmentStatePagerAdapter {

        public PageAdapter_popup(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(PlaceholderFragment.EXTRA_POSITION, position);
            Positionnn = position;
            final PlaceholderFragment_popup fragment = new PlaceholderFragment_popup();
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    private Bitmap getDownsampledBitmap(Context ctx, Uri uri, int targetWidth, int targetHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options outDimens = getBitmapDimensions(uri);

            int sampleSize = calculateSampleSize(outDimens.outWidth, outDimens.outHeight, targetWidth, targetHeight);

            bitmap = downsampleBitmap(uri, sampleSize);

        } catch (Exception e) {
            //handle the exception(s)
        }

        return bitmap;
    }

    private BitmapFactory.Options getBitmapDimensions(Uri uri) throws FileNotFoundException, IOException {
        BitmapFactory.Options outDimens = new BitmapFactory.Options();
        outDimens.inJustDecodeBounds = true; // the decoder will return null (no bitmap)

        InputStream is = getContentResolver().openInputStream(uri);
        // if Options requested only the size will be returned
        BitmapFactory.decodeStream(is, null, outDimens);
        is.close();

        return outDimens;
    }

    private int calculateSampleSize(int width, int height, int targetWidth, int targetHeight) {
        int inSampleSize = 1;

        if (height > targetHeight || width > targetWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) targetHeight);
            final int widthRatio = Math.round((float) width / (float) targetWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private Bitmap downsampleBitmap(Uri uri, int sampleSize) throws FileNotFoundException, IOException {
        Bitmap resizedBitmap;
        BitmapFactory.Options outBitmap = new BitmapFactory.Options();
        outBitmap.inJustDecodeBounds = false; // the decoder will return a bitmap
        outBitmap.inSampleSize = sampleSize;

        InputStream is = getContentResolver().openInputStream(uri);
        resizedBitmap = BitmapFactory.decodeStream(is, null, outBitmap);
        is.close();

        return resizedBitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                matrix.setRotate(90);
                break;
            // return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {

            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

//            try {
            //bitmap.recycle();
//            }catch (java.lang.RuntimeException e){
//                e.printStackTrace();
//            }
            return bmRotated;

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

    }

    public String saveimage(Bitmap bmp, String file_name) {
// Find the SD Card path
        String path = null;
        File filepath = Environment.getExternalStorageDirectory();
        OutputStream output;
        // Create a new folder in SD Card

        File dir = new File(filepath.getAbsolutePath()
                + "/Turnstrr/");
        if (dir.exists() && dir.isDirectory()) {
            // do something here


            // Create a name for the saved image
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File file = new File(dir, timeStamp + ".png");

            // Show a toast message on successful save

            try {

                output = new FileOutputStream(file);
                path = file.getAbsolutePath();

                // Compress into png format image from 0% - 100%
                bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            dir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File file = new File(dir, timeStamp + ".png");

            // Show a toast message on successful save

            try {

                output = new FileOutputStream(file);
                path = file.getAbsolutePath();

                // Compress into png format image from 0% - 100%
                bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return path;
    }

    class Bitmap_operation extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //   mProgressHUD = ProgressHUD.show(HomeActivity.this, "Connecting", true, true, this);
            pDialog = new ProgressDialog(Local_image_3dcube.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < arr.size(); i++) {
                String Str_path = null;
                Str_path = arr.get(i);
                if (!Str_path.endsWith(".mp4")) {
                 /*   BitmapFactory.Options optionsoriginal = new BitmapFactory.Options();
                    optionsoriginal.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    optionsoriginal.inSampleSize = 0;
                    Bitmap bitmap_orizinal;
                    try {


                        bitmap_orizinal = BitmapFactory.decodeFile(Str_path, optionsoriginal);
                    } catch (java.lang.OutOfMemoryError e) {
                        e.printStackTrace();
                        optionsoriginal.inSampleSize = 3;
                        bitmap_orizinal = BitmapFactory.decodeFile(Str_path, optionsoriginal);
                    }
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(Str_path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap bmRotated = rotateBitmap(bitmap_orizinal, orientation);
                    //  cropToSquare_bitmap
                    Bitmap bit_big_image = cropToSquare(bmRotated);
                    // Bitmap bit_big_image = cropToSquare_bitmap(bitmap_orizinal);
                    // Bitmap bit_big_image = getResizedBitmap(bmRotated, 500, 500);
                    //thumbnails[position] = bit_big_image;
                    String squareimagepath = saveimage(bit_big_image, "Turnstrr");
                    arr.set(i, squareimagepath);*/
                    File file = new File(Str_path);


                    //Bitmap bitmap_orizinal= ImageResizer.resize(file, 400, 400);
                    Bitmap bitmap_orizinal = ImageResizer.rotate(file, ImageRotation.CW_90);
                    String squareimagepath = saveimage(bitmap_orizinal, "Turnstrr");
                    arr.set(i, squareimagepath);
                }
            }

            //////////////////////////////////////
            img_1 = arr.get(0);

            Str_img2 = arr.get(1);


            Str_img3 = arr.get(2);
            Str_img4 = arr.get(3);

            if (img_1.endsWith(".mp4")) {
                try {
                    Bitmap Bmp = getVidioThumbnail(img_1);
                    Thumb_path1 = SaveImage(Bmp);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }


            }
            if (Str_img2.endsWith(".mp4")) {
                try {
                    Bitmap Bmp = getVidioThumbnail(Str_img2);
                    Thumb_path_2 = SaveImage(Bmp);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

            }
            if (Str_img3.endsWith(".mp4")) {
                try {
                    Bitmap Bmp = getVidioThumbnail(Str_img3);
                    Thumb_path3 = SaveImage(Bmp);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

            }
            if (Str_img4.endsWith(".mp4")) {
                try {
                    Bitmap Bmp = getVidioThumbnail(Str_img4);
                    thumb_path4 = SaveImage(Bmp);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

            }
            Url_path_local = arr.get(0);
            Image_path4filter = arr.get(0);
            // FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, arr);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            mAdapter = new PageAdapter(getFragmentManager());
            mPager.setAdapter(mAdapter);
            mAdapter_popup = new PageAdapter_popup(getFragmentManager());
            mpager2.setAdapter(mAdapter_popup);

        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }

    public static Bitmap cropToSquare_bitmap(Bitmap bitmap) {
        Bitmap dstBmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        } else {

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        return dstBmp;
    }
}
