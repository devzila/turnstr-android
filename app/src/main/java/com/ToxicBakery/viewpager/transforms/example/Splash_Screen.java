package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import circular_imagview.ProgressHUD;
import circular_imagview.ProgressHUDDemo;

public class Splash_Screen extends Activity {
   
    // private static final String TAG = "SplashActivity";
    protected boolean _active = true;
    private SharedPreferences preferences;
    private Thread splashTread;

    /** The _splash time. */
    protected int _splashTime = 1500;

    // SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash__screen);
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    finish();
                } finally {
                    try {

                         Intent i1 = new Intent(Splash_Screen.this,
                         Login_Screen.class);

                         startActivity(i1);

                        finish();

                    } catch (Exception e2) {

                    }
                }
            }
        };
        splashTread.start();
    }
    }

