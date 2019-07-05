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


    void removeListener(String tag);

    void registerHandler(String tag, ServiceListener handler);

    void unSubscribe(String tag);

    String add();

    void getGroupById(String groupId, ServiceListener serviceListener);

    void update(Group group, List<ExistUser> users, ServiceListener serviceListener);
    void update(Group group, ExistUser users, ServiceListener serviceListener);

    void subscribeForGroup(String id, ServiceListener serviceListener);

    void subscribe(String id, ServiceListener serviceListener);

    void removeAllListeners();
}
