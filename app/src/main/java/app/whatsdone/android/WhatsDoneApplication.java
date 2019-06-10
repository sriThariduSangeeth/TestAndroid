package app.whatsdone.android;

import android.app.Application;

public class WhatsDoneApplication extends Application {

    private static WhatsDoneApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        Thread.setDefaultUncaughtExceptionHandler((paramThread, paramThrowable) -> paramThrowable.printStackTrace());
    }

    public static WhatsDoneApplication getApplication() {
        return application;
    }
}
