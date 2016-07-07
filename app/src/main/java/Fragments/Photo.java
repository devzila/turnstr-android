package Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ToxicBakery.viewpager.transforms.example.Local_image_3dcube;
import com.ToxicBakery.viewpager.transforms.example.R;
import com.aviary.android.feather.sdk.widget.CropImageView;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Interfaces.bitmap_interface;
import Multipart_enttity.MultipartEntityy;
import Session_handler.TinyDB;
import Square_imageview.SquareImageView;
import adapter.FragmentInterface;
import bitmap_utils.AndroidBmpUtil;
import it.sephiroth.android.library.picasso.Picasso;

public class Photo extends Fragment implements FragmentInterface {
    public Cursor cursor;
    public int count;
    public Bitmap[] thumbnails;
    public boolean[] thumbnailsselection;
    public String[] arrPath;
    public int[] typeMedia;
    MultipartEntityy entity;
    // Column index for the Thumbnails Image IDs.
    GridView sdcardImages;
    private int columnIndex;
    ProgressDialog pDialog;
    int image_column_index;
    ImageView Imgvw1, imgvw2, imgvw3, imgvw4, Close_icon;
    ImageView img_cross1, imgcross2, img_cross3, imgcross4;
    int int_check_Imagevalue = 0;
    ImageCropView Img_Second_big;
    TextView next;
    ImageView img_Big;
    String result;
    ArrayList<String> Array_items = new ArrayList<String>();
    String Strfor_img1 = "niomage1";
    String Strfor_img2 = "noimag2";
    String Strfor_img3 = "noimage3";
    String Strfor_img4 = "noimage4";
    TinyDB tb;
    bitmap_interface myinterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
        img_Big = (ImageView) rootView.findViewById(R.id.imageView16);
        Img_Second_big = (ImageCropView) rootView.findViewById(R.id.camera_preview);
        Imgvw1 = (ImageView) rootView.findViewById(R.id.imageView21);
        imgvw2 = (ImageView) rootView.findViewById(R.id.imageView17);
        imgvw3 = (ImageView) rootView.findViewById(R.id.imageView19);
        imgvw4 = (ImageView) rootView.findViewById(R.id.imageView20);
        img_cross1 = (ImageView) rootView.findViewById(R.id.imageView22);
        imgcross2 = (ImageView) rootView.findViewById(R.id.imageView23);
        img_cross3 = (ImageView) rootView.findViewById(R.id.imageView24);
        imgcross4 = (ImageView) rootView.findViewById(R.id.imageView25);
        Close_icon = (ImageView) rootView.findViewById(R.id.img_close);
        next = (TextView) rootView.findViewById(R.id.textView27);
        sdcardImages = (GridView) rootView.findViewById(R.id.gridview);
        tb = new TinyDB(getActivity());
        tb.clear();
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
        Img_Second_big.setAspectRatio(4, 4);

        //@SuppressWarnings("deprecation")
        cursor = getActivity().getContentResolver().query(queryUri,
                columns,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );

        Close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        Imgvw1.setTag("no_image");
        imgvw2.setTag("no_image2");
        imgvw3.setTag("no_image3");
        imgvw4.setTag("no_image4");
//		 image_column_index = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
//		int Full_image = cursor.getInt(image_column_index);
//		Bitmap bmp_new = MediaStore.Images.Thumbnails.getThumbnail(
//				getActivity().getContentResolver(), Full_image,
//				MediaStore.Images.Thumbnails.MINI_KIND,null);
//		img_Big.setImageBitmap(bmp_new);
        count = cursor.getCount();
        thumbnails = new Bitmap[count];
        arrPath = new String[count];
        typeMedia = new int[count];
        thumbnailsselection = new boolean[count];
        ImageAdapter_new imageAdapter = new ImageAdapter_new();
        sdcardImages.setAdapter(imageAdapter);
        try {
//            Bitmap bit_big_imagenew = thumbnails[1];
//            img_Big.setImageBitmap(bit_big_imagenew);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        img_cross1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                } catch (IndexOutOfBoundsException error) {
                    error.printStackTrace();
                }


