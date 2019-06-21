package app.whatsdone.android.services;

public interface LogService extends ServiceBase {

    void getByTaskId(String id, ServiceListener serviceListener);
}
