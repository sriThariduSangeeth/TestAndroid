package app.whatsdone.android.services;

import android.graphics.Bitmap;

public interface StorageService  {
    interface Listener {
        default void onSuccess(String url) {}
        default void onError(String error) {}
    }
    void uploadUserImage(Bitmap image, Listener listener);
    void uploadGroupImage(Bitmap image, String groupId, Listener listener);
}
