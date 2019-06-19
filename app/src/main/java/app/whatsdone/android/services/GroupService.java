package app.whatsdone.android.services;

import java.io.IOException;
import java.util.List;

import app.whatsdone.android.model.ExistUser;
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

    void getGroupById(String groupId, ServiceListener serviceListener);

    void update(Group group, List<ExistUser> users, ServiceListener serviceListener);

    void subscribe(String id, ServiceListener serviceListener);
}
