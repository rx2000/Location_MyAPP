package com.location.app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;

public class MainActivity extends AppCompatActivity implements GPSLocationProvider.ShowUI {

    private TextView textViewLatitude;
    private TextView textViewLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLatitude = findViewById(R.id.textViewLatitude);
        textViewLongitude = findViewById(R.id.textViewLongitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(DialogOnDeniedPermissionListener.Builder.withContext(this)
                        .withTitle("Permission Needed")
                        .withMessage("Permission is required to determine your location")
                        .withButtonText(android.R.string.ok)
                        .build())
                .check();
        GPSLocationProvider.getSelf().setContext(this).setListener(this).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GPSLocationProvider.getSelf().stop();
    }

    @Override
    public void UIUpdate(Location location) {
        textViewLatitude.setText(String.format("%s", location.getLatitude()));
        textViewLongitude.setText(String.format("%s", location.getLongitude()));
    }
}