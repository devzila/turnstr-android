package Fragments;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.hardware.Camera.Size;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.example.Local_image_3dcube;
import com.ToxicBakery.viewpager.transforms.example.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Random;

import Image_resizer.ImageResizer;
import Session_handler.TinyDB;
import adapter.FragmentInterface;
import operations.ImageRotation;

public class Camera_fragment extends Fragment implements SurfaceHolder.Callback, FragmentInterface {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    private Camera.Size mPreviewSize;
    ImageView Imgvw1, imgvw2, imgvw3, imgvw4, Img_camera_click;
    private Display display;
    ArrayList<String> Array_items = new ArrayList<String>();
    Button Btn_next;
    ImageView img_cross1, imgcross2, img_cross3, imgcross4, Img_switch_camera;
    String Strfor_img1 = "niomage1";
    String Strfor_img2 = "noimag2";
    String Strfor_img3 = "noimage3";
    String Strfor_img4 = "noimage4";
    SharedPreferences sharedpreferences;
    String Value;
    MediaRecorder recorder;
    boolean recording = false;
    private int randomNum;
    String Str_video_status = "no_video";
    ProgressBar mProgressBar;
    private int progress = 0;
    private final int pBarMax = 60;
    int progressStatus;
    String Video_path;
    TinyDB tb;
    int CameraId;
    private Preview mPreview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_games, container, false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        display = ((WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE)).getDefaultDisplay();
        sharedpreferences = getActivity().getSharedPreferences("turnstrr", getActivity().MODE_PRIVATE);
        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) rootView.findViewById(R.id.camera_preview);
        Imgvw1 = (ImageView) rootView.findViewById(R.id.imageView21);
        imgvw2 = (ImageView) rootView.findViewById(R.id.imageView17);
        imgvw3 = (ImageView) rootView.findViewById(R.id.imageView19);
        imgvw4 = (ImageView) rootView.findViewById(R.id.imageView20);
        Img_switch_camera = (ImageView) rootView.findViewById(R.id.Switch_camera);
        img_cross1 = (ImageView) rootView.findViewById(R.id.imageView22);
        imgcross2 = (ImageView) rootView.findViewById(R.id.imageView23);
        img_cross3 = (ImageView) rootView.findViewById(R.id.imageView24);
        imgcross4 = (ImageView) rootView.findViewById(R.id.imageView25);
        Img_camera_click = (ImageView) rootView.findViewById(R.id.camera_click);
        Btn_next = (Button) rootView.findViewById(R.id.btn_Next);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_loading);
        // mPreview = new Preview(this.getActivity());
        //  camera = Camera.open( Camera.CameraInfo.CAMERA_FACING_BACK);
        // mPreview.setCamera(camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        tb = new TinyDB(getActivity());
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // recorder = new MediaRecorder();
        // initRecorder();
