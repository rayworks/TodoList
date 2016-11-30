package com.rayworks.todolist;

import android.app.Application;
import android.view.View;

import com.rayworks.todolist.ui.ViewBindingForView;
import com.squareup.leakcanary.LeakCanary;

import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import timber.log.Timber;

/**
 * Created by Shirley on 11/9/16.
 */

public class DroidApp extends Application {

    private BinderFactory reusableBinderFactory;
    private static DroidApp app;

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

        reusableBinderFactory = new BinderFactoryBuilder()
                .add(new ViewBindingForView().extend(View.class))
                .build();

        app = this;
    }

    public BinderFactory getReusableBinderFactory() {
        return reusableBinderFactory;
    }

    public static DroidApp get() {
        return app;
    }
}
