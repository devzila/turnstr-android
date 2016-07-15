package com.ToxicBakery.viewpager.transforms.example;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import Session_handler.Session_manager;
import adapter.MyTabFactory;
import adapter.tabs_pager_adapter_Main;
import circular_imagview.ProgressHUD;
import dgcam.DgCamActivity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    TabHost tabHost;
    public FragmentTabHost FmTabHost;
    ImageButton ib;
    public ViewPager mPager;
    String Name, Access_token, device_id;
    ImageView img_logout;
    ProgressDialog pDialog;
    ProgressHUD mProgressHUD;
    public tabs_pager_adapter_Main mAdapter;
    public ViewPager mViewPager;
    public TabHost mTabHost;
    ImageView img1, img2, img3;
    Button b1;
    RelativeLayout rlv_home, Rlv_Explorer, Rlv_Camera, Rlv_Setting, Rlv_Profile;
    TextView Txt_Phto, Txt_camera, Txt_video, Txt_vwTurnstr, Txt_activity, Txt_logout;
    ImageView img_close;
    boolean dragging = false;
    Session_manager session;
    RelativeLayout rlv_top;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        rlv_home = (RelativeLayout) findViewById(R.id.home);
        rlv_top = (RelativeLayout) findViewById(R.id.relativeLayout5);
        Rlv_Explorer = (RelativeLayout) findViewById(R.id.explorer);

        Rlv_Camera = (RelativeLayout) findViewById(R.id.camera);
        Rlv_Setting = (RelativeLayout) findViewById(R.id.like);
        Rlv_Profile = (RelativeLayout) findViewById(R.id.profile);
        Txt_vwTurnstr = (TextView) findViewById(R.id.textView13);
        Txt_activity = (TextView) findViewById(R.id.txt_activity);
        Txt_logout = (TextView) findViewById(R.id.textView49);
        rlv_top.setVisibility(View.GONE);
//        Name = intent.getStringExtra("Name");
//        Access_token = intent.getStringExtra("access_tocken");
        img_logout = (ImageView) findViewById(R.id.img_logout);
        //  device_id = intent.getStringExtra("Ostype");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new tabs_pager_adapter_Main(getSupportFragmentManager());
        // initialiseTabHost();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        session = new Session_manager(getApplicationContext());
        rlv_home.setBackgroundColor(getResources().getColor(R.color.Loginbg));

//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mViewPager.getCurrentItem() == PAGE) {
//                    mViewPager.setCurrentItem(PAGE-1, false);
//                    mViewPager.setCurrentItem(PAGE, false);
//                    return  true;
//                }
//            }
//                return false;
//            }
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  Linkify.
                return false;
            }
        });


        rlv_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlv_home.setBackgroundColor(getResources().getColor(R.color.Loginbg));
                Rlv_Explorer.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Camera.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Setting.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Profile.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                rlv_top.setVisibility(View.GONE);
                Txt_vwTurnstr.setText("Turnstr");
                Txt_logout.setVisibility(View.GONE);
                Txt_activity.setVisibility(View.GONE);
                Txt_vwTurnstr.setVisibility(View.VISIBLE);
                // rlv_home.invalidate();
                mViewPager.setCurrentItem(0);
            }
        });
        Rlv_Explorer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlv_home.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Explorer.setBackgroundColor(getResources().getColor(R.color.Loginbg));
                Rlv_Camera.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Setting.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Profile.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                rlv_top.setVisibility(View.GONE);
                Txt_activity.setVisibility(View.GONE);
                Txt_logout.setVisibility(View.GONE);
                Txt_vwTurnstr.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(1);
            }
        });
        Rlv_Camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                rlv_home.setBackgroundColor(getResources().getColor(R.color.tabMedium));
//                Rlv_Explorer.setBackgroundColor(getResources().getColor(R.color.tabMedium));
//                Rlv_Camera.setBackgroundColor(getResources().getColor(R.color.Loginbg));
//                Rlv_Setting.setBackgroundColor(getResources().getColor(R.color.tabMedium));
//                Rlv_Profile.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                // mViewPager.setCurrentItem(2);
//                Intent i1 = new Intent(MainActivity.this,
//                        Camera_activity.class);
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA);
                int permissionCheck_ForGallery = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCheck_For_Audio = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