//        Img_camera_click.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        if (Value.contentEquals("video")) {
//                            Img_camera_click.setImageResource(R.drawable.video_red_active);
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Img_camera_click.setImageResource(R.drawable.video_red_normal);
//                        break;
//                }
//                return true;
//            }
//        });
        Img_camera_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (Value.contentEquals("photto_camera")) {
                    if (Array_items.size() < 4) {

                        camera.takePicture(null, null, mPicture);
                        Img_camera_click.setImageResource(R.drawable.camera_blue_active);
                    } else {
                        Toast.makeText(getActivity(), "your post is ready for upload", Toast.LENGTH_LONG).show();
                    }
                } else if (Value.contentEquals("video")) {
                    if (Array_items.size() < 4) {


                        if (Str_video_status.contentEquals("no_video")) {
                            prepareMediaRecorder();
                            recorder.start();
                            Str_video_status = "is_recording";
                            Img_camera_click.setImageResource(R.drawable.video_red_active);
                            //startAnimation();
                            new AsyncTask<Void, Integer, Void>() {
                                @Override
                                protected void onPreExecute() {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    super.onPreExecute();
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    progressStatus = 0;

                                    while (progressStatus < 5000) {
                                        progressStatus++;
                                        publishProgress(progressStatus);
                                        try {
                                            Thread.sleep(1);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void onProgressUpdate(Integer... values) {
                                    mProgressBar.setProgress(values[0]);

                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    mProgressBar.setVisibility(View.GONE);
                                    if (Str_video_status.contentEquals("is_recording")) {
//
                                        recorder.stop();
                                        Str_video_status = "no_video";
                                        Img_camera_click.setImageResource(R.drawable.video_red_normal);
                                        //  Video_path = getOutputMedia_video_File().getAbsolutePath();
                                        // Bitmap btm_thumnail= ThumbnailUtils.createVideoThumbnail(Video_path, MediaStore.Video.Thumbnails.MICRO_KIND);

//                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                    Imgvw1.setImageBitmap(btm_thumnail);

                                        if (Array_items.size() < 4) {
                                            Array_items.add(Video_path);
                                            tb.putListString("list", Array_items);
                                            if (Strfor_img1.contentEquals("niomage1")) {
                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                Imgvw1.setImageBitmap(btm_thumnail);

                                                Strfor_img1 = "setimage1";
                                                img_cross1.setVisibility(View.VISIBLE);
                                                return;
                                                // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                                            } else if (Strfor_img2.contentEquals("noimag2")) {
                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                imgvw2.setImageBitmap(btm_thumnail);


                                                Strfor_img2 = "setimage2";
                                                imgcross2.setVisibility(View.VISIBLE);
                                                return;
                                                //  Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
                                            } else if (Strfor_img3.contentEquals("noimage3")) {
                                                try {
                                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                    imgvw3.setImageBitmap(btm_thumnail);

                                                    Strfor_img3 = "setimage3";
                                                    img_cross3.setVisibility(View.VISIBLE);
                                                    return;
                                                } catch (OutOfMemoryError e) {
                                                    e.printStackTrace();
                                                }

                                                // Picasso.with(getActivity()).load(pictureFile).into(imgvw3);
                                            } else if (Strfor_img4.contentEquals("noimage4")) {
                                                try {
                                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                    imgvw4.setImageBitmap(btm_thumnail);

                                                    Strfor_img4 = "setimage4";
                                                    imgcross4.setVisibility(View.VISIBLE);
                                                    return;
                                                } catch (OutOfMemoryError e) {
                                                    e.printStackTrace();
                                                }
                                                // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "your post is ready for upload", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    super.onPostExecute(aVoid);
                                }
                            }.execute();
                        } else if (Str_video_status.contentEquals("is_recording")) {
                            try {
                                recorder.stop();
                                Str_video_status = "no_video";
                                Img_camera_click.setImageResource(R.drawable.video_red_normal);
                                progressStatus = 0;
                                Img_camera_click.setImageResource(R.drawable.video_red_normal);
                                Video_path = getOutputMedia_video_File().getAbsolutePath();
                                Bitmap btm_thumnail_stop = ThumbnailUtils.createVideoThumbnail(Video_path, MediaStore.Video.Thumbnails.MICRO_KIND);

//                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                Imgvw1.setImageBitmap(btm_thumnail_stop);
                                mProgressBar.setVisibility(View.GONE);
                                if (Str_video_status.contentEquals("is_recording")) {
//
                                    recorder.stop();
                                    Str_video_status = "no_video";
                                    Img_camera_click.setImageResource(R.drawable.video_red_normal);
                                    // Video_path = getOutputMedia_video_File().getAbsolutePath();

                                    if (Array_items.size() < 4) {
                                        Array_items.add(Video_path);
                                        if (Strfor_img1.contentEquals("niomage1")) {
                                            Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                            Imgvw1.setImageBitmap(btm_thumnail);

                                            Strfor_img1 = "setimage1";
                                            img_cross1.setVisibility(View.VISIBLE);
                                            return;
                                            // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                                        } else if (Strfor_img2.contentEquals("noimag2")) {
                                            Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                            imgvw2.setImageBitmap(btm_thumnail);


                                            Strfor_img2 = "setimage2";
                                            imgcross2.setVisibility(View.VISIBLE);
                                            return;
                                            //  Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
                                        } else if (Strfor_img3.contentEquals("noimage3")) {
                                            try {
                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                imgvw3.setImageBitmap(btm_thumnail);

                                                Strfor_img3 = "setimage3";
                                                img_cross3.setVisibility(View.VISIBLE);
                                                return;
                                            } catch (OutOfMemoryError e) {
                                                e.printStackTrace();
                                            }

                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw3);
                                        } else if (Strfor_img4.contentEquals("noimage4")) {
                                            try {
                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                imgvw4.setImageBitmap(btm_thumnail);

                                                Strfor_img4 = "setimage4";
                                                imgcross4.setVisibility(View.VISIBLE);
                                                return;
                                            } catch (OutOfMemoryError e) {
                                                e.printStackTrace();
                                            }
                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "your post is ready for upload", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        Toast.makeText(getActivity(), "your post is ready for upload", Toast.LENGTH_LONG).show();


                    }
                }
            }
        });
        Img_switch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.stopPreview();
                } catch (java.lang.RuntimeException e) {
                    e.printStackTrace();
                }

                camera.release();
                if (CameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    CameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    CameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
                Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
                // You need to choose the most appropriate previewSize for your app
                //  Camera.Size optimalSize = parameters.getSupportedPreviewSizes().get(0);
                // previewSize.width = 720;
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);


                // start preview with new settings
                camera.setParameters(parameters);

                // Set the holder size based on the aspect ratio
//                int size = Math.min(display.getWidth(), display.getHeight());
//                double ratio = (double) previewSize.width / previewSize.height;
//
//                surfaceHolder.setFixedSize((int) (size * ratio), size);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.setDisplayOrientation(90);

                camera.startPreview();
            }
        });
        Btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Array_items.size() == 4) {
                    Intent i1 = new Intent(getActivity(),
                            Local_image_3dcube.class);
                    i1.putExtra("array_list", Array_items);
                    //  i1.putCharSequenceArrayListExtra("array_list", Array_items);
                    startActivity(i1);
                    camera.stopPreview();
                } else {
                    Toast.makeText(getActivity(), "Please select 4 images", Toast.LENGTH_LONG).show();
                }
            }
        });
        img_cross1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imgvw1.setImageBitmap(null);
                Imgvw1.setImageResource(android.R.drawable.ic_menu_gallery);
                img_cross1.setVisibility(View.INVISIBLE);
                try {

                    Array_items.remove(0);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                tb.putListString("list", Array_items);
                Strfor_img1 = "niomage1";

                //Imgvw1
            }
        });
        imgcross4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgvw4.setImageBitmap(null);
                imgvw4.setImageResource(android.R.drawable.ic_menu_gallery);
                imgcross4.setVisibility(View.INVISIBLE);
                try {

                    Array_items.remove(3);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                tb.putListString("list", Array_items);
                Strfor_img4 = "noimage4";

                //Imgvw1
            }
        });
        imgcross2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgvw2.setImageBitmap(null);
                imgvw2.setImageResource(android.R.drawable.ic_menu_gallery);
                imgcross2.setVisibility(View.INVISIBLE);
                try {

                    Array_items.remove(1);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                tb.putListString("list", Array_items);
                Strfor_img2 = "noimag2";

                //Imgvw1
            }
        });
        img_cross3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgvw3.setImageBitmap(null);
                imgvw3.setImageResource(android.R.drawable.ic_menu_gallery);
                img_cross3.setVisibility(View.INVISIBLE);
                try {

                    Array_items.remove(2);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                tb.putListString("list", Array_items);
                Strfor_img3 = "noimage3";
                //Imgvw1
            }
        });
        return rootView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //camera = Camera.open();

        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
