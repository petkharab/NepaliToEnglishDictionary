package com.makkhay.androiddictionary;


import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class AnalyticsApplication extends Application {

    //private Tracker mTracker;
    // It is only App tracing is activated
    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        E_COMMERCE_TRACKER,
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = null;
            if(trackerId== TrackerName.APP_TRACKER){
                // add your analytic tracking number as a string parameter
                t= analytics.newTracker("UA-66280994-4");
            }
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }
}
