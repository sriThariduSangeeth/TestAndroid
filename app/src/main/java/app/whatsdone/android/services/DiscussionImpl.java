package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import app.whatsdone.android.model.Message;
import app.whatsdone.android.model.User;
import app.whatsdone.android.utils.Constants;

public class DiscussionImpl implements DiscussionService {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = DiscussionImpl.class.getCanonicalName();
    private List<Map<String,Objects>> finalList = new ArrayList<>();
    private Query next;


    @Override
    public void getAllDiscussion(String groupId, ServiceListener serviceListener) {
        ArrayList<Message> discussions = new ArrayList<>();

        db.collection(Constants.REF_DISCUSSIONS)
                .whereEqualTo("group_id", Objects.requireNonNull(groupId))
                .orderBy("posted_at" , Query.Direction.DESCENDING).limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Query next = null;
                        if(task.getResult().getDocuments().size() > 0){
                            DocumentSnapshot lastVisible = task.getResult().getDocuments()
                                    .get(task.getResult().size() -1);
                            next =   db.collection(Constants.REF_DISCUSSIONS)
                                    .whereEqualTo("group_id", Objects.requireNonNull(groupId))
                                    .orderBy("posted_at" , Query.Direction.DESCENDING)
                                    .startAfter(lastVisible)
                                    .limit(10);
                        }

                        this.next = next;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = new User(document.getString("by_user"),document.getString("user_name"),checkAvatarIsEmpty(document.getString("user_image")),true);
                            Message message = new Message(document.getId(),user,document.getString("message"),document.getDate("posted_at"));
                            discussions.add(message);
                        }
                        serviceListener.onDataReceivedForMessage(discussions);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    @Override
    public void insterMessage(Message getMessage, ServiceListener serviceListener) {

        Message message = (Message) getMessage;
        DocumentReference document = db.collection(Constants.REF_DISCUSSIONS).document();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_DISCUSSION_GROUP_ID, message.getId());
        data.put(Constants.FIELD_DISCUSSION_MESSAGE, message.getText());
        data.put(Constants.FIELD_DISCUSSION_POSTED_AT,message.getCreatedAt());
        data.put(Constants.FIELD_DISCUSSION_USER_ID,message.getUser().getId());
        data.put(Constants.FIELD_DISCUSSION_USER_NAME, message.getUser().getName());
        data.put(Constants.FIELD_DISCUSSION_USER_AVATAR , message.getUser().getAvatar());

        document.set(data).addOnCompleteListener(taskResult -> {
            if(taskResult.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error creating document.", taskResult.getException());
                serviceListener.onError(taskResult.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(null);
        });

    }

    @Override
    public void deleteMessage() {

    }

    @Override
    public void loadRestMessages( String groupId, ServiceListener serviceListener) {
        ArrayList<Message> discussions = new ArrayList<>();

        if(!(next == null)){
            this.next.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Query next = null;
                            if(task.getResult().getDocuments().size() > 0){
                                DocumentSnapshot lastVisible = task.getResult().getDocuments()
                                        .get(task.getResult().size() -1);
                                next =   db.collection(Constants.REF_DISCUSSIONS)
                                        .whereEqualTo("group_id", Objects.requireNonNull(groupId))
                                        .orderBy("posted_at" , Query.Direction.DESCENDING)
                                        .startAfter(lastVisible)
                                        .limit(10);
                            }

                            this.next = next;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getString("by_user"),document.getString("user_name"),checkAvatarIsEmpty(document.getString("user_image")),true);
                                Message message = new Message(document.getString("by_user"),user,document.getString("message"),document.getDate("posted_at"));
                                discussions.add(message);
                            }
                            serviceListener.onDataReceivedForMessage(discussions);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    });
        }
    }

    private String checkAvatarIsEmpty(String url){
        if(url == null || url.isEmpty()){
           url = "https://img.icons8.com/color/100/000000/user-group-man-man.png";
        }
        return url;
    }

}
