package dgcam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ToxicBakery.viewpager.transforms.example.Local_image_3dcube;
import com.ToxicBakery.viewpager.transforms.example.R;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Image_resizer.ImageResizer;
import it.sephiroth.android.library.picasso.Picasso;
import operations.ImageRotation;

public class DgCamActivity extends Activity implements SensorEventListener {
    private Camera mCamera;
    public Cursor cursor;
    public int count;
    public String[] arrPath;
    int image_column_index;
    private CameraPreview mPreview;
    private SensorManager sensorManager = null;
    private int orientation;
    private ExifInterface exif;
    private int deviceHeight;
    private Button ibRetake;
    private Button ibUse;
    private Button ibCapture;
    private FrameLayout flBtnContainer;
    private File sdRoot;
    Camera.Parameters params;
    private String dir;
    private String fileName;
    MediaRecorder recorder;
    String Video_path;
    private ImageView rotatingImage;
    ArrayList<String> Array_items = new ArrayList<String>();
    private int degrees = -1;
    String Strfor_img1 = "niomage1";
    String Strfor_img2 = "noimag2";
    String Strfor_img3 = "noimage3";
    String Strfor_img4 = "noimage4";
    public Bitmap[] thumbnails;
    ProgressBar mProgressBar;
    TextView Txtvw_photo, Txt_video, Txt_Camera;
    ImageView img_cross1, imgcross2, img_cross3, imgcross4, Img_switch_camera;
    ImageView Image_camera_photoclick, img_video_record, ImgSwitch_camera;
    ImageView Imgvw1, imgvw2, imgvw3, imgvw4, Img_camera_click, img_bottomphoto, img_bottom_camera, Img_bottom_video;
    RelativeLayout Rlv_photo, Rlv_camera, Rlv_video;
    String Str_video_status = "no_video";
    private SurfaceView myCameraSurfaceView;
    int progressStatus;
    FrameLayout preview;
    video_recording mTask;
    int CameraId;
    // TextView next;
    RelativeLayout Rlv_gridview_holder;
    ImageCropView Img_Second_big;
    GridView sdcardImages;
    Button next,Butn_close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Image_camera_photoclick = (ImageView) findViewById(R.id.imageView60);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        img_video_record = (ImageView) findViewById(R.id.img_video_normal);
        ImgSwitch_camera = (ImageView) findViewById(R.id.imageView61);
        Imgvw1 = (ImageView) findViewById(R.id.imageView54);
        Rlv_gridview_holder = (RelativeLayout) findViewById(R.id.relativeLayout37);
        Img_Second_big = (ImageCropView) findViewById(R.id.img_cropView);
        imgvw2 = (ImageView) findViewById(R.id.imageView50);
        imgvw3 = (ImageView) findViewById(R.id.imageView52);
        imgvw4 = (ImageView) findViewById(R.id.imageView53);
        img_bottomphoto = (ImageView) findViewById(R.id.imageView47);
        img_bottom_camera = (ImageView) findViewById(R.id.imageView48);
        Img_bottom_video = (ImageView) findViewById(R.id.imageView49);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        img_cross1 = (ImageView) findViewById(R.id.imageView55);
        imgcross2 = (ImageView) findViewById(R.id.imageView56);
        img_cross3 = (ImageView) findViewById(R.id.imageView57);
        imgcross4 = (ImageView) findViewById(R.id.imageView58);
        Rlv_photo = (RelativeLayout) findViewById(R.id.relativeLayout32);
        Rlv_camera = (RelativeLayout) findViewById(R.id.relativeLayout33);
        Rlv_video = (RelativeLayout) findViewById(R.id.relativeLayout34);
        Txtvw_photo = (TextView) findViewById(R.id.textView32);
        Txt_video = (TextView) findViewById(R.id.textView52);
        Txt_Camera = (TextView) findViewById(R.id.textView51);
        sdcardImages = (GridView) findViewById(R.id.gridView);
        next = (Button) findViewById(R.id.button8);
        Butn_close = (Button) findViewById(R.id.button9);
        // Setting all the path for the image
        sdRoot = Environment.getExternalStorageDirectory();
        dir = "/DCIM/Camera/";

        ////////////////////////////////////////////////////////

