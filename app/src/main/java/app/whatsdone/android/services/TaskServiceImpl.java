package app.whatsdone.android.services;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.utils.Constants;

public class TaskServiceImpl implements TaskService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = GroupServiceImpl.class.getCanonicalName();
    private ListenerRegistration listener;
    private Activity context;

    public void setContext(Activity context) {
        this.context = context;
    }

    @Override
    public void subscribeForUser(String userId, ServiceListener serviceListener) {
        listener = db.collection(Constants.REF_TASKS)
                .whereEqualTo(Constants.FIELD_TASK_ASSIGNED_USER, userId)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Task subscription failed", e);
                        return;
                    }

                    List<BaseEntity> groups = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {

                        try {

                            Task task = new Task();
                            task.setDocumentID(doc.getId());
                            if (doc.get(Constants.FIELD_TASK_TITLE) != null)
                                task.setTitle(doc.getString(Constants.FIELD_TASK_TITLE));
                            if (doc.get(Constants.FIELD_TASK_DESCRIPTION) != null)
                                task.setDescription(doc.getString(Constants.FIELD_TASK_DESCRIPTION));
                            if (doc.get(Constants.FIELD_TASK_CREATED_BY) != null)
                                task.setCreatedBy(doc.getString(Constants.FIELD_TASK_CREATED_BY));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER) != null)
                                task.setAssignedUser(Constants.FIELD_TASK_ASSIGNED_USER);
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER_NAME) != null)
                                task.setAssignedUserImage(Constants.FIELD_TASK_ASSIGNED_USER_NAME);
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER_NAME) != null)
                                task.setAssignedUserName(doc.getString(Constants.FIELD_TASK_ASSIGNED_USER_NAME));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_BY) != null)
                                task.setAssignedBy(doc.getString(Constants.FIELD_TASK_ASSIGNED_BY));
                            if (doc.get(Constants.FIELD_TASK_UPDATED_AT) != null)
                                task.setUpdatedDate(doc.getDate(Constants.FIELD_TASK_UPDATED_AT));
                            groups.add(task);
                        }catch (Exception exception) {
                            Log.d(TAG, "failed to parse group", exception);
                        }

                    }

                    serviceListener.onDataReceived(groups);
                });
    }

    @Override
    public void subscribeForGroup(String groupId, ServiceListener serviceListener) {
        listener = db.collection(Constants.REF_TASKS)
                .whereEqualTo(Constants.FIELD_TASK_GROUP_ID, groupId)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Task subscription failed", e);
                        return;
                    }

                    List<BaseEntity> tasks = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {

                        try {

                            Task task = new Task();
                            task.setDocumentID(doc.getId());
                            if (doc.get(Constants.FIELD_TASK_TITLE) != null)
                                task.setTitle(doc.getString(Constants.FIELD_TASK_TITLE));
                            if (doc.get(Constants.FIELD_TASK_DESCRIPTION) != null)
                                task.setDescription(doc.getString(Constants.FIELD_TASK_DESCRIPTION));
                            if (doc.get(Constants.FIELD_TASK_CREATED_BY) != null)
                                task.setCreatedBy(doc.getString(Constants.FIELD_TASK_CREATED_BY));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER) != null)
                                task.setAssignedUser(Constants.FIELD_TASK_ASSIGNED_USER);
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER_NAME) != null)
                                task.setAssignedUserImage(Constants.FIELD_TASK_ASSIGNED_USER_NAME);
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER_NAME) != null)
                                task.setAssignedUserName(doc.getString(Constants.FIELD_TASK_ASSIGNED_USER_NAME));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_BY) != null)
                                task.setAssignedBy(doc.getString(Constants.FIELD_TASK_ASSIGNED_BY));
                            if (doc.get(Constants.FIELD_TASK_UPDATED_AT) != null)
                                task.setUpdatedDate(doc.getDate(Constants.FIELD_TASK_UPDATED_AT));
                            tasks.add(task);
                        }catch (Exception exception) {
                            Log.d(TAG, "failed to parse group", exception);
                        }

                    }

                    serviceListener.onDataReceived(tasks);
                });
    }

    @Override
    public void unSubscribe() {
        if(listener != null){
            listener.remove();
            listener = null;
        }
    }

    @Override
    public void create(BaseEntity entity, ServiceListener listener) {

    }

    @Override
    public void update(BaseEntity entity, ServiceListener listener) {

    }

    @Override
    public void delete(String documentID, ServiceListener listener) {

    }
}