//                    Intent i1 = new Intent(MainActivity.this,
//                            DgCamActivity.class);
//
//                    startActivity(i1);
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            1);

                    Toast.makeText(MainActivity.this, "you need to enable camera permission", Toast.LENGTH_LONG).show();
                } else if (permissionCheck_ForGallery == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);
                    Toast.makeText(MainActivity.this, "you need to enable External Storage permission", Toast.LENGTH_LONG).show();
                }
                else if (permissionCheck_For_Audio == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            3);
                    Toast.makeText(MainActivity.this, "you need to enable Audio permission", Toast.LENGTH_LONG).show();
                } else {
                    Intent i1 = new Intent(MainActivity.this,
                            DgCamActivity.class);

                    startActivity(i1);
                }

            }
        });
        Rlv_Setting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlv_home.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Explorer.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Camera.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Setting.setBackgroundColor(getResources().getColor(R.color.Loginbg));
                Rlv_Profile.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                rlv_top.setVisibility(View.VISIBLE);
                Txt_activity.setVisibility(View.VISIBLE);
                Txt_logout.setVisibility(View.GONE);
                Txt_activity.setText("What's up");
                Txt_vwTurnstr.setVisibility(View.GONE);
                mViewPager.setCurrentItem(3);
            }
        });
        Rlv_Profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rlv_home.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Explorer.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Camera.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Setting.setBackgroundColor(getResources().getColor(R.color.tabMedium));
                Rlv_Profile.setBackgroundColor(getResources().getColor(R.color.Loginbg));
                rlv_top.setVisibility(View.VISIBLE);
                Txt_logout.setVisibility(View.VISIBLE);
                Txt_vwTurnstr.setText("Profile");
                Txt_activity.setVisibility(View.GONE);
                Txt_vwTurnstr.setVisibility(View.VISIBLE);
                mViewPager.setCurrentItem(4);
            }
        });
        mViewPager.setOnTouchListener(null);
        img_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Logout().execute();
                session.logoutUser();
                finish();
            }
        });
        Txt_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });
    }

//	private void setTabs() {
//		addTab("", R.drawable.home, HomeActivity.class);
//		addTab("", R.drawable.search, Explorer_Screen.class);
//		addTab("", R.drawable.camerabg, HomeActivity.class);
//		addTab("", R.drawable.like, AlarmActivity.class);
//		addTab("", R.drawable.profile, SettingActivity.class);
//
//		ib = (ImageButton) findViewById(R.id.ibHome);
//		ib.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
////				Toast.makeText(MainActivity.this, "Work in progress", Toast.LENGTH_LONG)
////						.show();
//
//				Intent i1 = new Intent(MainActivity.this,
//						Camera_activity.class);
//
//				startActivity(i1);
//
//			}
//		});
//	}

//	private void addTab(String labelId, int drawableId, Class<?> c) {
//		Intent intent = new Intent(this, c);
//		intent.putExtra("Name",Name);
//		intent.putExtra("Acess_Token",Access_token);
//		intent.putExtra("device_id",device_id);
//		//intent.addFlag(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
//
//		View tabIndicator = LayoutInflater.from(this).inflate(
//				R.layout.tab_indicator, getTabWidget(), false);
//
//		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
//		icon.setImageResource(drawableId);
//		spec.setIndicator(tabIndicator);
//		spec.setContent(intent);
//		tabHost.addTab(spec);
//
//
//	}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {


        }

    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }


    private class Logout extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode;
        String str;
        String Deviceid, Token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mProgressHUD = ProgressHUD.show(MainActivity.this, "Connecting", true, true, this);
            // Showing progress dialog

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("access_token", Access_token));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constant.Logout,
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

            if (str.contentEquals("false")) {
                Intent i1 = new Intent(MainActivity.this,
                        Login_Screen.class);

                startActivity(i1);

                finish();
            }


        }

        @Override
        public void onCancel(DialogInterface dialog) {

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
                        Toast.makeText(MainActivity.this, "enabled camera", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, "enabled external Storage", Toast.LENGTH_LONG).show();
                    } else {
                        // Toast.makeText(getActivity(), "you need to enable camera permission", Toast.LENGTH_LONG).show();
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }
                case 3: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        // selectImage();
                        Toast.makeText(MainActivity.this, "enabled Audio permission", Toast.LENGTH_LONG).show();
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