        img_bottomphoto.setVisibility(View.VISIBLE);
        img_bottom_camera.setVisibility(View.INVISIBLE);
        Img_bottom_video.setVisibility(View.INVISIBLE);
        sdcardImages.setVisibility(View.VISIBLE);
        Img_Second_big.setVisibility(View.VISIBLE);
        img_video_record.setVisibility(View.GONE);
        ImgSwitch_camera.setVisibility(View.GONE);
        Image_camera_photoclick.setVisibility(View.GONE);
        preview.setVisibility(View.GONE);
        //Rlv_gridview_holder.sethi
        ViewGroup.LayoutParams params = Rlv_gridview_holder.getLayoutParams();
        params.height = 250;

        Txtvw_photo.setTextColor(getResources().getColor(R.color.Loginbg));
        Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
        Txt_Camera.setTextColor(getResources().getColor(R.color.tabWhite));

        //////////////////////////////////////////////////////////////////////////
        Butn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Rlv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_bottomphoto.setVisibility(View.VISIBLE);
                img_bottom_camera.setVisibility(View.INVISIBLE);
                Img_bottom_video.setVisibility(View.INVISIBLE);
                sdcardImages.setVisibility(View.VISIBLE);
                Img_Second_big.setVisibility(View.VISIBLE);
                img_video_record.setVisibility(View.GONE);
                ImgSwitch_camera.setVisibility(View.GONE);
                Image_camera_photoclick.setVisibility(View.GONE);
                preview.setVisibility(View.GONE);
                //Rlv_gridview_holder.sethi
                ViewGroup.LayoutParams params = Rlv_gridview_holder.getLayoutParams();
                params.height = 250;

                Txtvw_photo.setTextColor(getResources().getColor(R.color.Loginbg));
                Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_Camera.setTextColor(getResources().getColor(R.color.tabWhite));

            }
        });
        Rlv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_bottomphoto.setVisibility(View.INVISIBLE);
                img_bottom_camera.setVisibility(View.VISIBLE);
                Img_bottom_video.setVisibility(View.INVISIBLE);
                Txtvw_photo.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_video.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_Camera.setTextColor(getResources().getColor(R.color.Loginbg));
                Image_camera_photoclick.setVisibility(View.VISIBLE);
                img_video_record.setVisibility(View.GONE);

                sdcardImages.setVisibility(View.GONE);
                Img_Second_big.setVisibility(View.GONE);

                ImgSwitch_camera.setVisibility(View.VISIBLE);
                // Image_camera_photoclick.setVisibility(View.GONE);
                preview.setVisibility(View.VISIBLE);
                //Rlv_gridview_holder.sethi
                ViewGroup.LayoutParams params = Rlv_gridview_holder.getLayoutParams();
                params.height = 150;
            }
        });
        Rlv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_bottomphoto.setVisibility(View.INVISIBLE);
                img_bottom_camera.setVisibility(View.INVISIBLE);
                Img_bottom_video.setVisibility(View.VISIBLE);
                Txtvw_photo.setTextColor(getResources().getColor(R.color.tabWhite));
                Txt_video.setTextColor(getResources().getColor(R.color.Loginbg));
                Txt_Camera.setTextColor(getResources().getColor(R.color.tabWhite));
                Image_camera_photoclick.setVisibility(View.GONE);
                img_video_record.setVisibility(View.VISIBLE);
                sdcardImages.setVisibility(View.GONE);
                Img_Second_big.setVisibility(View.GONE);

                ImgSwitch_camera.setVisibility(View.VISIBLE);
                // Image_camera_photoclick.setVisibility(View.GONE);
                preview.setVisibility(View.VISIBLE);
                //Rlv_gridview_holder.sethi
                ViewGroup.LayoutParams params = Rlv_gridview_holder.getLayoutParams();
                params.height = 150;
            }
        });
        ImgSwitch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }

