package com.carpooluniversitario.carpooluniversitario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carpooluniversitario.carpooluniversitario.Utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.internal.ShareConstants;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class RegistroActivity extends AppCompatActivity implements Validator.ValidationListener{

    CallbackManager callbackManager;
    LoginButton loginButton;
    @NotEmpty(message = "Esta campo es obligatorio")
    EditText txt_origen,txt_destino,txt_numeroDeControl,txt_carrera,txt_semestre;
    Validator validator;
    SessionManagement session;
    String firstname = "";
    String lastname = "";
    String email = "";
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        txt_origen = findViewById(R.id.txtorigen);
        txt_destino = findViewById(R.id.txtdestino);
        txt_numeroDeControl = findViewById(R.id.txtmatricula);
        txt_carrera = findViewById(R.id.txtcarrera);
        txt_semestre = findViewById(R.id.txtsemestre);
        validator= new Validator(this);
        validator.setValidationListener(this);
        session = new SessionManagement(getApplicationContext());



            loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarSesionFacebook();

            }
        });





    }

    private void IniciarSesionFacebook() {
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
                validator.validate();

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

    private void goMainScreen() {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        private void displayUserInfo(JSONObject object) {

            try {
                firstname = object.getString("first_name");
                lastname = object.getString("last_name");
                email = object.getString("email");
                id = object.getString(ShareConstants.WEB_DIALOG_PARAM_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
           // Toast.makeText(this, "nombre:" + firstname + "\napellido:" + lastname + "\nemail:" + email + "\nid:" + id, Toast.LENGTH_SHORT).show();
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            this.callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    @Override
    public void onValidationSucceeded() {
        session.createLoginSession(txt_origen.getText().toString()
                , txt_destino.getText().toString()
                , txt_numeroDeControl.getText().toString()
                , txt_semestre.getText().toString()
                , txt_carrera.getText().toString()
                ,firstname+""+lastname, email,id);
        goMainScreen();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
