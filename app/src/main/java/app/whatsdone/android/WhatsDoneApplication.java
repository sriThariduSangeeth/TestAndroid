package app.whatsdone.android;

import android.app.Application;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import timber.log.Timber;

public class WhatsDoneApplication extends Application {

    private static WhatsDoneApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                (paramThread, paramThrowable) -> {
                    //Do your own error handling here
                    logEvent(paramThrowable);

                    if (oldHandler != null)
                        oldHandler.uncaughtException(
                                paramThread,
                                paramThrowable
                        ); //Delegates to Android's error handling
                    else
                        System.exit(2); //Prevents the service/app from freezing
                });
    }

    public static WhatsDoneApplication getApplication() {
        return application;
    }

    private void logEvent(Throwable ex){
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(application);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, ex.hashCode()+ "");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ex.getMessage());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "string");
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}
