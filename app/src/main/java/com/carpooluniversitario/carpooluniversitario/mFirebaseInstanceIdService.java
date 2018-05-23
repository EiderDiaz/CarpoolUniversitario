package com.carpooluniversitario.carpooluniversitario;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class mFirebaseInstanceIdService extends FirebaseInstanceIdService {
    String TAG = "NOTIFICACIONES";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: "+token);
    }
}
