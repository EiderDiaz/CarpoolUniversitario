package com.carpooluniversitario.carpooluniversitario;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carpooluniversitario.carpooluniversitario.Utils.Select_location_onMap;
import com.carpooluniversitario.carpooluniversitario.Utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    Button buttonRegitro;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        session = new SessionManagement(getApplicationContext());
        buttonRegitro = findViewById(R.id.botonregistrarse);
        buttonRegitro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StepsSignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        txt_origen = findViewById(R.id.txtorigen);
        txt_destino = findViewById(R.id.txtdestino);
        txt_numeroDeControl = findViewById(R.id.txtmatricula);
        txt_semestre = findViewById(R.id.txtsemestre);
        txt_carrera = findViewById(R.id.txtcarrera);

        validator= new Validator(this);
        validator.setValidationListener(this);

        txt_origen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),Select_location_onMap.class);
                startActivityForResult(intent,1);
            }
        });


            loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarSesionFacebook();

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    goMainScreen();
                }

            }
        };





    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(RegistroActivity.this, "value is "+value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("error", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void IniciarSesionFacebook() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email","public_profile");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GetUserDataFromFacebook(loginResult);
                // TODO: 22/05/2018 hacer un wizard para poner el inicio de sesion con facebook en un fragmento o actividad unica que no interfiera con los campos de escolares
                validator.validate();
                handleFacebookAccesToken(loginResult.getAccessToken());

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

    private void GetUserDataFromFacebook(LoginResult loginResult) {
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
    }

    private void handleFacebookAccesToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ((task.isSuccessful())) {
                    Toast.makeText(getApplicationContext(), "todo bien", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "error en autenticacion"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroActivity.this, "fallo: "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
            if (requestCode == 1) {
                if(resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getParcelableExtra("bundle");
                     LatLng fromPosition = bundle.getParcelable("casa");
                    LatLng toPosition = bundle.getParcelable("universidad");
                    txt_origen.setText(fromPosition.toString());
                    txt_destino.setText(toPosition.toString());
                    //Toast.makeText(this, fromPosition+" : "+toPosition, Toast.LENGTH_SHORT).show();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
            else{
                super.onActivityResult(requestCode, resultCode, data);
                this.callbackManager.onActivityResult(requestCode, resultCode, data);
            }

        }


    @Override
    public void onValidationSucceeded() {
        session.createLoginSession(txt_origen.getText().toString()
                , txt_destino.getText().toString()
                , txt_numeroDeControl.getText().toString()
                , txt_semestre.getText().toString()
                , txt_carrera.getText().toString()
                ,firstname+""+lastname, email,id);

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
