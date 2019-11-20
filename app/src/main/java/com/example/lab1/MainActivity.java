package com.example.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean isShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            isShowing = savedInstanceState.getBoolean("isShowing");
            if(isShowing)
                showAlert();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean("isShowing", isShowing);
    }

    public void onButtonClick(View view) {
        getDeviceId();
    }

    public void onButtonVersionClick(View view) {getDeviceVersion();}

    public void getDeviceVersion(){
        try{
//            Toast.makeText(this, "android id: " + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName,
//                    Toast.LENGTH_LONG).show();
            Toast.makeText(this, "android version: " + BuildConfig.VERSION_NAME,
                    Toast.LENGTH_LONG).show();

        } catch (Exception ex){}
    }

    public void requestPermissionsWithExplanation(){

        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
            showAlert();
            isShowing = true;
        } else
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

    }

    public void showAlert(){
        final String message = "We need to access this permission to get your phone ID";
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("warning")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Understood",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getDeviceId() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            Toast.makeText(this, "android id: " + telephonyManager.getDeviceId(),
                    Toast.LENGTH_LONG).show();
        } else
            requestPermissionsWithExplanation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceId();
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
