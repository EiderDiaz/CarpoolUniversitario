package com.carpooluniversitario.carpooluniversitario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments.BienvenidoFragment;
import com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments.CasaDataFragment;
import com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments.ConfirmFragment;
import com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments.EscuelaDataFragment;
import com.carpooluniversitario.carpooluniversitario.Steps_SignIn_Fragments.PersonalDataFragment;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class StepsSignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocationManager locationManager;
    private Provider provider;
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HideActionBarWhenLandscape();

        setContentView(R.layout.activity_steps_sign_up);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        verificarPermisosDeUbicacion();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void verificarPermisosDeUbicacion() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {



        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }
    private void HideActionBarWhenLandscape() {
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide(); //<< this
        }
    }







    private void setupViewPager(ViewPager viewPager) {
       // adapter.addFragment(new BienvenidoFragment(), "BIENVENIDO");
        adapter.addFragment(new CasaDataFragment(), "CASA");
        adapter.addFragment(new EscuelaDataFragment(), "ESCUELA");
        //adapter.addFragment(new PersonalDataFragment(), "TU");
        adapter.addFragment(new ConfirmFragment(), "CONFIRMAR");
        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }




}
