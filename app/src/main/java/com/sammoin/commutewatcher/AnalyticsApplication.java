package com.sammoin.commutewatcher;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by DoctorMondo on 5/23/2016.
 */
public class AnalyticsApplication extends Application {
    private Tracker mTracker;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.string.tracker_id);
        }
        return mTracker;
    }
}