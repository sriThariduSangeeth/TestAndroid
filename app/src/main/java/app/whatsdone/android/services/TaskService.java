package app.whatsdone.android.services;

public interface TaskService extends ServiceBase {
    void subscribeForUser(ServiceListener listener);
    void subscribeForGroup(String groupId, ServiceListener listener);
    void unSubscribe();

    String add();
}
