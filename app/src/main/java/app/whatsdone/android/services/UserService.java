package app.whatsdone.android.services;

import app.whatsdone.android.model.User;

public interface UserService extends ServiceBase {
    interface Listener extends ServiceListener {
        default void onUserRetrieved(User user) {

        }
    }
    void getById(String id, Listener listener);
}