                //Imgvw1
            }
        });
        imgcross4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


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
                } catch (IndexOutOfBoundsException error) {
                    error.printStackTrace();
                }

                //Imgvw1
            }
        });
        imgcross2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                } catch (IndexOutOfBoundsException error) {
                    error.printStackTrace();
                }
                //Imgvw1
            }
        });
        img_cross3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                } catch (IndexOutOfBoundsException error) {
                    error.printStackTrace();
                }
            }
        });

        sdcardImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    // Toast.makeText(getActivity(),position,Toast.LENGTH_LONG).show();
                                                    //  img_Big.invalidate();
                                                    //int items = (Integer) parent.getItemAtPosition(position);
                                                    Bitmap bit_big_image = thumbnails[position];
                                                   // Bitmap bit_big_image_crop=Img_Second_big.getCroppedImage();
                                                   // Bitmap bit_big_image_crop = cropToSquare(bit_big_image);
                                                    //cropToSquare_bitmap
                                                   // Bitmap bit_big_image_crop = cropToSquare_bitmap(bit_big_image);
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
//                                                     Bitmap bmRotated = rotateBitmap(bit_big_image, orientation);
                                                    //BITMAP_RESIZER
                                                //   Bitmap bit_big_image_crop = cropToSquare(bit_big_image);
//                                                    Bitmap bit_big_image_crop = BITMAP_RESIZER(bit_big_image,400,400);
//                                                    String squareimagepath = saveimage(bit_big_image_crop, "turnstr");
                                                  //  arrPath[position] = squareimagepath;

                                                    img_Big.setVisibility(View.INVISIBLE);

                                                    Img_Second_big.setImageBitmap(bit_big_image);

//                                                    Bitmap bit_big_image_crop=Img_Second_big.getCroppedImage();
//                                                    Img_Second_big.setImageBitmap(bit_big_image_crop);
                                                    String parthh = arrPath[position];

                                                    if (Array_items.size() < 4) {
                                                        Array_items.add(parthh);
                                                        tb.putListString("list", Array_items);
                                                        if (Strfor_img1.contentEquals("niomage1")) {
                                                            //     Bitmap bmpp = Img_Second_big.getCroppedImage();
                                                            Imgvw1.setImageBitmap(bit_big_image);

                                                            Strfor_img1 = "setimage1";
                                                            img_cross1.setVisibility(View.VISIBLE);
                                                            return;
                                                            // Picasso.with(getActivity()).load(pictureFile).into(Imgvw1);
                                                        } else if (Strfor_img2.contentEquals("noimag2")) {
                                                            imgvw2.setImageBitmap(bit_big_image);


                                                            Strfor_img2 = "setimage2";
                                                            imgcross2.setVisibility(View.VISIBLE);
                                                            return;
                                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw2);
                                                        } else if (Strfor_img3.contentEquals("noimage3")) {
                                                            try {

                                                                imgvw3.setImageBitmap(bit_big_image);

                                                                Strfor_img3 = "setimage3";
                                                                img_cross3.setVisibility(View.VISIBLE);
                                                                return;
                                                            } catch (OutOfMemoryError e) {
                                                                e.printStackTrace();
                                                            }

                                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw3);
                                                        } else if (Strfor_img4.contentEquals("noimage4")) {
                                                            try {

                                                                imgvw4.setImageBitmap(bit_big_image);

                                                                Strfor_img4 = "setimage4";
                                                                imgcross4.setVisibility(View.VISIBLE);
                                                                return;
                                                            } catch (OutOfMemoryError e) {
                                                                e.printStackTrace();
                                                            }
                                                            // Picasso.with(getActivity()).load(pictureFile).into(imgvw4);
                                                        }
                                                    } else {
                                                        Toast.makeText(getActivity(), "Ready for upload", Toast.LENGTH_LONG).show();
                                                    }




                                                }
                                            }

        );
        next.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        // new UploadTask().execute();
                                        if (Array_items.size() == 4) {
                                            Intent i1 = new Intent(getActivity(),
                                                    Local_image_3dcube.class);
                                            i1.putExtra("array_list", Array_items);
                                            //  i1.putCharSequenceArrayListExtra("array_list", Array_items);
                                            startActivity(i1);
                                        } else {
                                            Toast.makeText(getActivity(), "Select 4 images", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

        );
        //cursor.close();
        //new Gallery_images().execute();
        return rootView;
    }

    @Override
    public void fragmentBecameVisible() {
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
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
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
                            getActivity().getContentResolver(), id,
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

                    img_Big.setImageBitmap(thumbnails[0]);
                }
