package com.rayworks.todolist;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by Shirley on 11/9/16.
 */

public class DroidApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }
}