//                Camera.Size optimalSize = null;
//                optimalSize.width=  1280;
//                optimalSize.height= 720;
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            // You need to choose the most appropriate previewSize for your app
//                Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(0);
//                // previewSize.width = 720;
//                parameters.setPreviewSize(previewSize.width, previewSize.height);


            // start preview with new settings
            camera.setParameters(parameters);
            CameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } catch (java.lang.RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Fail to connect to camera service", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            try {
                //camera.setPreviewDisplay(surfaceHolder);
                android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(0, info);
                int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 180;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 270;
                        break;
                }

                int result;
                //int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                // do something for phones running an SDK before lollipop
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    result = (info.orientation + degrees) % 360;
                    result = (360 - result) % 360; // compensate the mirror
                } else { // back-facing
                    result = (info.orientation - degrees + 360) % 360;
                }


                Camera.Parameters parameters = camera.getParameters();
                // parameters.setPreviewSize(width, height);
                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
                Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
//                Camera.Size optimalSize = null;
//                optimalSize.width=  1280;
//                optimalSize.height= 720;
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
                //  parameters.setPictureSize(250,250);
                // You need to choose the most appropriate previewSize for your app
//                Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(0);
//                // previewSize.width = 720;
//                parameters.setPreviewSize(previewSize.width, previewSize.height);


                // start preview with new settings
                camera.setParameters(parameters);

                // Set the holder size based on the aspect ratio
