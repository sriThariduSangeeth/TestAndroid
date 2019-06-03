package app.whatsdone.android.services;

import java.util.List;

import app.whatsdone.android.model.Group;

interface GroupServiceListener {
    void onGroupsUpdated(List<Group> groups);
    void onSuccess();
    void onError(String error);
}

public interface GroupService {
    void getAllGroups(String userId, GroupServiceListener groupServiceListener);
    void create(Group group,GroupServiceListener groupServiceListener );
    void update(Group group,GroupServiceListener groupServiceListener );
    void delete(String id,GroupServiceListener groupServiceListener );
    void subscribe(String userId,GroupServiceListener groupServiceListener );
    void unSubscribe();

}
