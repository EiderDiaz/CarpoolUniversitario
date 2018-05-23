package com.carpooluniversitario.carpooluniversitario;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class mFirebaseMessagingService extends FirebaseMessagingService {
private final String TAG = "NOTIFICATION X2";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
            String from = remoteMessage.getFrom();
        Log.d(TAG, "mensaje recibido de : "+from);

        if (remoteMessage.getNotification() != null){
            Log.d(TAG, "notificacion: "+remoteMessage.getNotification().getBody());
        }

    }
}
