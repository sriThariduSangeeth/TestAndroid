package app.whatsdone.android.services;

import app.whatsdone.android.model.User;

public interface AuthService {
    User getCurrentUser();
    void Login(String phoneNo, String verificationCode, Listener listener);
    void register(String phoneNo, Listener listener);
    void logout();

    interface Listener extends ServiceListener {
        default void onCodeSent(String mVerificationId) { }
    }
}
