package app.whatsdone.android.services;

public interface TaskService extends ServiceBase {
    void subscribeForUser(ServiceListener listener);

    void getByGroupId(String groupId, ServiceListener serviceListener);

    void subscribeForGroup(String groupId, ServiceListener listener);
    void unSubscribe();

    String add();
}
