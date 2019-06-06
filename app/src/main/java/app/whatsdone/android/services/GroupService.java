package app.whatsdone.android.services;

import app.whatsdone.android.model.Group;

public interface GroupService {
    void getAllGroups(String userId, ServiceListener serviceListener);
    void create(Group group, ServiceListener serviceListener);
    void update(Group group, ServiceListener serviceListener);
    void delete(String id, ServiceListener serviceListener);
    void leave(String groupId, ServiceListener serviceListener);
    void subscribe(ServiceListener serviceListener);
    void unSubscribe();

    String add();
}
