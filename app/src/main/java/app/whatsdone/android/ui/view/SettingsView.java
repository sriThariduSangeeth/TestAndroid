package app.whatsdone.android.ui.view;

import app.whatsdone.android.model.User;

public interface SettingsView {
    void onImageEdit();
    void onLogout();

    void onProfileLoaded(User user);
}
