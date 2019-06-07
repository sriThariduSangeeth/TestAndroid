package app.whatsdone.android.services;

import java.util.List;

import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;

public interface ContactService {


    interface Listener {
       default void onContactsSynced(int added, int deleted) {}
       default void onCompleteSearch(List<String> isExisting) {}
       default void onInvited() {}
       default void onError(String error) {
           System.out.println(error);
       }
    }

    void syncContacts(List<Contact> contacts, Listener listener);
    void existsInPlatform(List<Contact> contacts, Listener listener);
    void inviteAssignee(Contact contact, Group group, Task task, Listener serviceListener);
    void inviteMembers(List<String> members, Group group, Listener serviceListener);
}