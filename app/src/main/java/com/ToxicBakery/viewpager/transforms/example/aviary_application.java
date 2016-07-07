//package com.ToxicBakery.viewpager.transforms.example;
//
//import android.app.Application;
//
//import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
//import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;
//
///**
// * Created by Nirmal on 5/24/2016.
// */
//public class aviary_application extends Application implements IAdobeAuthClientCredentials {
//
//    /* Be sure to fill in the two strings below. */
//    private static final String CREATIVE_SDK_CLIENT_ID = "d18bb5e529b34bdf9544044949db93e7";
//    private static final String CREATIVE_SDK_CLIENT_SECRET = "889d9116-68e9-4ba0-8c54-9bc888c8d90a";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
//    }
//
//    @Override
//    public String getClientID() {
//        return CREATIVE_SDK_CLIENT_ID;
//    }
//
//    @Override
//    public String getClientSecret() {
//        return CREATIVE_SDK_CLIENT_SECRET;
//    }
//}