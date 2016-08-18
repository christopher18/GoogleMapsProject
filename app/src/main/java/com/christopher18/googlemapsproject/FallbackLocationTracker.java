package com.christopher18.googlemapsproject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Chris Acker and inspired by code written by Gabriel Sechan.
 */

public class FallbackLocationTracker implements LocationTracker, LocationTracker.LocationUpdateListener {

    private boolean isRunning;

    private ProviderLocationTracker gps;
    private ProviderLocationTracker net;

    private LocationUpdateListener listener;

    Location lastLoc;
    long lastTime;

    public FallbackLocationTracker(Context context) {
        gps = new ProviderLocationTracker(context, ProviderLocationTracker.ProviderType.GPS);
        net = new ProviderLocationTracker(context, ProviderLocationTracker.ProviderType.NETWORK);
    }

    public void start(Context context) {
        if (!isRunning) {
            // Already running so do nothing
            gps.start(this, context);
            net.start(this, context);
            isRunning=true;
        }
    }

    public void start (LocationUpdateListener update, Context context) {
        start(context);
        listener = update;
    }

    public void stop (Context context) {
        if (isRunning) {
            gps.stop(context);
            net.stop(context);
            isRunning = false;
            listener = null;
        }
    }

    public boolean hasLocation () {
        // if either has location, use it
        return gps.hasLocation() || net.hasLocation();
    }

    public boolean hasPossiblyStaleLocation (Context context) {
        // if either has location, use it
        return gps.hasPossiblyStaleLocation(context) || net.hasPossiblyStaleLocation(context);
    }

    public Location getLocation() {
        Location ret = gps.getLocation();
        if (ret == null) {
            ret = net.getLocation();
        }
        return ret;
    }

    public Location getPossiblyStaleLocation (Context context) {
        Location ret = gps.getPossiblyStaleLocation(context);
        if(ret == null){
            ret = net.getPossiblyStaleLocation(context);
        }
        return ret;
    }

    public void onUpdate (Location oldLoc, long oldTime, Location newLoc, long newTime) {
        boolean update = false;

        //We should update only if there is no last location, the provider is the same,
        // or the provider is more accurate, or the old location is stale
        if ( (lastLoc == null)
                || (lastLoc.getProvider().equals(newLoc.getProvider()))
                || (newLoc.getProvider().equals(LocationManager.GPS_PROVIDER))
                || (newTime - lastTime > 5 * 60 * 1000)) {
            lastLoc = newLoc;
            lastTime = newTime;
        }
    }
}
