package app.whatsdone.android;

import android.app.Application;

public class WhatsDoneApplication extends Application {

    private static WhatsDoneApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static WhatsDoneApplication getApplication() {
        return application;
    }
}
