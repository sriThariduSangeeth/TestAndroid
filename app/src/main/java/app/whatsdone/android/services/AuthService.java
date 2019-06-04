package app.whatsdone.android.services;

import app.whatsdone.android.model.User;

public interface AuthService {
    User getCurrentUser();
    void Login(String phoneNo, String verificationCode, ServiceListener listener);
    void register(String phoneNo, ServiceListener listener);
    void logout();
}
