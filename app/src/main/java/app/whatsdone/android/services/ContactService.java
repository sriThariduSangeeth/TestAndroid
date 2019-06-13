package app.whatsdone.android.services;

import java.util.List;

import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;

public interface ContactService {


    public interface Listener {
       default void onContactsSynced(int added, int deleted) {}
       default void onCompleteSearch(List<ExistUser> users, List<String> isExisting) {}
       default void onInvited() {}
       default void onError(String error) {
           System.out.println(error);
       }
    }

    void syncContacts(List<Contact> contacts, Listener listener);
    void existsInPlatform(List<String> contacts, Listener listener);
    void inviteAssignee(Contact contact, Group group, Task task, Listener serviceListener);
    void inviteMembers(List<String> members, Group group, Listener serviceListener);
}
