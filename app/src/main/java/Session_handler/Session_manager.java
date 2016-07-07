package Session_handler;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ToxicBakery.viewpager.transforms.example.Login_Screen;

public class Session_manager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Turnst_pref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String Acess_Token = "acess_token";

    // Email address (make variable public to access from outside)
    public static final String device_id = "device_id";

    public static final String User_id = "userid";
    public static final String Email = "email";
    public static final String Username = "User_name";
    public static final String fIrst_name = "first_name";
    public static final String Bioo = "Bio";
    public static final String Phone = "phone";
    public static final String Gender = "gender";
    public static final String WEbsite = "website";
    public static final String Profile_PIC = "profile_image";
//    session.createLoginSession(access_tocken, Ostype,userid,Email,username,First_name,bio
//    ,phone,gender,website,profile_image);
    // Constructor
    public Session_manager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String acesstoken, String deviceid,String userid,String email,String username,String First_name,String bio,String phone,String gender,String website,String profile_image){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing Acesstoken in pref
        editor.putString(Acess_Token, acesstoken);

        // Storing device_id in pref
        editor.putString(device_id, deviceid);
        editor.putString(User_id, userid);
        editor.putString(Email, email);
        editor.putString(Username, username);
        editor.putString(fIrst_name, First_name);
        editor.putString(Bioo, bio);
        editor.putString(Phone, phone);
        editor.putString(Gender, gender);
        editor.putString(WEbsite, website);
        editor.putString(Profile_PIC, profile_image);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login_Screen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(Acess_Token, pref.getString(Acess_Token, null));

        // user email id
        user.put(device_id, pref.getString(device_id, null));
        user.put(User_id, pref.getString(User_id, null));
        user.put(Email, pref.getString(Email, null));
        user.put(Username, pref.getString(Username, null));
        user.put(fIrst_name, pref.getString(fIrst_name, null));
        user.put(Bioo, pref.getString(Bioo, null));
        user.put(Phone, pref.getString(Phone, null));
        user.put(Gender, pref.getString(Gender, null));
        user.put(WEbsite, pref.getString(WEbsite, null));
        user.put(Profile_PIC, pref.getString(Profile_PIC, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login_Screen.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}