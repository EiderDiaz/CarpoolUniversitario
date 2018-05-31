package com.carpooluniversitario.carpooluniversitario.Firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {
    String TAG = "NOTIFICACIONES";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
        //co0xOUkDxtI:APA91bETaKXtspqCoWnYwIt47sfdj5rhbOz1O7FHNXBsjxKizEI7_IxL8MmCIsFIpT-xpuouAcrYgb-MECuTplzW32PXMuSCLOGwPSk4f0N-9uT-O7a-cNGTZxv9eukwvMuPqR2NVHA0
        Log.d(TAG, "onTokenRefresh: "+token);
    }
}
