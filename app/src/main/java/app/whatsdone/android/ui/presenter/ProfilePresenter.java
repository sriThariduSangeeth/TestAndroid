package app.whatsdone.android.ui.presenter;

import android.graphics.Bitmap;

public interface ProfilePresenter {
    void updateProfile(String displayName);

    void setProfileImage(Bitmap image);
}
