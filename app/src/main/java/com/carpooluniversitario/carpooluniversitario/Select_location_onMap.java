package com.carpooluniversitario.carpooluniversitario;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Select_location_onMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private Location lastlocation;
    private LocationRequest locationRequest;
    public static final int PERMISSION_REQUEST_LOCATION_CODE= 99;

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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latLng.latitude, latLng.longitude)).title("New Marker");
                mMap.addMarker(marker);
                // TODO: 13/11/2017 conversion de coordenadas a ciudad o localidad
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses;
                String cityName="";
                try {
                    addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0)
                        cityName = addresses.get(0).getLocality();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"error: "+e.getMessage(), Toast.LENGTH_SHORT).show();;
                }
                Toast.makeText(getApplicationContext(), "LATITUD :"+latLng.latitude+", LONGITUD :"+latLng.longitude+"\nCIUDAD:"+cityName, Toast.LENGTH_SHORT).show();
                showChangeLangDialog(String.valueOf(latLng.latitude),String.valueOf(latLng.longitude),cityName);

            }
        });
    }
    public void showChangeLangDialog(final String lat,final  String lng,final  String city) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        //final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        //dialogBuilder.setView(dialogView);
        //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        dialogBuilder.setTitle("Selecciona Ubicación");
        dialogBuilder.setMessage("Estas seguro que quieres obtener el clima de esta ubicación ("+city+")?");
        dialogBuilder.setPositiveButton("Si, quiero esta ubicación", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // TODO: 13/11/2017 startatcityty for resutl
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat",lat);
                returnIntent.putExtra("lng",lng);
                returnIntent.putExtra("city",city);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        dialogBuilder.setNegativeButton("No, quiero otra ubicación", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMap.clear();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        //final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        //dialogBuilder.setView(dialogView);
        //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        dialogBuilder.setTitle("Aviso");
        dialogBuilder.setMessage("Estas seguro que quieres salir de la seleccion de ubicacion?");
        dialogBuilder.setPositiveButton("Si, quiero salir ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        dialogBuilder.setNegativeButton("No, quiero seguir en este menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

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
        lastlocation = location;
        if (currentLocationMarker != null){
            currentLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
       /* MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

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