//                int size = Math.min(display.getWidth(), display.getHeight());
//                double ratio = (double) optimalSize.width / optimalSize.height;
//
//                surfaceHolder.setFixedSize((int) (size * ratio), size);
                camera.setPreviewDisplay(surfaceHolder);
                camera.setDisplayOrientation(result);

                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // camera.stopPreview();
//        if (camera != null) {
//            camera.release();
//            camera = null;
//        }
    }

    @Override
    public void fragmentBecameVisible() {

        Value = sharedpreferences.getString("Photo", "");
        if (Value.contentEquals("photto_camera")) {
            try {
                Img_camera_click.setImageResource(R.drawable.camera_blue_normal);
            } catch (java.lang.OutOfMemoryError e) {
                e.printStackTrace();
            }

        } else if (Value.contentEquals("video")) {
            try {
                Img_camera_click.setImageResource(R.drawable.video_red_normal);
            } catch (java.lang.OutOfMemoryError e) {
                e.printStackTrace();
            }

        }

        Array_items = tb.getListString("list");
        // new Bitmap_operation(myinterface).execute();
        if (Array_items.size() == 0) {
            Imgvw1.setImageResource(android.R.drawable.ic_menu_gallery);
            imgvw2.setImageResource(android.R.drawable.ic_menu_gallery);
            imgvw3.setImageResource(android.R.drawable.ic_menu_gallery);
            imgvw4.setImageResource(android.R.drawable.ic_menu_gallery);


            img_cross1.setVisibility(View.INVISIBLE);
            imgcross2.setVisibility(View.INVISIBLE);
            img_cross3.setVisibility(View.INVISIBLE);
            imgcross4.setVisibility(View.INVISIBLE);
            Strfor_img1 = "niomage1";
            Strfor_img2 = "noimag2";
            Strfor_img3 = "noimage3";
            Strfor_img4 = "noimage4";
        }
        if (Array_items.size() == 1) {
            try {
                Imgvw1.setImageBitmap(null);
                imgvw2.setImageResource(android.R.drawable.ic_menu_gallery);
                imgvw3.setImageResource(android.R.drawable.ic_menu_gallery);
                imgvw4.setImageResource(android.R.drawable.ic_menu_gallery);
                Strfor_img1 = "niomage1";
                Strfor_img2 = "noimag2";
                Strfor_img3 = "noimage3";
                Strfor_img4 = "noimage4";

                img_cross1.setVisibility(View.INVISIBLE);
                imgcross2.setVisibility(View.INVISIBLE);
                img_cross3.setVisibility(View.INVISIBLE);
                imgcross4.setVisibility(View.INVISIBLE);
                String path1 = Array_items.get(0);
                if (path1.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path1);
                    Imgvw1.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inSampleSize = 16;
                    Bitmap bitmap = BitmapFactory.decodeFile(path1, options);
                    Imgvw1.setImageBitmap(bitmap);
                }


                Strfor_img1 = "setimage1";
                img_cross1.setVisibility(View.VISIBLE);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } else if (Array_items.size() == 2) {
            try {

                Imgvw1.setImageBitmap(null);
                imgvw2.setImageBitmap(null);
                imgvw3.setImageResource(android.R.drawable.ic_menu_gallery);
                imgvw4.setImageResource(android.R.drawable.ic_menu_gallery);
                Strfor_img1 = "niomage1";
                Strfor_img2 = "noimag2";
                Strfor_img3 = "noimage3";
                Strfor_img4 = "noimage4";
                img_cross1.setVisibility(View.INVISIBLE);
                imgcross2.setVisibility(View.INVISIBLE);
                img_cross3.setVisibility(View.INVISIBLE);
                imgcross4.setVisibility(View.INVISIBLE);
                String path1 = Array_items.get(0);
                if (path1.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path1);
                    Imgvw1.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inSampleSize = 16;
                    Bitmap bitmap = BitmapFactory.decodeFile(path1, options);
                    Imgvw1.setImageBitmap(bitmap);
                }

                Strfor_img1 = "setimage1";
                img_cross1.setVisibility(View.VISIBLE);

//////////////////////////////////////////////////////////////////////
                imgcross2.setVisibility(View.VISIBLE);
                String path2 = Array_items.get(1);
                if (path2.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path2);
                    imgvw2.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options2.inSampleSize = 16;
                    Bitmap bitmap2 = BitmapFactory.decodeFile(path2, options2);
                    imgvw2.setImageBitmap(bitmap2);
                }

                Strfor_img2 = "setimage2";
                imgcross2.setVisibility(View.VISIBLE);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } else if (Array_items.size() == 3) {
            try {
                Imgvw1.setImageBitmap(null);
                imgvw2.setImageBitmap(null);
                imgvw3.setImageBitmap(null);
                imgvw4.setImageResource(android.R.drawable.ic_menu_gallery);
                Strfor_img1 = "niomage1";
                Strfor_img2 = "noimag2";
                Strfor_img3 = "noimage3";
                Strfor_img4 = "noimage4";
                img_cross1.setVisibility(View.INVISIBLE);
                imgcross2.setVisibility(View.INVISIBLE);
                img_cross3.setVisibility(View.INVISIBLE);
                imgcross4.setVisibility(View.INVISIBLE);
                String path1 = Array_items.get(0);
                if (path1.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path1);
                    Imgvw1.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inSampleSize = 16;
                    Bitmap bitmap = BitmapFactory.decodeFile(path1, options);
                    Imgvw1.setImageBitmap(bitmap);
                }

                Strfor_img1 = "setimage1";
                img_cross1.setVisibility(View.VISIBLE);

//////////////////////////////////////////////////////////////////////
                imgcross2.setVisibility(View.VISIBLE);
                String path2 = Array_items.get(1);

                if (path2.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path2);
                    imgvw2.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options2.inSampleSize = 16;
                    Bitmap bitmap2 = BitmapFactory.decodeFile(path2, options2);
                    imgvw2.setImageBitmap(bitmap2);
                }


                Strfor_img2 = "setimage2";
                imgcross2.setVisibility(View.VISIBLE);

