package com.carpooluniversitario.carpooluniversitario;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.carpooluniversitario.carpooluniversitario.Utils.MapsActivity;
import com.carpooluniversitario.carpooluniversitario.Utils.SessionManagement;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener  {
    SessionManagement session;
Button btnCasaDestino,btnUniversidadDestino,btnfecha,btnhora,btnConductor,btnPasajero;
LinearLayout layout;
FloatingActionButton actionButton;
private Toast toast;
String[] infoViaje= new String[5];
    HashMap<String,String> hashMap;

    private DatePickerDialog dpd;
    private TimePickerDialog tpd;
    private View positiveAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }
        session = new SessionManagement(getApplicationContext());
        layout = findViewById(R.id.layoutpadre);
        btnCasaDestino = findViewById(R.id.btnCasaDestino);
        btnUniversidadDestino = findViewById(R.id.btnUniversidadDestino);
        btnfecha = findViewById(R.id.btnfecha);
        btnhora = findViewById(R.id.btnhora);
        btnConductor = findViewById(R.id.btnConductor);
        btnPasajero = findViewById(R.id.btnPasajero);

        actionButton = findViewById(R.id.fab);
        actionButton.setVisibility(View.GONE);
        actionButton.setOnClickListener(clickListener);
        btnCasaDestino.setOnClickListener(clickListener);
        btnUniversidadDestino.setOnClickListener(clickListener);
        btnfecha.setOnClickListener(clickListener);
        btnhora.setOnClickListener(clickListener);
        btnConductor.setOnClickListener(clickListener);
        btnPasajero.setOnClickListener(clickListener);




         hashMap = session.getUserDetails();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_tab_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_configuracion:
                Toast.makeText(this, "mandar a la pantalla de configuracion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_acerda:
                Toast.makeText(this, "mabdar  ala pantalla de acerda de ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_cerrar_session:
                disconnectFromFacebook();
                break;
        }

        return true;
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() != null) {
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    LoginManager.getInstance().logOut();
                }
            }).executeAsync();
        }
        goLoginScreen();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.btnCasaDestino:
                    infoViaje[0]= hashMap.get("origen_nombre");
                    infoViaje[1]= hashMap.get("destino_nombre");
                    Snackbar snackbar = Snackbar.make(layout, "Seleccionaste el origen como: "+hashMap.get("origen_nombre"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    break;

                case R.id.btnUniversidadDestino:
                    infoViaje[0]= hashMap.get("destino_nombre");
                    infoViaje[1]= hashMap.get("origen_nombre");
                    snackbar = Snackbar.make(layout, "Seleccionaste el origen como: "+hashMap.get("origen_nombre"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    break;
                    case R.id.btnfecha:
                        mostrarDialogoDeFecha();
                    break;

                case R.id.btnhora:
                    mostrarDialogoDeHora();
                    break;

                case R.id.btnPasajero:
                    snackbar = Snackbar.make(layout, "Seleccionaste ser pasajero", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    infoViaje[4]="pasajero";
                    break;

                case R.id.btnConductor:
                    snackbar = Snackbar.make(layout, "Seleccionaste ser conductor", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    infoViaje[4]="conductor";
                    break;
                case R.id.fab:
                 //   showList();
                    showCustomView();
                    break;

            }

            validarFormularioCompleto();


        }
    };

    private void validarFormularioCompleto() {

        int contador=0;
        for (int i=0;i<infoViaje.length;i++){
            if (infoViaje[i]!=null){
                //Toast.makeText(this, i+":"+infoViaje[i], Toast.LENGTH_SHORT).show();
                contador++;
                Log.d("contador", "validarFormularioCompleto: "+contador);

            }
        }
        if (contador>4){
            actionButton.setVisibility(View.VISIBLE);
        }

    }

    private void mostrarDialogoDeHora() {
        Calendar now = Calendar.getInstance();
        if (tpd == null) {
            tpd = TimePickerDialog.newInstance(
                    MainActivity.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
        } else {
            tpd.initialize(
                    MainActivity.this,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    true
            );
        }


        tpd.setTimeInterval(1, 5, 10);

        tpd.show(getFragmentManager(), "Timepickerdialog");


    }

    private void mostrarDialogoDeFecha() {
        Calendar now = Calendar.getInstance();
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    MainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    MainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }

        Calendar[] days = new Calendar[31];
        for (int i = 0; i < days.length; i++) {
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DAY_OF_MONTH, i );
            days[i] = day;
        }
        dpd.setSelectableDays(days);
        dpd.show(getFragmentManager(),"Datepickerdialog");

    }


    private void goLoginScreen() {
        Intent intent = new Intent(this, RegistroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        infoViaje[2]=date;
        Snackbar snackbar = Snackbar.make(layout, "Seleccionaste "+date, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        infoViaje[3]=hourString+":"+minuteString;
        Snackbar snackbar = Snackbar.make(layout, "Seleccionaste "+hourString+"h"+minuteString+"m", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showList() {
        new MaterialDialog.Builder(this)
                .title("Estas Seguro de estos datos?")
                .items(infoViaje)
                .positiveText("Si, estoy seguro")
                .negativeText("No, dejame cambiarlos")
                .show();
    }


    @SuppressWarnings("ResourceAsColor")
    public void showCustomView() {
        MaterialDialog dialog =
                new MaterialDialog.Builder(this)
                        .title("¿Estas seguro?")
                        .titleColor(getResources().getColor(R.color.Alizarin))
                        .customView(R.layout.dialog_customview, true)
                        .positiveText("si, estoy seguro")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent =  new Intent(getApplicationContext(),Select_location_onMap.class);
                                startActivity(intent);
                            }
                        })
                        .build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions


        // Toggling the show password CheckBox will mask or unmask the password input EditText
        CheckBox checkbox = dialog.getCustomView().findViewById(R.id.showPassword);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    positiveAction.setEnabled(true);
                }
                else{
                    positiveAction.setEnabled(false);

                }
            }
        });


        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }


}
