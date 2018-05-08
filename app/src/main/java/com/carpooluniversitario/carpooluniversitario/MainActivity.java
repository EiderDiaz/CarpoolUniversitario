package com.carpooluniversitario.carpooluniversitario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.carpooluniversitario.carpooluniversitario.Utils.SessionManagement;
import com.facebook.AccessToken;

import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivity extends AppCompatActivity {
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManagement(getApplicationContext());

       /* if (AccessToken.getCurrentAccessToken() != null) {
            goLoginScreen();
        }*/

        HashMap<String,String> hashMap = session.getUserDetails();
        String origen_nombre = hashMap.get("origen_nombre");
        String destino_nombre = hashMap.get("destino_nombre");
        Toast.makeText(this, "origen"+origen_nombre+"\ndestino"+destino_nombre, Toast.LENGTH_SHORT).show();

    }


    private void goLoginScreen() {
        Intent intent = new Intent(this, RegistroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
