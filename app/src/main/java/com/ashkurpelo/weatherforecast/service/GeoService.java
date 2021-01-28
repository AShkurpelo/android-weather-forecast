package com.ashkurpelo.weatherforecast.service;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class GeoService extends Service {
    private final String TAG = "GeoService";
    Location mLastLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;


    public GeoService() {
        super();
    }

    public final static String ACTION_LOCATION_RESULT = "com.ashkurpelo.weatherforecast.GeoService.LOCATION_RESULT";
    public final static String EXTRA_RESULTOK_OUT = "com.ashkurpelo.weatherforecast.GeoService.EXTRA_RESULTOK_OUT";
    public final static String EXTRA_EXCEPTION_OUT = "com.ashkurpelo.weatherforecast.GeoService.EXTRA_EXCEPTION_OUT";
    public final static String EXTRA_LOCATION_OUT = "com.ashkurpelo.weatherforecast.GeoService.EXTRA_LOCATION_OUT";
    public final static String EXTRA_CITY_OUT = "com.ashkurpelo.weatherforecast.GeoService.EXTRA_CITY_OUT";
    public final static String EXTRA_CITY_LOCALIZED_OUT =
            "com.ashkurpelo.weatherforecast.GeoService.EXTRA_CITY_LOCALIZED_OUT";

    public final static String EXTRA_FORCE_UPDATE = "com.ashkurpelo.weatherforecast.GeoService.EXTRA_FORCE_UPDATE";
    boolean forceUpdate = false;

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "OnLocationResult (updates service): " + locationResult.toString());
            mLastLocation = locationResult.getLastLocation();
            try {
                sendResult();
                fusedLocationClient.removeLocationUpdates(this);
                stopSelf();
            } catch (Throwable t) {
                Log.e(TAG, "Error when sending result: " + t.getLocalizedMessage(), t);
            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Log.d(TAG, "onLocationAvailability: " + locationAvailability.isLocationAvailable());
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            forceUpdate = intent.getBooleanExtra(EXTRA_FORCE_UPDATE, false);
        }
        initLocationServices();
        return START_NOT_STICKY;
    }

    private void initLocationServices() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(GeoService.this);

        createLocationRequest();
        requestSettings();

        Log.d(TAG, "clients set up");
    }

    private void onSettingsReady() {
        requestLastLocation();
        requestLocationUpdates();
    }

    private void requestSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "Location settings response: " + locationSettingsResponse);
                onSettingsReady();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error while getting location settings", e);
                onSettingsRequestFailure(e);
            }
        });
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onDestroy() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback);
        super.onDestroy();
    }

    /**
     * Looks for last known location. Sets up location update requests
     */
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Illegal State: Missing GPS permission", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Illegal State: Missing GPS permission");
            return;
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private void sendResult() throws IOException {
        Intent resultIntent = new Intent();

        resultIntent.setAction(ACTION_LOCATION_RESULT)
                    .addCategory(Intent.CATEGORY_DEFAULT);
        String location_str = "lat=" + mLastLocation.getLatitude() + "&lon=" + mLastLocation.getLongitude();
        resultIntent.putExtra(EXTRA_LOCATION_OUT, location_str);

        Geocoder geocoder = new Geocoder(GeoService.this, Locale.getDefault());
        List<Address> list = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
        String cityStr = list.get(0).getLocality();

        if (list.size() > 0) {
            resultIntent.putExtra(EXTRA_CITY_OUT, cityStr);
            resultIntent.putExtra(EXTRA_CITY_LOCALIZED_OUT, list.get(0).getLocality());
        }
        resultIntent.putExtra(EXTRA_RESULTOK_OUT, true);

        Log.d(TAG, "Sending result: " + location_str + "; City: " + cityStr);
        sendBroadcast(resultIntent);
    }

    private void sendErrorResponse() {
        Log.i(TAG, "GoogleApiCallbacks/sendErrorResponse");
        if (forceUpdate) {
            initLocationServices();
        }
        Intent resultIntent = new Intent()
                .setAction(ACTION_LOCATION_RESULT)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .putExtra(EXTRA_RESULTOK_OUT, false);
        sendBroadcast(resultIntent);
    }

    private void sendErrorResponse(ResolvableApiException e) {
        Log.i(TAG, "GoogleApiCallbacks/sendErrorResponse+ResolvableAPIException");
        if (forceUpdate) {
            initLocationServices();
        }
        Intent resultIntent = new Intent()
                .setAction(ACTION_LOCATION_RESULT)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .putExtra(EXTRA_RESULTOK_OUT, false)
                .putExtra(EXTRA_EXCEPTION_OUT, e);
        sendBroadcast(resultIntent);
    }

    private void onSettingsRequestFailure(Exception e) {
        if (e instanceof ResolvableApiException) {
            ResolvableApiException resolvable = (ResolvableApiException) e;
            sendErrorResponse(resolvable);
        } else {
            sendErrorResponse();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLastLocation() {
        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d(TAG, "Got location: " + location);
                            mLastLocation = location;
                            try {
                                sendResult();
                            } catch (Throwable t) {
                                Log.e(TAG, "Error when sending result: " + t.getLocalizedMessage(), t);
                            }
                        } else {
                            Log.e(TAG, "On location success, but location = null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting last location: " + e.getLocalizedMessage(), e);
                        sendErrorResponse();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d(TAG, "get last location complete: success=" + task.isSuccessful());
                    }
                });
    }
}
