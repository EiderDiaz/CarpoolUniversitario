package com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carpooluniversitario.carpooluniversitario.R;


public class BienvenidoFragment extends Fragment {


    public BienvenidoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Rootview = inflater.inflate(R.layout.fragment_bienvenido, container, false);
        AppCompatButton appCompatButton = Rootview.findViewById(R.id.btnContinuar);
       final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);

            }
        });
        return  Rootview;


    }




}
