package com.christopher18.googlemapsproject;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Chris Acker and inspired by code written by Gabriel Sechan.
 */
public class ProviderLocationTracker implements LocationListener, LocationTracker {
    // The minimum distance to change Updates in meters
    private static final long MIN_UPDATE_DISTANCE = 10;

    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATE_TIME = 1000*60;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private LocationManager lm;

    public enum ProviderType {
        NETWORK,
        GPS
    }
    private String provider;

    private Location lastLocation;
    private long lastTime;

    private boolean isRunning;

    private LocationUpdateListener listener;

    public ProviderLocationTracker(Context context, ProviderType type) {
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (type == ProviderType.NETWORK) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            provider = LocationManager.GPS_PROVIDER;
        }
    }

    public void start(Context context) {
        if (!isRunning) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            lm.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
            // provider is on
            isRunning = true;
            lastLocation = null;
            lastTime = 0;
        }
    }

    public void start(LocationUpdateListener update, Context context) {
        start(context);
        listener = update;
    }

    public void stop(Context context) {
        if(isRunning) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            lm.removeUpdates(this);
            isRunning = false;
            listener = null;
        }
    }
    public boolean hasLocation() {
        if ((lastLocation == null) ||
                (System.currentTimeMillis() - lastTime > 5 * MIN_UPDATE_TIME)) {
            return false;
        }
        return true;
    }

    public boolean hasPossiblyStaleLocation(Context context) {
        if (lastLocation != null) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return lm.getLastKnownLocation(provider) != null;
    }

    public Location getLocation() {
        if ((lastLocation == null) ||
                (System.currentTimeMillis() - lastTime > 5 * MIN_UPDATE_TIME)) {
            return null;
        }
        return lastLocation;
    }

    public Location getPossiblyStaleLocation(Context context) {
        if(lastLocation != null) {
            return lastLocation;
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return lm.getLastKnownLocation(provider);
    }

    public void onLocationChanged (Location newLoc) {
        long now = System.currentTimeMillis();
        if(listener != null) {
            listener.onUpdate(lastLocation, lastTime, newLoc, now);
        }
        lastLocation = newLoc;
        lastTime = now;
    }

    public void onProviderDisabled(String arg0) {

    }

    public void onProviderEnabled(String arg0) {

    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }
}
