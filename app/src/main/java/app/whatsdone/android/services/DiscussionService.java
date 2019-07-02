package app.whatsdone.android.services;

import app.whatsdone.android.model.Message;
import app.whatsdone.android.model.MessageFormatter;

public interface DiscussionService {

    void getAllDiscussion(String groupId , ServiceListener serviceListener);

    void insertMessage(Message getMessage , ServiceListener serviceListener);

    void deleteMessage ();

    void loadRestMessages( String groupId , ServiceListener serviceListener);

    void subscribe(String id, MessageFormatter formatter, ServiceListener serviceListener);

    void unSubscribe();


}
