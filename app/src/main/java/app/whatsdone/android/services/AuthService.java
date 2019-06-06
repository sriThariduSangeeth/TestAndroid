package app.whatsdone.android.services;

import android.app.Activity;

import app.whatsdone.android.model.User;

public interface AuthService {
    void Login(String phoneNo, String verificationCode, Listener listener);
    void register(String phoneNo, Listener listener);
    void logout();
    void setContext(Activity activity);
    interface Listener extends ServiceListener {
        default void onCodeSent(String mVerificationId) { }
    }
}
