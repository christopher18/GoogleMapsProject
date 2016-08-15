package com.christopher18.googlemapsproject;

import android.content.Context;
import android.location.Location;

/**
 * Created by chris on 8/14/16.
 */
public interface LocationTracker {
    interface LocationUpdateListener{
        void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime);
    }

    void start(Context context);

    void start(LocationUpdateListener update, Context context);

    void stop(Context context);

    boolean hasLocation();

    boolean hasPossiblyStaleLocation(Context context);

    Location getLocation();

    Location getPossiblyStaleLocation(Context context);
}