/////////////////////////////////////////////////////////////////////////////
                String path3 = Array_items.get(2);
                if (path3.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path3);
                    imgvw3.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options3 = new BitmapFactory.Options();
                    options3.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options3.inSampleSize = 16;
                    Bitmap bitmap3 = BitmapFactory.decodeFile(path3, options3);
                    imgvw3.setImageBitmap(bitmap3);
                }


                Strfor_img3 = "setimage3";
                img_cross3.setVisibility(View.VISIBLE);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        } else if (Array_items.size() == 4) {
            try {
                Imgvw1.setImageBitmap(null);
                imgvw2.setImageBitmap(null);
                imgvw3.setImageBitmap(null);
                imgvw4.setImageBitmap(null);
                Strfor_img1 = "niomage1";
                Strfor_img2 = "noimag2";
                Strfor_img3 = "noimage3";
                Strfor_img4 = "noimage4";
                img_cross1.setVisibility(View.INVISIBLE);
                imgcross2.setVisibility(View.INVISIBLE);
                img_cross3.setVisibility(View.INVISIBLE);
                imgcross4.setVisibility(View.INVISIBLE);
                String path1 = Array_items.get(0);
                if (path1.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path1);
                    Imgvw1.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inSampleSize = 16;
                    Bitmap bitmap = BitmapFactory.decodeFile(path1, options);
                    Imgvw1.setImageBitmap(bitmap);
                }

                Strfor_img1 = "setimage1";
                img_cross1.setVisibility(View.VISIBLE);

//////////////////////////////////////////////////////////////////////
                imgcross2.setVisibility(View.VISIBLE);
                String path2 = Array_items.get(1);
                if (path2.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path2);
                    imgvw2.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                    options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options2.inSampleSize = 16;
                    Bitmap bitmap2 = BitmapFactory.decodeFile(path2, options2);
                    imgvw2.setImageBitmap(bitmap2);
                }


                Strfor_img2 = "setimage2";
                imgcross2.setVisibility(View.VISIBLE);

/////////////////////////////////////////////////////////////////////////////
                String path3 = Array_items.get(2);
                if (path3.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path3);
                    imgvw3.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options3 = new BitmapFactory.Options();
                    options3.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options3.inSampleSize = 16;
                    Bitmap bitmap3 = BitmapFactory.decodeFile(path3, options3);
                    imgvw3.setImageBitmap(bitmap3);
                }


                Strfor_img3 = "setimage3";
                img_cross3.setVisibility(View.VISIBLE);

