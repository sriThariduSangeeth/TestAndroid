package app.whatsdone.android.ui.viewmodel;

import android.database.Observable;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;

import java.util.Arrays;

import app.whatsdone.android.model.UserStatus;

public class SettingsViewModel extends BaseObservable {
    private String displayName;
    private boolean enableNotifications;
    public ObservableInt status = new ObservableInt();
    private String avatar;
    public final ObservableArrayList<String> statusTypes = new ObservableArrayList<>();

    public SettingsViewModel(String displayName, boolean enableNotifications, UserStatus status, String avatar) {
        this.displayName = displayName;
        this.enableNotifications = enableNotifications;
        this.status.set(status.getValue());
        this.avatar = avatar;
        statusTypes.addAll(Arrays.asList("Available", "Busy", "Away"));
    }

    @Bindable
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if(!this.displayName.equals(displayName)){
            this.displayName = displayName;
        }
    }

    @Bindable
    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
