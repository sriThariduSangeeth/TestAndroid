package app.whatsdone.android.ui.presenter;

import android.graphics.Bitmap;

import app.whatsdone.android.ui.viewmodel.SettingsViewModel;

public interface SettingsPresenter {
    void save(SettingsViewModel model);
    void onImageEdit();
    void syncContacts();
    void logout();
    void initUser();

    void uploadUserImage(Bitmap image);
}