///////////////////////////////////////////////////////////////////////////
                String path4 = Array_items.get(3);
                if (path4.endsWith(".mp4")) {
                    Bitmap btm_thumnail = getVidioThumbnail(path4);
                    imgvw4.setImageBitmap(btm_thumnail);
                } else {
                    BitmapFactory.Options options4 = new BitmapFactory.Options();
                    options4.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options4.inSampleSize = 16;
                    Bitmap bitmap4 = BitmapFactory.decodeFile(path4, options4);
                    imgvw4.setImageBitmap(bitmap4);
                }


                Strfor_img4 = "setimage4";
                imgcross4.setVisibility(View.VISIBLE);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        System.out.println("photo..." + Array_items.size());
        System.out.println(Value);
        // camera.stopPreview();
//        camera = Camera.open();
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();


            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                try {
                    fos.write(data);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                fos.close();
                camera.startPreview();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }


            String path = pictureFile.getAbsolutePath();


            if (Array_items.size() < 4) {
//                BitmapFactory.Options optionsoriginal = new BitmapFactory.Options();
//                optionsoriginal.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                Bitmap bitmap_orizinal;
//                try {
//
//                    optionsoriginal.inSampleSize = 2;
//                    bitmap_orizinal = BitmapFactory.decodeFile(path, optionsoriginal);
//                } catch (java.lang.OutOfMemoryError e) {
//                    e.printStackTrace();
//                    optionsoriginal.inSampleSize = 14;
//                    bitmap_orizinal = BitmapFactory.decodeFile(path, optionsoriginal);
//                }
//                ExifInterface exif = null;
//                try {
//                    exif = new ExifInterface(path);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                        ExifInterface.ORIENTATION_UNDEFINED);
                File file = new File(path);
                Bitmap bmRotated = ImageResizer.rotate(file, ImageRotation.CW_90);
                // Bitmap bmRotated = rotateBitmap(bitmap_orizinal, orientation);
                //Bitmap bit_big_image = cropToSquare(bmRotated);
                // Bitmap bit_big_image = getResizedBitmap(bmRotated, 500, 500);
                //thumbnails[position] = bit_big_image;
                String squareimagepath = saveimage(bmRotated, "turnstr");

                Array_items.add(squareimagepath);
                Img_camera_click.setImageResource(R.drawable.camera_blue_normal);
                tb.putListString("list", Array_items);
                if (Strfor_img1.contentEquals("niomage1")) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    options.inSampleSize = 14;
                    try {
//                        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//                        ExifInterface exif = null;
//                        try {
//                            exif = new ExifInterface(path);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                ExifInterface.ORIENTATION_UNDEFINED);
//                        Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                        Imgvw1.setImageBitmap(bmRotated);


                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    Strfor_img1 = "setimage1";
                    img_cross1.setVisibility(View.VISIBLE);
                    return;
                    // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                } else if (Strfor_img2.contentEquals("noimag2")) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    options.inSampleSize = 14;
                    try {

//                          Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//                        ExifInterface exif = null;
//                        try {
//                            exif = new ExifInterface(path);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                ExifInterface.ORIENTATION_UNDEFINED);
//                        Bitmap bmRotated = rotateBitmap(bitmap, orientation);

                        imgvw2.setImageBitmap(bmRotated);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                    Strfor_img2 = "setimage2";
                    imgcross2.setVisibility(View.VISIBLE);
                    return;
                    //  Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
                } else if (Strfor_img3.contentEquals("noimage3")) {
                    try {
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                        options.inSampleSize = 14;
                        try {
//                              Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//                            ExifInterface exif = null;
//                            try {
//                                exif = new ExifInterface(path);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//                            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                            imgvw3.setImageBitmap(bmRotated);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }

                        Strfor_img3 = "setimage3";
                        img_cross3.setVisibility(View.VISIBLE);
                        return;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                    // Picasso.with(getActivity()).load(pictureFile).into(imgvw3);
                } else if (Strfor_img4.contentEquals("noimage4")) {
                    try {
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                        options.inSampleSize = 4;
                        try {
//                               Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//                            ExifInterface exif = null;
//                            try {
//                                exif = new ExifInterface(path);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//                            Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                            imgvw4.setImageBitmap(bmRotated);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }

                        Strfor_img4 = "setimage4";
                        imgcross4.setVisibility(View.VISIBLE);
                        return;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                    // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
                }
            } else {
                Toast.makeText(getActivity(), "your post is ready for upload", Toast.LENGTH_LONG).show();
            }
        }
    };


    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Turnstr");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Turnstr", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private static File getOutputMedia_video_File() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Turnstr");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Turnstr", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "video" + timeStamp + ".mp4");

        return mediaFile;
    }

    public class FileReader {

        public String getFileAsString(File file) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            StringBuffer sb = new StringBuffer();
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DataInputStream(bis);

                while (dis.available() != 0) {
                    sb.append(dis.readLine() + "\n");
                }
                fis.close();
                bis.close();
                dis.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            // java.lang.RuntimeException
            try {
                camera.stopPreview();
                camera.release();
            } catch (java.lang.RuntimeException e) {
                e.printStackTrace();
            }

        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    private boolean prepareMediaRecorder() {
        // camera = getCameraInstance();
        recorder = new MediaRecorder();

        camera.unlock();
        recorder.setCamera(camera);

        //  recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//       // mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        Video_path = getOutputMedia_video_File().getAbsolutePath();
        recorder.setOutputFile(Video_path);
        //Array_items.add(Video_path);
        recorder.setMaxDuration(60000); // Set max duration 60 sec.
        recorder.setMaxFileSize(5000000); // Set max file size 5M
        recorder.setOrientationHint(90);
        recorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

        try {
            recorder.prepare();
            //recorder.start();


        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    private void releaseMediaRecorder() {
        if (recorder != null) {
            recorder.reset();   // clear recorder configuration
            recorder.release(); // release the recorder object
            recorder = null;
            camera.lock();           // lock camera for later use
        }
    }

    private Camera getCameraInstance() {
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
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
            Toast.makeText(getActivity(), "Image Saved to SD Card",
                    Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Image Saved to SD Card",
                    Toast.LENGTH_SHORT).show();
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

    class Preview extends ViewGroup implements SurfaceHolder.Callback {
        private final String TAG = "Preview";
        // SurfaceView mSurfaceView;
        SurfaceHolder mHolder;
        Size mPreviewSize;
        List<Size> mSupportedPreviewSizes;
        Camera mCamera;
        boolean mSurfaceCreated = false;

        Preview(Context context) {
            super(context);
            surfaceView = new SurfaceView(context);
            addView(surfaceView);
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = surfaceView.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void setCamera(Camera camera) {
            mCamera = camera;
            if (mCamera != null) {
                mSupportedPreviewSizes = mCamera.getParameters()
                        .getSupportedPreviewSizes();
                if (mSurfaceCreated) requestLayout();
            }
        }

        public void switchCamera(Camera camera) {
            setCamera(camera);
            try {
                camera.setPreviewDisplay(mHolder);
            } catch (IOException exception) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // We purposely disregard child measurements because act as a
            // wrapper to a SurfaceView that centers the camera preview instead
            // of stretching it.
            final int width = resolveSize(getSuggestedMinimumWidth(),
                    widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(),
                    heightMeasureSpec);
            setMeasuredDimension(width, height);
            if (mSupportedPreviewSizes != null) {
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
                        height);
            }
            if (mCamera != null) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                mCamera.setParameters(parameters);
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (getChildCount() > 0) {
                final View child = getChildAt(0);
                final int width = r - l;
                final int height = b - t;
                int previewWidth = width;
                int previewHeight = height;
                if (mPreviewSize != null) {
                    previewWidth = mPreviewSize.width;
                    previewHeight = mPreviewSize.height;
                }
                // Center the child SurfaceView within the parent.
                if (width * previewHeight > height * previewWidth) {
                    final int scaledChildWidth = previewWidth * height
                            / previewHeight;
                    child.layout((width - scaledChildWidth) / 2, 0,
                            (width + scaledChildWidth) / 2, height);
                } else {
                    final int scaledChildHeight = previewHeight * width
                            / previewWidth;
                    child.layout(0, (height - scaledChildHeight) / 2, width,
                            (height + scaledChildHeight) / 2);
                }
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where
            // to draw.
            try {
                if (mCamera != null) {
                    mCamera.setPreviewDisplay(holder);
                }
            } catch (IOException exception) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
            if (mPreviewSize == null) requestLayout();
            mSurfaceCreated = true;
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        }

        private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) w / h;
            if (sizes == null)
                return null;
            Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;
            int targetHeight = h;
            // Try to find an size match aspect ratio and size
            for (Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
            // Cannot find the one match the aspect ratio, ignore the requirement
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }
            return optimalSize;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            requestLayout();
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
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


}
