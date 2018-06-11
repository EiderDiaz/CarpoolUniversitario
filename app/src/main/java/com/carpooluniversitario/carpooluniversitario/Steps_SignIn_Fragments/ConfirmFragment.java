package com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.carpooluniversitario.carpooluniversitario.MainActivity;
import com.carpooluniversitario.carpooluniversitario.R;
import com.carpooluniversitario.carpooluniversitario.RegistroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;


public class ConfirmFragment extends Fragment {

    public ConfirmFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Rootview= inflater.inflate(R.layout.activity_verification_phone, container, false);

        return Rootview;
    }


    private void verificarEmail(final FirebaseUser firebaseUser) {
        firebaseUser.sendEmailVerification().
                addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Re-enable button
                        //findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Verification email sent to " + firebaseUser.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("firabase", "sendEmailVerification", task.getException());
                            Toast.makeText(getContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void goMainScreen() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