//swap the id of the camera to be used
                if (CameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                    CameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                else
                    CameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

                try {
                    mCamera = Camera.open(CameraId);

                    mCamera.setDisplayOrientation(90);

                    mCamera.setPreviewDisplay(mPreview.getHolder());

                    mCamera.startPreview();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        img_video_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DgCamActivity.this, "Hold it for record video", Toast.LENGTH_LONG).show();
            }
        });
        next.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        // new UploadTask().execute();
                                        if (Array_items.size() == 4) {
                                            Intent i1 = new Intent(DgCamActivity.this,
                                                    Local_image_3dcube.class);
                                            i1.putExtra("array_list", Array_items);
                                            //  i1.putCharSequenceArrayListExtra("array_list", Array_items);
                                            startActivity(i1);
                                            finish();
                                        } else {
                                            Toast.makeText(DgCamActivity.this, "Select 4 images", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

        );
        sdcardImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    // Toast.makeText(getActivity(),position,Toast.LENGTH_LONG).show();
                                                    //  img_Big.invalidate();
                                                    //int items = (Integer) parent.getItemAtPosition(position);
                                                    //  Bitmap bit_big_image = thumbnails[position];
                                                    String Str_path = arrPath[position];
                                                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                                                    options2.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                                    options2.inSampleSize = 2;
                                                    String squareimagepath = null;
                                                    Bitmap bmRotated = null;
                                                    try {
                                                        Bitmap bitmap2 = BitmapFactory.decodeFile(Str_path, options2);
                                                        Img_Second_big.setImageBitmap(bitmap2);
                                                        // Bitmap bit_big_image_crop=Img_Second_big.getCroppedImage();
                                                        // Bitmap bit_big_image_crop = cropToSquare(bit_big_image);
                                                        //cropToSquare_bitmap
                                                        // cropToSquare
                                                        //  Bitmap bit_big_image_crop = cropToSquare_bitmap(bitmap2);
                                                        Bitmap bit_big_image_crop = cropToSquare(bitmap2);
                                                        //Bitmap bit_big_image_crop = Img_Second_big.getCroppedImage();
                                                        //Bitmap bit_big_image = getResizedBitmap(bit_big_imagee, 500, 500);
                                                        //thumbnails[position] = bit_big_image;
//                                                    ExifInterface exif = null;
//                                                    try {
//                                                        exif = new ExifInterface(arrPath[position]);
//                                                    } catch (IOException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                                            ExifInterface.ORIENTATION_UNDEFINED);
                                                        ExifInterface exif = null;
                                                        try {
                                                            exif = new ExifInterface(Str_path);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                                                ExifInterface.ORIENTATION_UNDEFINED);
                                                        bmRotated = rotateBitmap(bit_big_image_crop, orientation);
                                                        //BITMAP_RESIZER
                                                        //   Bitmap bit_big_image_crop = cropToSquare(bit_big_image);
                                                        // Bitmap bit_big_image_crop = BITMAP_RESIZER(bit_big_image,400,400);
                                                        squareimagepath = saveimage(bmRotated, "turnstr");
                                                        //  arrPath[position] = squareimagepath;
                                                    } catch (OutOfMemoryError e) {
                                                        e.printStackTrace();
                                                    }

                                                    //Img_Second_big.setImageBitmap(bit_big_image);

//                                                    Bitmap bit_big_image_crop=Img_Second_big.getCroppedImage();
//                                                    Img_Second_big.setImageBitmap(bit_big_image_crop);
                                                    // String parthh = arrPath[position];

                                                    if (Array_items.size() < 4) {
                                                        Array_items.add(squareimagepath);
                                                        //tb.putListString("list", Array_items);
                                                        if (Strfor_img1.contentEquals("niomage1")) {
                                                            //     Bitmap bmpp = Img_Second_big.getCroppedImage();
                                                            Imgvw1.setImageBitmap(bmRotated);

                                                            Strfor_img1 = "setimage1";
                                                            img_cross1.setVisibility(View.VISIBLE);
                                                            return;
                                                            // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                                                        } else if (Strfor_img2.contentEquals("noimag2")) {
                                                            imgvw2.setImageBitmap(bmRotated);


                                                            Strfor_img2 = "setimage2";
                                                            imgcross2.setVisibility(View.VISIBLE);
                                                            return;
                                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
                                                        } else if (Strfor_img3.contentEquals("noimage3")) {
                                                            try {

                                                                imgvw3.setImageBitmap(bmRotated);

                                                                Strfor_img3 = "setimage3";
                                                                img_cross3.setVisibility(View.VISIBLE);
                                                                return;
                                                            } catch (OutOfMemoryError e) {
                                                                e.printStackTrace();
                                                            }

                                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw3);
                                                        } else if (Strfor_img4.contentEquals("noimage4")) {
                                                            try {

                                                                imgvw4.setImageBitmap(bmRotated);

                                                                Strfor_img4 = "setimage4";
                                                                imgcross4.setVisibility(View.VISIBLE);
                                                                return;
                                                            } catch (OutOfMemoryError e) {
                                                                e.printStackTrace();
                                                            }
                                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
                                                        }
                                                    } else {
                                                        Toast.makeText(DgCamActivity.this, "Ready for upload", Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            }

        );
        img_video_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                       // mProgressBar =new ProgressBar(DgCamActivity.this);
                        progressStatus = 0;
                        img_video_record.setImageResource(R.drawable.video_red_active);
                        if (Array_items.size() < 4) {
                            prepareMediaRecorder();
                            recorder.start();
                            Str_video_status = "is_recording";
                            mTask=new video_recording();
                            mTask.execute();
//                            new AsyncTask<Void, Integer, Void>() {
//                                @Override
//                                protected void onPreExecute() {
//                   /                 mProgressBar.setVisibility(View.VISIBLE);
//                                    super.onPreExecute();
//                                }
//
//                                @Override
//                                protected Void doInBackground(Void... params) {
//                                    progressStatus = 0;
//
//                                    while (progressStatus < 5000) {
//                                        progressStatus++;
//                                        publishProgress(progressStatus);
//                                        try {
//                                            Thread.sleep(1);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                    return null;
//                                }
//
//                                @Override
//                                protected void onProgressUpdate(Integer... values) {
//                                    mProgressBar.setProgress(values[0]);
//
//                                }
//
//                                @Override
//                                protected void onPostExecute(Void aVoid) {
//                                    mProgressBar.setVisibility(View.GONE);
//                                    progressStatus = 0;
//                                    if (Str_video_status.contentEquals("is_recording")) {
//
//                                        recorder.stop();
//                                        Str_video_status = "no_video";
//                                        img_video_record.setImageResource(R.drawable.video_red_normal);
//                                        //  Video_path = getOutputMedia_video_File().getAbsolutePath();
//                                        // Bitmap btm_thumnail= ThumbnailUtils.createVideoThumbnail(Video_path, MediaStore.Video.Thumbnails.MICRO_KIND);
//
////                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
////                                    Imgvw1.setImageBitmap(btm_thumnail);
//
//                                        if (Array_items.size() < 4) {
//                                            Array_items.add(Video_path);
//
//                                            if (Strfor_img1.contentEquals("niomage1")) {
//                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                                Imgvw1.setImageBitmap(btm_thumnail);
//
//                                                Strfor_img1 = "setimage1";
//                                                img_cross1.setVisibility(View.VISIBLE);
//                                                return;
//                                                // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
//                                            } else if (Strfor_img2.contentEquals("noimag2")) {
//                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                                imgvw2.setImageBitmap(btm_thumnail);
//
//
//                                                Strfor_img2 = "setimage2";
//                                                imgcross2.setVisibility(View.VISIBLE);
//                                                return;
//                                                //  Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
//                                            } else if (Strfor_img3.contentEquals("noimage3")) {
//                                                try {
//                                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                                    imgvw3.setImageBitmap(btm_thumnail);
//
//                                                    Strfor_img3 = "setimage3";
//                                                    img_cross3.setVisibility(View.VISIBLE);
//                                                    return;
//                                                } catch (OutOfMemoryError e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                // Picasso.with(getActivity()).load(pictureFile).into(imgvw3);
//                                            } else if (Strfor_img4.contentEquals("noimage4")) {
//                                                try {
//                                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                                    imgvw4.setImageBitmap(btm_thumnail);
//
//                                                    Strfor_img4 = "setimage4";
//                                                    imgcross4.setVisibility(View.VISIBLE);
//                                                    return;
//                                                } catch (OutOfMemoryError e) {
//                                                    e.printStackTrace();
//                                                }
//                                                // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
//                                            }
//                                        } else {
//                                            Toast.makeText(DgCamActivity.this, "your post is ready for upload", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                    super.onPostExecute(aVoid);
//                                }
//                            }.execute();

                        }


                        break;
                    case MotionEvent.ACTION_UP:
                        mTask.cancel(true);
                      //  mProgressBar.

                        mProgressBar.setVisibility(View.GONE);
                        progressStatus = 0;
                        mProgressBar.setProgress(progressStatus);

                        img_video_record.setImageResource(R.drawable.video_red_normal);
                        if (Str_video_status.contentEquals("is_recording")) {
                            try {
                                // recorder.stop();
                                //Str_video_status = "no_video";
                                //  img_video_record.setImageResource(R.drawable.video_red_normal);
                                progressStatus = 0;
                                //  Img_camera_click.setImageResource(R.drawable.video_red_normal);
                                //   Video_path = getOutputMedia_video_File().getAbsolutePath();
//                                Bitmap btm_thumnail_stop = ThumbnailUtils.createVideoThumbnail(Video_path, MediaStore.Video.Thumbnails.MICRO_KIND);
//
////                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                Imgvw1.setImageBitmap(btm_thumnail_stop);
                                mProgressBar.setVisibility(View.GONE);
                                if (Str_video_status.contentEquals("is_recording")) {
//
                                    recorder.stop();
                                    Str_video_status = "no_video";
                                    img_video_record.setImageResource(R.drawable.video_red_normal);
                                    // Video_path = getOutputMedia_video_File().getAbsolutePath();

                                    if (Array_items.size() < 4) {
                                        Array_items.add(Video_path);
                                        if (Strfor_img1.contentEquals("niomage1")) {
                                            Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                            Imgvw1.setImageBitmap(btm_thumnail);

                                            Strfor_img1 = "setimage1";
                                            img_cross1.setVisibility(View.VISIBLE);

                                            // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                                        } else if (Strfor_img2.contentEquals("noimag2")) {
                                            Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                            imgvw2.setImageBitmap(btm_thumnail);


                                            Strfor_img2 = "setimage2";
                                            imgcross2.setVisibility(View.VISIBLE);

                                            //  Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
                                        } else if (Strfor_img3.contentEquals("noimage3")) {
                                            try {
                                                Bitmap btm_thumnail = getVidioThumbnail(Video_path);
                                                imgvw3.setImageBitmap(btm_thumnail);

                                                Strfor_img3 = "setimage3";
                                                img_cross3.setVisibility(View.VISIBLE);

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

                                            } catch (OutOfMemoryError e) {
                                                e.printStackTrace();
                                            }
                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
                                        }
                                    } else {
                                        Toast.makeText(DgCamActivity.this, "your post is ready for upload", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                        break;
                }
                return true;
            }
        });
//////////////////////////////////////////////////////////////////////////
        // images from gallery query

        String[] columns = {MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE,
        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        final String orderBy = MediaStore.Files.FileColumns.DATE_ADDED;
        Uri queryUri = MediaStore.Files.getContentUri("external");
        cursor = getContentResolver().query(queryUri,
                columns,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );

        count = cursor.getCount();
        arrPath = new String[count];
        thumbnails = new Bitmap[count];
        ImageAdapter_new imageAdapter = new ImageAdapter_new();
        sdcardImages.setAdapter(imageAdapter);
        ////////////////////////////////////////////////////////////////////

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        deviceHeight = display.getHeight();
        Image_camera_photoclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Array_items.size() < 4) {

                    mCamera.takePicture(null, null, mPicture);
                    Image_camera_photoclick.setImageResource(R.drawable.camera_blue_active);
                } else {
                    Toast.makeText(DgCamActivity.this, "your post is ready for upload", Toast.LENGTH_LONG).show();
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


                Strfor_img3 = "noimage3";
                //Imgvw1
            }
        });


    }

    private void createCamera() {
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Setting the right parameters in the camera
        params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size cs = sizes.get(0);
        params.setPreviewSize(cs.width, cs.height);
        // List<Camera.Size> sizes = params.getSupportedPreviewSizes();
//        Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
//        params.setPreviewSize(optimalSize.width, optimalSize.height);
        params.setPictureSize(1600, 1200);
        params.setPictureFormat(PixelFormat.JPEG);
        params.setJpegQuality(95);
        //mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        // FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        ///FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        // preview = (FrameLayout) findViewById(R.id.camera_preview);

        // Calculating the width of the preview so it is proportional.
        float widthFloat = (float) (deviceHeight) * 4 / 3;
        int width = Math.round(widthFloat);

        // Resizing the LinearLayout so we can make a proportional preview. This
        // approach is not 100% perfect because on devices with a really small
        // screen the the image will still be distorted - there is place for
        // improvment.
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, deviceHeight);
        preview.setLayoutParams(layoutParams);

        // Adding the camera preview after the FrameLayout and before the button
        // as a separated element.
        preview.addView(mPreview, 0);
        //preview.addView(myCameraSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Test if there is a camera on the device and if the SD card is
        // mounted.
        if (!checkCameraHardware(this)) {
            Intent i = new Intent(this, NoCamera.class);
            startActivity(i);
            finish();
        } else if (!checkSDCard()) {
            Intent i = new Intent(this, NoSDCard.class);
            startActivity(i);
            finish();
        }

        // Creating the camera
        createCamera();

        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // release the camera immediately on pause event
        releaseCamera();

        // removing the inserted view - so when we come back to the app we
        // won't have the views on top of each other.
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.removeViewAt(0);
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private boolean checkSDCard() {
        boolean state = false;

        String sd = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sd)) {
            state = true;
        }

        return state;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            //mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            // attempt to get a Camera instance
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }

        // returns null if camera is unavailable
        return c;
    }

    private PictureCallback mPicture = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {

            //// Replacing the button after a photho was taken.
            Bitmap bmRotated = MakeSquare(data, CameraId);
            String Str_path = saveimage(bmRotated, "turnst");
            // File name of the image that we just took.
//            fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";
//
//            // Creating the directory where to save the image. Sadly in older
//            // version of Android we can not get the Media catalog name
//            File mkDir = new File(sdRoot, dir);
//            mkDir.mkdirs();
//
//            // Main file where to save the data that we recive from the camera
//            File pictureFile = new File(sdRoot, dir + fileName);
//
//            try {
//                FileOutputStream purge = new FileOutputStream(pictureFile);
//                purge.write(data);
//                purge.close();
//            } catch (FileNotFoundException e) {
//                Log.d("DG_DEBUG", "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
//            }
//
//            // Adding Exif data for the orientation. For some strange reason the
//            // ExifInterface class takes a string instead of a file.
//            try {
//                exif = new ExifInterface("/sdcard/" + dir + fileName);
//                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + orientation);
//                exif.saveAttributes();
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //   String Str_path = pictureFile.getAbsolutePath();

            //  Imgvw1.setImageBitmap(bmRotated);
            Image_camera_photoclick.setImageResource(R.drawable.camera_blue_normal);
            mCamera.startPreview();

            if (Array_items.size() < 4) {


                Array_items.add(Str_path);


                if (Strfor_img1.contentEquals("niomage1")) {

                    try {
                        // public static Bitmap MakeSquare(byte[] data, int cameraID) {
                        //  Bitmap bmRotated = ImageResizer.rotate(pictureFile, ImageRotation.CW_90);

                        //Picasso.with(DgCamActivity.this).load(pictureFile).into(Imgvw1);
                        Imgvw1.setImageBitmap(bmRotated);


                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    Strfor_img1 = "setimage1";
                    img_cross1.setVisibility(View.VISIBLE);
                    return;
                    // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                } else if (Strfor_img2.contentEquals("noimag2")) {
//
                    try {
                        //Bitmap bmRotated = ImageResizer.rotate(pictureFile, ImageRotation.CW_90);

                        // Picasso.with(DgCamActivity.this).load(pictureFile).into(imgvw2);
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

                        try {
                            // Bitmap bmRotated = ImageResizer.rotate(pictureFile, ImageRotation.CW_90);
                            // Picasso.with(DgCamActivity.this).load(pictureFile).into(imgvw3);
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


                } else if (Strfor_img4.contentEquals("noimage4")) {
                    try {

                        try {
                            // Bitmap bmRotated = ImageResizer.rotate(pictureFile, ImageRotation.CW_90);
                            //  Picasso.with(DgCamActivity.this).load(pictureFile).into(imgvw4);
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
                Toast.makeText(DgCamActivity.this, "your post is ready for upload", Toast.LENGTH_LONG).show();
            }
        }


    };

    /**
     * Putting in place a listener so we can get the sensor data only when
     * something changes.
     */
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                RotateAnimation animation = null;
                if (event.values[0] < 4 && event.values[0] > -4) {
                    if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
                        // UP
                        orientation = ExifInterface.ORIENTATION_ROTATE_90;
                        animation = getRotateAnimation(180);
                        degrees = 180;
                    } else if (event.values[1] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_270) {
                        // UP SIDE DOWN
                        orientation = ExifInterface.ORIENTATION_ROTATE_270;
                        animation = getRotateAnimation(90);
                        degrees = 90;
                    }
                } else if (event.values[1] < 4 && event.values[1] > -4) {
                    if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
                        // LEFT
                        orientation = ExifInterface.ORIENTATION_NORMAL;
                        animation = getRotateAnimation(0);
                        degrees = 0;
                    } else if (event.values[0] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_180) {
                        // RIGHT
                        orientation = ExifInterface.ORIENTATION_ROTATE_180;
                        animation = getRotateAnimation(180);
                        degrees = 180;
                    }
                }
                if (animation != null) {
                    //rotatingImage.startAnimation(animation);
//					Txtvw.startAnimation(animation);
//					Txt_video.startAnimation(animation);
//					Txt_photo.startAnimation(animation);
//					Image_camera_photoclick.startAnimation(animation);
//					ImgSwitch_camera.startAnimation(animation);
                }
            }

        }
    }

    /**
     * Calculating the degrees needed to rotate the image imposed on the button
     * so it is always facing the user in the right direction
     *
     * @param toDegrees
     * @return
     */
    private RotateAnimation getRotateAnimation(float toDegrees) {
        float compensation = 0;

        if (Math.abs(degrees - toDegrees) > 180) {
            compensation = 360;
        }

        // When the device is being held on the left side (default position for
        // a camera) we need to add, not subtract from the toDegrees.
        if (toDegrees == 0) {
            compensation = -compensation;
        }

        // Creating the animation and the RELATIVE_TO_SELF means that he image
        // will rotate on it center instead of a corner.
        RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Adding the time needed to rotate the image
        animation.setDuration(250);

        // Set the animation to stop after reaching the desired position. With
        // out this it would return to the original state.
        animation.setFillAfter(true);

        return animation;
    }

    /**
     * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private boolean prepareMediaRecorder() {
        // camera = getCameraInstance();
        recorder = new MediaRecorder();

        mCamera.unlock();
        recorder.setCamera(mCamera);
//        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//        preview.addView(myCameraSurfaceView);

        //  recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//       // mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        Camera.Parameters parameters = DgCamActivity.this.mCamera.getParameters();
////        // parameters.setPreviewSize(width, height);
//       List<Camera.Size> sizes = params.getSupportedPreviewSizes();
//        Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        Video_path = getOutputMedia_video_File().getAbsolutePath();
        recorder.setOutputFile(Video_path);
        //Array_items.add(Video_path);
        recorder.setMaxDuration(150000); // Set max duration 15 sec.
        recorder.setMaxFileSize(5000000);
        //recorder.setVideoSize(1280,720);
        //recorder.setVideoSize(optimalSize.width,optimalSize.height);
        //recorder.setVideoFrameRate(30);
//        DisplayMetrics dm;
//        dm = new DisplayMetrics();
//        DgCamActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int height = dm.heightPixels;
//        int width = dm.widthPixels;
//        1600, 1200
        // recorder.setVideoSize(900, 480);


        if (CameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            recorder.setOrientationHint(90);
        } else {
            recorder.setOrientationHint(270);
        }

        recorder.setPreviewDisplay(mPreview.getHolder().getSurface());

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
            // lock camera for later use
        }
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

    public static Bitmap MakeSquare(byte[] data, int cameraID) {
        int width;
        int height;
        Matrix matrix = new Matrix();
        Camera.CameraInfo info = new Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraID, info);
        // Convert ByteArray to Bitmap

        Bitmap bitPic = BitmapFactory.decodeByteArray(data, 0, data.length);
        width = bitPic.getWidth();
        height = bitPic.getHeight();

        // Perform matrix rotations/mirrors depending on camera that took the photo
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);

            matrix.postConcat(matrixMirrorY);
        }

        matrix.postRotate(90);


        // Create new Bitmap out of the old one
        Bitmap bitPicFinal = Bitmap.createBitmap(bitPic, 0, 0, width, height, matrix, true);
        bitPic.recycle();
        int desWidth;
        int desHeight;
        desWidth = bitPicFinal.getWidth();
        desHeight = desWidth;
        Bitmap croppedBitmap = Bitmap.createBitmap(bitPicFinal, 0, bitPicFinal.getHeight() / 2 - bitPicFinal.getWidth() / 2, desWidth, desHeight);
        croppedBitmap = Bitmap.createScaledBitmap(croppedBitmap, 528, 528, true);
        return croppedBitmap;
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

    class ViewHolder {
        //  SquareImageView imgView;
        ImageView imgView;
        ImageView videoICON;
        int id;
    }

    public class ImageAdapter_new extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter_new() {
            mInflater = (LayoutInflater) DgCamActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;


            Bitmap bmp = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.gallery_imageview, null);
                holder.imgView = (ImageView) convertView.findViewById(R.id.picture);

                //holder.videoICON = (ImageView) convertView.findViewById(R.id.videoICON);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            cursor.moveToPosition(position);
            int id = cursor.getInt(image_column_index);
            int image_path_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[position] = cursor.getString(image_path_index);
            String imagepath = cursor.getString(image_path_index);

            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //bmOptions.inJustDecodeBounds = true;

            bmOptions.inSampleSize = 3;


            // bmOptions.inPurgeable = true;
            int type = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);

            int t = cursor.getInt(type);
            if (t == 1) {
                try {
//                    thumbnails[position] = MediaStore.Images.Thumbnails.getThumbnail(
//                            getActivity().getContentResolver(), id,
//                            MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
                    thumbnails[position] = MediaStore.Images.Thumbnails.getThumbnail(
                            getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
//                    holder.videoICON.setVisibility(View.INVISIBLE);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

            }
            String Str_path = arrPath[position];
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(Str_path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

//            } else if (t == 3) {
//                try {
//                    thumbnails[position] = MediaStore.Video.Thumbnails.getThumbnail(
//                            getActivity().getContentResolver(), id,
//                            MediaStore.Video.Thumbnails.MINI_KIND, bmOptions);
//                    holder.videoICON.setVisibility(View.VISIBLE);
//                } catch (OutOfMemoryError e) {
//                    e.printStackTrace();
//                }
//            }


            holder.imgView.setId(position);


            try {
                //String img_path = cursor.getString(image_path_index);
                //  File file_piccaso = new File(img_path);
                if (position == 0) {

                    // Img_Second_big.setImageBitmap(thumbnails[0]);
                }
//                String Str_path2 = arrPath[position];
                File file_path = new File(Str_path);
//
//                Uri uri = Uri.fromFile(file_path);
//                Bitmap Bmp_down_sample =  getDownsampledBitmap(getActivity(),uri)
                // Picasso.with(getActivity()).load(file_piccaso).into(holder.imgView);
                try {
                    // Bitmap bmRotated = ImageResizer.rotate(file_path, ImageRotation.CW_90);
                    Bitmap bmRotated = rotateBitmap(thumbnails[position], orientation);
                    // Bitmap BmpSquare = cropToSquare(bmRotated);
                    thumbnails[position] = bmRotated;
                    //String squareimagepath = saveimage(BmpSquare,"turnstr");
                    // arrPath[position] =squareimagepath;

                    holder.imgView.setImageBitmap(bmRotated);
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            holder.id = position;
            return convertView;

        }

        public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
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
//                bitmap.recycle();
//            }catch (java.lang.RuntimeException e){
//                e.printStackTrace();
//            }
                return bmRotated;

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }

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
//        Matrix matrix = new Matrix();
//        matrix.postScale(0.5f, 0.5f);
//        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 500, 500,500, 500, matrix, true);

        return cropImg;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
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
//                bitmap.recycle();
//            }catch (java.lang.RuntimeException e){
//                e.printStackTrace();
//            }
            return bmRotated;

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

    }

    class video_recording extends AsyncTask<Void, Integer, Void> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected Void doInBackground(Void... params) {
            progressStatus = 0;

            while (progressStatus < 5000) {
                if (isCancelled())
                    break;
                publishProgress(progressStatus);
                try {
                    Thread.sleep(1);
                    progressStatus++;
                } catch (InterruptedException e) {
                    progressStatus=0;


                    e.printStackTrace();
                }
            }
            return null;
        }
        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(Integer... values) {
            // setting progress percentage
            mProgressBar.setProgress(values[0]);
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(Void aVoid) {
            // dismiss the dialog after the file was downloaded
            mProgressBar.setVisibility(View.GONE);
            progressStatus = 0;
            if (Str_video_status.contentEquals("is_recording")) {

                recorder.stop();
                Str_video_status = "no_video";
                img_video_record.setImageResource(R.drawable.video_red_normal);
                //  Video_path = getOutputMedia_video_File().getAbsolutePath();
                // Bitmap btm_thumnail= ThumbnailUtils.createVideoThumbnail(Video_path, MediaStore.Video.Thumbnails.MICRO_KIND);

//                                    Bitmap btm_thumnail = getVidioThumbnail(Video_path);
//                                    Imgvw1.setImageBitmap(btm_thumnail);

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
                    Toast.makeText(DgCamActivity.this, "your post is ready for upload", Toast.LENGTH_LONG).show();
                }
            }
            super.onPostExecute(aVoid);
        }
    }

}

