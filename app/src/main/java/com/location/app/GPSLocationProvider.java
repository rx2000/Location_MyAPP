package com.location.app;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GPSLocationProvider {
    private static GPSLocationProvider self;
    private static ShowUI mListener;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Context mContext;

    private GPSLocationProvider() {
    }

    public static GPSLocationProvider getSelf() {
        if (self == null) {
            self = new GPSLocationProvider();
        }
        return self;

    }

    public GPSLocationProvider setListener(ShowUI listener) {
        GPSLocationProvider.mListener = listener;
        return this;
    }

    public void start() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        createLocationRequest();
        startLocationAndUpdates();
    }

    public void stop() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationAndUpdates() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mListener.UIUpdate(location);
                        }
                    }
                });


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    mListener.UIUpdate(location);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public GPSLocationProvider setContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    public interface ShowUI {
        void UIUpdate(Location location);
    }
}
