package app.whatsdone.android.services;

public interface UserService extends ServiceBase {
    void getById(String id, ServiceListener listener);
}
