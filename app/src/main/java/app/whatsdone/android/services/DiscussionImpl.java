package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Discussion;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.model.User;
import app.whatsdone.android.utils.Constants;
import timber.log.Timber;

public class DiscussionImpl implements DiscussionService {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = DiscussionImpl.class.getCanonicalName();
    private Query next;
    private ListenerRegistration listener;


    @Override
    public void getAllDiscussion(String groupId, ServiceListener serviceListener) {
        ArrayList<Message> discussions = new ArrayList<>();

        db.collection(Constants.REF_DISCUSSIONS)
                .whereEqualTo(Constants.FIELD_DISCUSSION_GROUP_ID, Objects.requireNonNull(groupId))
                .orderBy(Constants.FIELD_DISCUSSION_POSTED_AT, Query.Direction.DESCENDING).limit(Constants.DISCUSSION_LIMIT)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Query next = null;
                        if (task.getResult().getDocuments().size() > 0) {
                            DocumentSnapshot lastVisible = task.getResult().getDocuments()
                                    .get(task.getResult().size() - 1);
                            next = db.collection(Constants.REF_DISCUSSIONS)
                                    .whereEqualTo("group_id", Objects.requireNonNull(groupId))
                                    .orderBy("posted_at", Query.Direction.DESCENDING)
                                    .startAfter(lastVisible)
                                    .limit(10);
                        }

                        this.next = next;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = new User(document.getString("by_user"), document.getString("user_name"), checkAvatarIsEmpty(document.getString("user_image")), true);
                            Message message = new Message(document.getId(), user, document.getString("message"), document.getDate("posted_at"));
                            discussions.add(message);
                        }
                        serviceListener.onDataReceivedForMessage(discussions);
                    } else {
                        Timber.tag(TAG).w(task.getException(), "Error getting documents.");
                    }
                });
    }


    @Override
    public void insertMessage(Message getMessage, ServiceListener serviceListener) {

        DocumentReference document = db.collection(Constants.REF_DISCUSSIONS).document();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_DISCUSSION_GROUP_ID, getMessage.getId());
        data.put(Constants.FIELD_DISCUSSION_MESSAGE, getMessage.getPlainText());
        data.put(Constants.FIELD_DISCUSSION_POSTED_AT, getMessage.getCreatedAt());
        data.put(Constants.FIELD_DISCUSSION_USER_ID, getMessage.getUser().getId());
        data.put(Constants.FIELD_DISCUSSION_USER_NAME, getMessage.getUser().getName());
        data.put(Constants.FIELD_DISCUSSION_USER_AVATAR, getMessage.getUser().getAvatar());

        document.set(data).addOnCompleteListener(taskResult -> {
            if (taskResult.isSuccessful())
                serviceListener.onSuccess();
            else {
                Timber.w(taskResult.getException(), TAG, "Error creating document.");
                serviceListener.onError(taskResult.getException().getMessage());
            }
            serviceListener.onCompleted(taskResult.isSuccessful());
        });

    }

    @Override
    public void deleteMessage() {

    }

    @Override
    public void loadRestMessages(String groupId, ServiceListener serviceListener) {
        ArrayList<Message> discussions = new ArrayList<>();

        if (!(next == null)) {
            this.next.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Query next = null;
                            if (task.getResult().getDocuments().size() > 0) {
                                DocumentSnapshot lastVisible = task.getResult().getDocuments()
                                        .get(task.getResult().size() - 1);
                                next = db.collection(Constants.REF_DISCUSSIONS)
                                        .whereEqualTo("group_id", Objects.requireNonNull(groupId))
                                        .orderBy("posted_at", Query.Direction.DESCENDING)
                                        .startAfter(lastVisible)
                                        .limit(10);
                            }

                            this.next = next;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getString("by_user"), document.getString("user_name"), checkAvatarIsEmpty(document.getString("user_image")), true);
                                Message message = new Message(document.getString("by_user"), user, document.getString("message"), document.getDate("posted_at"));
                                discussions.add(message);
                            }
                            serviceListener.onDataReceivedForMessage(discussions);
                        } else {
                            Timber.tag(TAG).w(task.getException(), "Error getting documents.");
                        }
                    });
        }
    }

    @Override
    public void subscribe(String groupId, ServiceListener serviceListener) {
        listener = db.collection(Constants.REF_DISCUSSIONS)
                .whereEqualTo(Constants.FIELD_DISCUSSION_GROUP_ID, Objects.requireNonNull(groupId))
                .orderBy(Constants.FIELD_DISCUSSION_POSTED_AT, Query.Direction.DESCENDING).limit(Constants.DISCUSSION_LIMIT)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Timber.tag(TAG).w(e, "Discussion subscription failed");
                        return;
                    }

                    Query next = null;
                    if (value.getDocuments().size() > 0) {

                        DocumentSnapshot lastVisible = value.getDocuments()
                                .get(value.size() - 1);
                        next = db.collection(Constants.REF_DISCUSSIONS)
                                .whereEqualTo("group_id", Objects.requireNonNull(groupId))
                                .orderBy("posted_at", Query.Direction.DESCENDING)
                                .startAfter(lastVisible)
                                .limit(10);
                    }
                    this.next = next;
                    ArrayList<Message> messages = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        try {

                            User user = new User(document.getString("by_user"), document.getString("user_name"), checkAvatarIsEmpty(document.getString("user_image")), true);
                            Message message = new Message(document.getId(), user, document.getString("message"), document.getDate("posted_at"));
                            messages.add(message);
                        } catch (Exception exception) {
                            Timber.d(exception, "failed to parse group");
                        }

                    }

                    serviceListener.onDataReceivedForMessage(messages);
                });
    }

    @Override
    public void unSubscribe() {
        if (listener != null) {
            listener.remove();
            listener = null;
        }
    }

    private String checkAvatarIsEmpty(String url) {
        if (url == null || url.isEmpty()) {
            url = null;
        }
        return url;
    }

}
