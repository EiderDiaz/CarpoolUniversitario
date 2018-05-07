package com.carpooluniversitario.carpooluniversitario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.internal.ShareConstants;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class RegistroActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                callbackManager = CallbackManager.Factory.create();
                loginButton.setReadPermissions("email","public_profile");
                // Callback registration
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String userid = loginResult.getAccessToken().getUserId();
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                               displayUserInfo(object);

                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putString(GraphRequest.FIELDS_PARAM, "first_name,last_name,email,id");
                        graphRequest.setParameters(bundle);
                        graphRequest.executeAsync();
                        goMainScreen();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "sesion cancelada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), "error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

        private void goMainScreen() {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        private void displayUserInfo(JSONObject object) {
            String firstname = "";
            String lastname = "";
            String email = "";
            String id = "";
            try {
                firstname = object.getString("first_name");
                lastname = object.getString("last_name");
                email = object.getString("email");
                id = object.getString(ShareConstants.WEB_DIALOG_PARAM_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "nombre:" + firstname + "\napellido:" + lastname + "\nemail:" + email + "\nid:" + id, Toast.LENGTH_SHORT).show();
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            this.callbackManager.onActivityResult(requestCode, resultCode, data);
        }


}
