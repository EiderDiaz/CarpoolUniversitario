package com.carpooluniversitario.carpooluniversitario.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.carpooluniversitario.carpooluniversitario.RegistroActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class SessionManagement {
    // Shared Preferences
     SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file password
    private static final String PREF_password = "SessionPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // User password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Email address (make variable public to access from outside)
    public static final String KEY_TOKEN = "token";

    public static final String KEY_ORIGEN_LATLNG = "origen_latlng";
    public static final String KEY_ORIGEN_NOMBRE = "origen_nombre";

    public static final String KEY_DESTINO_LATLNG = "destino_latlng";
    public static final String KEY_DESTINO_NOMBRE = "destino_nombre";


    public static final String KEY_NUMERO_DE_CONTROL = "numero_de_control";
    public static final String KEY_SEMESTRE = "semestre";
    public static final String KEY_CARRERA = "semestre";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_password, PRIVATE_MODE);
        editor = pref.edit();
    }



    
    /**
     * Create login session
     * */
    public void createLoginSession(String email, String password, String token, LatLng origen){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_ORIGEN_LATLNG,origen.toString());
        editor.putString(KEY_ORIGEN_NOMBRE,origen.toString());
        editor.putString(KEY_DESTINO_LATLNG,origen.toString());
        editor.putString(KEY_DESTINO_NOMBRE,origen.toString());
        editor.putString(KEY_ORIGEN_LATLNG,origen.toString());
        editor.putString(KEY_ORIGEN_LATLNG,origen.toString());
        editor.putString(KEY_ORIGEN_LATLNG,origen.toString());

        // commit changes
        editor.apply();
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
            Intent i = new Intent(_context, RegistroActivity.class);
            // Closing all the Activities
            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TOP | i.FLAG_ACTIVITY_CLEAR_TASK |i.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
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
        Intent i = new Intent(_context, RegistroActivity.class);
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