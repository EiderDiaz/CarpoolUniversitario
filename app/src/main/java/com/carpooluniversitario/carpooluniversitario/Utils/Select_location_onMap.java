package com.carpooluniversitario.carpooluniversitario.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.carpooluniversitario.carpooluniversitario.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Select_location_onMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    public static final int PERMISSION_REQUEST_LOCATION_CODE= 99;
    Drawable icon ;
    LatLng casa,universidad=null;
    Button btnsiguiente;
    TextView  titulo;

    private Marker currentLocationMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_on_map);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            CheckLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        icon = getResources().getDrawable(R.drawable.carpool_white_logo);
        btnsiguiente = findViewById(R.id.botonsiguiente);
        btnsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (universidad==null&& casa!=null){
                 AhoraSeleccionaEscuela();
                }
                else{
                    showMaterialDialog();

                }
            }
        });
        titulo = findViewById(R.id.titulo);

    }

    private void AhoraSeleccionaEscuela() {
        titulo.setText("Ahora selecciona Tu Universidad");
        btnsiguiente.setText("Terminar");
    }

    public Boolean CheckLocationPermission(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE);

            }
            return  false;
        }
        else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_LOCATION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                        if (client == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                if (btnsiguiente.getText().toString().equals("Terminar")) {
                    Toast.makeText(Select_location_onMap.this, "entro?", Toast.LENGTH_SHORT).show();
                    addDestinationMarker(latLng);
                } else {
                    addHomeMarker(latLng);
                }

            }
        });
    }

    private void addDestinationMarker(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title("Escuela")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .snippet("esta es mi escuela :)");
        Marker marker1 = mMap.addMarker(marker);
        marker1.showInfoWindow();
        marker1.setDraggable(true);
        MostrarMas();

        universidad=latLng;
        addHomeMarker(casa);
    }

    private void addHomeMarker(LatLng latLng) {

        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title("Casa")
                .snippet("esta es mi casita bonita :)");
        Marker marker1 = mMap.addMarker(marker);
        marker1.showInfoWindow();
        marker1.setDraggable(true);
        MostrarMas();

        casa=latLng;
    }

    private void MostrarMas() {
/*
este metodo muestra las vistas que estan ocultas
 */
    }

    private String obtenerNombreDeCiudad(LatLng latLng) {
        String cityName="";
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0)
                cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"error: "+e.getMessage(), Toast.LENGTH_SHORT).show();;
        }
        return cityName;
    }



    public void showMaterialDialog() {
        MaterialDialog builder = new MaterialDialog.Builder(this)
                .title("Hola :)")
                .backgroundColor(getResources().getColor(R.color.Clouds))
                .icon(icon)
                .limitIconToDefaultSize()
                .content("Estas seguro que esta es tu casa y tu universidad?")
                .negativeText("No")
                .positiveText("Si")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent returnIntent = new Intent();
                        Bundle args = new Bundle();
                        args.putParcelable("casa", casa);
                        args.putParcelable("universidad", universidad);
                        returnIntent.putExtra("bundle", args);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mMap.clear();

                    }
                })
                .build();

        builder.show();
    }

    @Override
    public void onBackPressed() {

        MaterialDialog builder = new MaterialDialog.Builder(this)
                .title("Aviso")
                .icon(icon)
                .limitIconToDefaultSize()
                .content("Estas seguro que quieres salir de la seleccion de ubicacion?")
                .positiveText("Si, quiero salir")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();

                    }
                })
                .build();
        builder.show();
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();


    }


    @Override
    public void onLocationChanged(Location location) {
        Location lastlocation = location;
        if (currentLocationMarker != null){
            currentLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
      //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(data.location_sam, 14f));


        if (client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client,  this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest,  this);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
