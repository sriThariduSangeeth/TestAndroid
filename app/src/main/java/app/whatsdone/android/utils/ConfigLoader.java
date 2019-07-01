package app.whatsdone.android.utils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import app.whatsdone.android.BuildConfig;
import app.whatsdone.android.R;
import timber.log.Timber;

public class ConfigLoader {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    public void fetch() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        fetchWelcome();
    }

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
    private void fetchWelcome() {
        // [START fetch_config_with_callback]
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener((task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Timber.d("Config params updated: %s", updated);

                    } else {
                        return;
                    }
                    readInitialValues();
                }));
        // [END fetch_config_with_callback]
    }

    private void readInitialValues() {
        Long taskLimit = mFirebaseRemoteConfig.getLong(Constants.CONFIG_TASK_LIMIT);
        Constants.TASKS_LIMIT = taskLimit.intValue();

        Long messageLimit = mFirebaseRemoteConfig.getLong(Constants.CONFIG_MESSAGE_LIMIT);
        Constants.DISCUSSION_LIMIT = messageLimit.intValue();

    }
}
