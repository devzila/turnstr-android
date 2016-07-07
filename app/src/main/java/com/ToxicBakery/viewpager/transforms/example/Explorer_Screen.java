package com.ToxicBakery.viewpager.transforms.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Service_Handler.Constant;
import Service_Handler.ServiceHandler;
import adapter.Grid_view_adapter;
import adapter.explore_gridviewadapter;
import circular_imagview.ProgressHUD;

public class Explorer_Screen extends Activity {
    ProgressHUD mProgressHUD;
    GridView gridView;
    ArrayList<HashMap<String, String>> List_Array = new ArrayList<HashMap<String, String>>();
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_explorer__screen);
         gridView = (GridView) findViewById(R.id.gridview);
        new Explorer().execute();
        // Instance of ImageAdapter Class
        //gridView.setAdapter(new Grid_view_adapter(this,List_Array));

    }

    private class Explorer extends AsyncTask<Void, String, Void> implements DialogInterface.OnCancelListener {
        String android_id;
        String myVersion, Hardware;
        JSONObject jsonnode;
        String str;
        String Deviceid, Token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //mProgressHUD = ProgressHUD.show(Explorer_Screen.this, "Connecting", true, true, this);
            pDialog = new ProgressDialog(Explorer_Screen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            android_id = Settings.Secure.getString(Explorer_Screen.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            myVersion = android.os.Build.VERSION.RELEASE;
            Hardware = Build.MANUFACTURER;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            publishProgress("Please wait...");

            ServiceHandler sh = new ServiceHandler();
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//            nameValuePairs.add(new BasicNameValuePair("access_token", Access_token));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constant.Explorer,
                    ServiceHandler.POST);

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
                    JSONArray Array_image = null;
                    Array_image = jsonObj.getJSONArray("data");
                    for (int i = 0; i < Array_image.length(); i++) {
                        JSONObject c = Array_image.getJSONObject(i);

                        String media_url = c.getString("media1_url");



                        // tmp hashmap for single contact
                        HashMap<String, String> Explorer = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        Explorer.put("media_url", media_url);


                        // adding contact to contact list
                        List_Array.add(Explorer);
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

            pDialog.dismiss();
            explore_gridviewadapter adapter = new explore_gridviewadapter(Explorer_Screen.this,
                    android.R.layout.simple_list_item_1, List_Array,
                    getApplication());
            gridView.setAdapter(adapter);


        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
