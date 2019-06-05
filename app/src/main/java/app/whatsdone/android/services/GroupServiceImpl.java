package app.whatsdone.android.services;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.utils.Constants;

public class GroupServiceImpl implements GroupService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = GroupServiceImpl.class.getCanonicalName();
    private ListenerRegistration listener;
    private Activity context;

    public void setContext(Activity context) {
        this.context = context;
    }

    @Override
    public void getAllGroups(String userId, ServiceListener serviceListener) {
        List<BaseEntity> groups = new ArrayList<>();

        db.collection(Constants.REF_TEAMS)
                .whereArrayContains(Constants.FIELD_GROUP_MEMBERS, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Group group = new Group();
                            group.setDocumentID(doc.getId());
                            if (doc.get(Constants.FIELD_GROUP_TITLE) != null)
                                group.setGroupName(doc.getString(Constants.FIELD_GROUP_TITLE));
                            if (doc.get(Constants.FIELD_GROUP_AVATAR) != null)
                                group.setAvatar(doc.getString(Constants.FIELD_GROUP_AVATAR));
                            if (doc.get(Constants.FIELD_GROUP_CREATED_BY) != null)
                                group.setCreatedBy(doc.getString(Constants.FIELD_GROUP_CREATED_BY));
                            if (doc.get(Constants.FIELD_GROUP_DISCUSSION_COUNT) != null)
                                group.setDiscussionCount(doc.getLong(Constants.FIELD_GROUP_DISCUSSION_COUNT).intValue());
                            if (doc.get(Constants.FIELD_GROUP_TASKS_COUNT) != null)
                                group.setTaskCount(doc.getLong(Constants.FIELD_GROUP_TASKS_COUNT).intValue());
                            if (doc.get(Constants.FIELD_GROUP_UPDATED_AT) != null)
                                group.setUpdatedDate(doc.getDate(Constants.FIELD_GROUP_UPDATED_AT));
                            if (doc.get(Constants.FIELD_GROUP_MEMBERS) != null)
                                group.setMembers((List<String>) doc.get(Constants.FIELD_GROUP_MEMBERS));

                            groups.add(group);
                            serviceListener.onDataReceived(groups);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        serviceListener.onError(task.getException().getLocalizedMessage());
                    }
                    serviceListener.onCompleted(null);
                });
    }

    @Override
    public void create(Group group, ServiceListener serviceListener) {

        DocumentReference document = db.collection(Constants.REF_TEAMS).document();
        HashMap<String, Object>  data = new HashMap<>();
        data.put(Constants.FIELD_GROUP_TITLE, group.getGroupName());
        data.put(Constants.FIELD_GROUP_CREATED_BY, group.getCreatedBy());
        data.put(Constants.FIELD_GROUP_TASKS_COUNT, 0);
        data.put(Constants.FIELD_GROUP_DISCUSSION_COUNT, 0);
        data.put(Constants.FIELD_GROUP_MANAGED_BY_ADMIN, true);
        data.put(Constants.FIELD_GROUP_ENABLE_USER_TASKS, true);
        data.put(Constants.FIELD_GROUP_MEMBERS, group.getMembers());
        data.put(Constants.FIELD_GROUP_AVATAR, group.getAvatar());
        data.put(Constants.FIELD_GROUP_UPDATED_AT, new Date());

        document.set(data).addOnCompleteListener(context, task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error creating document.", task.getException());
                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(null);
        });
    }

    @Override
    public void update(Group group, ServiceListener serviceListener) {
        DocumentReference document = db.collection(Constants.REF_TEAMS).document(group.getDocumentID());
        HashMap<String, Object>  data = new HashMap<>();
        data.put(Constants.FIELD_GROUP_TITLE, group.getGroupName());
        data.put(Constants.FIELD_GROUP_MEMBERS, group.getMembers());
        data.put(Constants.FIELD_GROUP_AVATAR, group.getAvatar());
        data.put(Constants.FIELD_GROUP_UPDATED_AT, new Date());

        document.update(data).addOnCompleteListener(context, task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error updating document.", task.getException());

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(null);
        });

    }

    @Override
    public void delete(String id, ServiceListener serviceListener) {
        db.collection(Constants.REF_TEAMS)
                .document(id)
                .delete()
                .addOnCompleteListener(context, task -> {
                   if(task.isSuccessful()) {
                    serviceListener.onSuccess();
                   }else {
                       Log.w(TAG, "Error deleting document.", task.getException());
                       serviceListener.onError(task.getException().getLocalizedMessage());
                   }
                    serviceListener.onCompleted(null);
                });


    }

    @Override
    public void subscribe(ServiceListener serviceListener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        listener = db.collection(Constants.REF_TEAMS)
                .whereArrayContains(Constants.FIELD_GROUP_MEMBERS, user.getPhoneNumber())
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Team subscription failed", e);
                        return;
                    }

                    List<BaseEntity> groups = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {

                        try {

                            Group group = new Group();
                            group.setDocumentID(doc.getId());
                            if (doc.get(Constants.FIELD_GROUP_TITLE) != null)
                                group.setGroupName(doc.getString(Constants.FIELD_GROUP_TITLE));
                            if (doc.get(Constants.FIELD_GROUP_AVATAR) != null)
                                group.setAvatar(doc.getString(Constants.FIELD_GROUP_AVATAR));
                            if (doc.get(Constants.FIELD_GROUP_CREATED_BY) != null)
                                group.setCreatedBy(doc.getString(Constants.FIELD_GROUP_CREATED_BY));
                            if (doc.get(Constants.FIELD_GROUP_DISCUSSION_COUNT) != null)
                                group.setDiscussionCount(doc.getLong(Constants.FIELD_GROUP_DISCUSSION_COUNT).intValue());
                            if (doc.get(Constants.FIELD_GROUP_TASKS_COUNT) != null)
                                group.setTaskCount(doc.getLong(Constants.FIELD_GROUP_TASKS_COUNT).intValue());
                            if (doc.get(Constants.FIELD_GROUP_UPDATED_AT) != null)
                                group.setUpdatedDate(doc.getDate(Constants.FIELD_GROUP_UPDATED_AT));
                            if (doc.get(Constants.FIELD_GROUP_MEMBERS) != null)
                                group.setMembers((List<String>) doc.get(Constants.FIELD_GROUP_MEMBERS));
                            groups.add(group);
                        }catch (Exception exception) {
                            Log.d(TAG, "failed to parse group", exception);
                        }

                    }

                    serviceListener.onDataReceived(groups);
                });
    }

    @Override
    public void unSubscribe() {
        if( listener != null) {
            listener.remove();
            listener = null;
        }
    }
}
