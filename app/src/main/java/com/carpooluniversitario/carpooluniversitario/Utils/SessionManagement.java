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

   //public static final String KEY_ORIGEN_LATLNG = "origen_latlng";
    public static final String KEY_ORIGEN_NOMBRE = "origen_nombre";
    //public static final String KEY_DESTINO_LATLNG = "destino_latlng";
    public static final String KEY_DESTINO_NOMBRE = "destino_nombre";


    public static final String KEY_NUMERO_DE_CONTROL = "numero_de_control";
    public static final String KEY_SEMESTRE = "semestre";
    public static final String KEY_CARRERA = "carrera";

    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_CORREO = "correo";
    public static final String KEY_ID_FB = "id_fb";
    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_password, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String origen, String destino, String num_control, String semestre, String carrera,String nombre, String correo,String id_fb){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ORIGEN_NOMBRE,origen);
        editor.putString(KEY_DESTINO_NOMBRE,destino);
        
        editor.putString(KEY_NUMERO_DE_CONTROL,num_control);
        editor.putString(KEY_SEMESTRE,semestre);
        editor.putString(KEY_CARRERA,carrera);

        editor.putString(KEY_NOMBRE,nombre);
        editor.putString(KEY_CORREO,correo);
        editor.putString(KEY_ID_FB,id_fb);


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
        user.put(KEY_ORIGEN_NOMBRE, pref.getString(KEY_ORIGEN_NOMBRE, null));
        user.put(KEY_DESTINO_NOMBRE, pref.getString(KEY_DESTINO_NOMBRE, null));

        user.put(KEY_NUMERO_DE_CONTROL, pref.getString(KEY_NUMERO_DE_CONTROL, null));
        user.put(KEY_SEMESTRE, pref.getString(KEY_SEMESTRE, null));
        user.put(KEY_CARRERA, pref.getString(KEY_CARRERA, null));

        user.put(KEY_NOMBRE, pref.getString(KEY_NOMBRE, null));
        user.put(KEY_CORREO, pref.getString(KEY_CORREO, null));
        user.put(KEY_ID_FB, pref.getString(KEY_ID_FB, null));

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