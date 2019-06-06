package app.whatsdone.android.services;

import java.util.List;

import app.whatsdone.android.model.Contact;

public interface ContactService {

    interface Listener {
       default void onContactsSynced(int added, int deleted) {}
       default void onCompleteSearch(boolean isExisting) {}
       default void onInvited() {}
       default void onError(String error) {
           System.out.println(error);
       }
    }

    void syncContacts(List<Contact> contacts, Listener listener);
    void existsInPlatform(List<Contact> contacts, Listener listener);
    void inviteAssignee(Contact contact, Listener listener);
    void inviteMembers(List<Contact> contacts, Listener listener);
}
