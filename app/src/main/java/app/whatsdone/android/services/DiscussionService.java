package app.whatsdone.android.services;

import com.google.firebase.firestore.DocumentSnapshot;

import app.whatsdone.android.model.Message;

public interface DiscussionService {

    void getAllDiscussion(String groupId , ServiceListener serviceListener);

    void insterMessage(Message getMessage ,  ServiceListener serviceListener);

    void deleteMessage ();

    void loadRestMessages( String groupId , ServiceListener serviceListener);


}