//                String Str_path2 = arrPath[position];
//                File file_path = new File(Str_path2);
//
//                Uri uri = Uri.fromFile(file_path);
//                Bitmap Bmp_down_sample =  getDownsampledBitmap(getActivity(),uri)
                // Picasso.with(getActivity()).load(file_piccaso).into(holder.imgView);
                try {
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

    private class Gallery_images extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Syncying...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            //publishProgress("Please wait...");
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

            @SuppressWarnings("deprecation")
            Cursor imagecursor = getActivity().managedQuery(queryUri,
                    columns,
                    selection,
                    null, // Selection args (none).
                    MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
            );

            int image_column_index = imagecursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            count = imagecursor.getCount();
            thumbnails = new Bitmap[count];
            arrPath = new String[count];
            typeMedia = new int[count];
            thumbnailsselection = new boolean[count];
            Bitmap bmp = null;
            for (int i = 0; i < 1; i++) {
                imagecursor.moveToPosition(i);
                int id = imagecursor.getInt(image_column_index);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 2;
                bmOptions.inPurgeable = true;
                int type = imagecursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
                int t = imagecursor.getInt(type);
                if (t == 1)
                    thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
                            getActivity().getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MINI_KIND, bmOptions);
                else if (t == 3)
                    thumbnails[i] = MediaStore.Video.Thumbnails.getThumbnail(
                            getActivity().getContentResolver(), id,
                            MediaStore.Video.Thumbnails.MINI_KIND, bmOptions);

                arrPath[i] = imagecursor.getString(dataColumnIndex);
                typeMedia[i] = imagecursor.getInt(type);

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
//            ImageAdapter imageAdapter = new ImageAdapter();
//            sdcardImages.setAdapter(imageAdapter);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

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
           // dir.delete();



            // Create a name for the saved image
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            File file = new File(dir, timeStamp + ".png");

            // Show a toast message on successful save

            try {

                output = new FileOutputStream(file);
                path = file.getAbsolutePath();
//
                // Compress into png format image from 0% - 100%
                bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
              //  new AndroidBmpUtil().save(bmp, path);
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
               // new AndroidBmpUtil().save(bmp, path);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return path;
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
    public static Bitmap cropToSquare_bitmap(Bitmap bitmap) {
        Bitmap dstBmp;
        if (bitmap.getWidth() >= bitmap.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        }else{

             dstBmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        return dstBmp;
    }
    private class Bitmap_operation extends AsyncTask<String, Void, String> {


        Bitmap_operation(bitmap_interface mi) {
            myinterface = mi;
        }

        @Override
        protected String doInBackground(String... params) {
            myinterface.onPostExecute();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(Void... values) {
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

        InputStream is = getActivity().getContentResolver().openInputStream(uri);
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

        InputStream is = getActivity().getContentResolver().openInputStream(uri);
        resizedBitmap = BitmapFactory.decodeStream(is, null, outBitmap);
        is.close();

        return resizedBitmap;
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

    public class ImageAdapterrr extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapterrr(Context c) {
            mContext = c;
        }

        void add(String path){
            itemList.add(path);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);

            imageView.setImageBitmap(bm);
            return imageView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float)height / (float)reqHeight);
                } else {
                    inSampleSize = Math.round((float)width / (float)reqWidth);
                }
            }

            return inSampleSize;
        }

    }
    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }
}




