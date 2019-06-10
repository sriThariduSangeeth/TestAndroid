package app.whatsdone.android.services;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.utils.Constants;

public class TaskServiceImpl implements TaskService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = TaskServiceImpl.class.getSimpleName();
    private ListenerRegistration listener;

    @Override
    public void subscribeForUser(ServiceListener serviceListener) {
        listener = db.collection(Constants.REF_TASKS)
                .whereEqualTo(Constants.FIELD_TASK_ASSIGNED_USER, AuthServiceImpl.getCurrentUser().getDocumentID())
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
                .orderBy(Constants.FIELD_TASK_DUE_AT, Query.Direction.ASCENDING)
                .limit(Constants.TASKS_LIMIT)
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
                                task.setAssignedUser(doc.getString(Constants.FIELD_TASK_ASSIGNED_USER));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER_NAME) != null)
                                task.setAssignedUserImage(doc.getString(Constants.FIELD_TASK_ASSIGNED_USER_IMAGE));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_USER_NAME) != null)
                                task.setAssignedUserName(doc.getString(Constants.FIELD_TASK_ASSIGNED_USER_NAME));
                            if (doc.get(Constants.FIELD_TASK_ASSIGNED_BY) != null)
                                task.setAssignedBy(doc.getString(Constants.FIELD_TASK_ASSIGNED_BY));
                            if (doc.get(Constants.FIELD_TASK_UPDATED_AT) != null)
                                task.setUpdatedDate(doc.getDate(Constants.FIELD_TASK_UPDATED_AT));
                            if (doc.get(Constants.FIELD_TASK_STATUS) != null)
                                task.setStatus(Task.TaskStatus.fromInt(doc.getLong(Constants.FIELD_TASK_STATUS).intValue()));
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
    public void create(BaseEntity entity, ServiceListener serviceListener) {
        Task task = (Task)entity;
        DocumentReference document = db.collection(Constants.REF_TASKS).document();
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_TASK_TITLE, task.getTitle());
        data.put(Constants.FIELD_TASK_GROUP_ID, task.getGroupId());
        data.put(Constants.FIELD_TASK_GROUP_NAME, task.getGroupName());
        data.put(Constants.FIELD_TASK_DESCRIPTION, task.getDescription());
        data.put(Constants.FIELD_TASK_ASSIGNED_BY, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        data.put(Constants.FIELD_TASK_ASSIGNED_USER, task.getAssignedUser());
        data.put(Constants.FIELD_TASK_ASSIGNED_USER_NAME, task.getAssignedUserName());
        data.put(Constants.FIELD_TASK_ASSIGNED_USER_IMAGE, "");
        data.put(Constants.FIELD_TASK_CREATED_BY, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        data.put(Constants.FIELD_TASK_STATUS, task.getStatus().getValue());
        data.put(Constants.FIELD_TASK_UPDATED_AT, new Date());
        data.put(Constants.FIELD_TASK_DUE_AT, task.getDueDate());
        data.put(Constants.FIELD_TASK_CREATED_AT, new Date());
        List<Object> checkListItems = new ArrayList<>();
        for (CheckListItem item :
                task.getCheckList()) {
            Map<String, Object> checkListItem = new HashMap<>();
            checkListItem.put(Constants.FIELD_TASK_CHECKLIST_TITLE, item.getTitle());
            checkListItem.put(Constants.FIELD_TASK_CHECKLIST_COMPLETED, item.isCompleted());
            checkListItems.add(checkListItem);
        }
        data.put(Constants.FIELD_TASK_CHECKLIST, checkListItems);

        document.set(data).addOnCompleteListener(taskResult -> {
            if(taskResult.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error creating document.", taskResult.getException());
                serviceListener.onError(taskResult.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(taskResult.isSuccessful());
        });
    }

    @Override
    public void update(BaseEntity entity, ServiceListener serviceListener) {
        Task task = (Task)entity;
        DocumentReference document = db.collection(Constants.REF_TASKS).document(entity.getDocumentID());
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_TASK_TITLE, task.getTitle());
        data.put(Constants.FIELD_TASK_DESCRIPTION, task.getDescription());
        data.put(Constants.FIELD_TASK_ASSIGNED_BY, task.getAssignedBy());
        data.put(Constants.FIELD_TASK_ASSIGNED_USER, task.getAssignedUser());
        data.put(Constants.FIELD_TASK_ASSIGNED_USER_NAME, task.getAssignedUserName());
        data.put(Constants.FIELD_TASK_ASSIGNED_USER_IMAGE, task.getAssignedBy());
        data.put(Constants.FIELD_TASK_STATUS, task.getStatus().getValue());
        data.put(Constants.FIELD_TASK_UPDATED_AT, new Date());
        List<Object> checkListItems = new ArrayList<>();
        for (CheckListItem item :
                task.getCheckList()) {
            Map<String, Object> checkListItem = new HashMap<>();
            checkListItem.put(Constants.FIELD_TASK_CHECKLIST_TITLE, item.getTitle());
            checkListItem.put(Constants.FIELD_TASK_CHECKLIST_COMPLETED, item.isCompleted());
            checkListItems.add(checkListItem);
        }
        data.put(Constants.FIELD_TASK_CHECKLIST, checkListItems);

        document.update(data).addOnCompleteListener(taskResult -> {
            if(taskResult.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error creating document.", taskResult.getException());
                serviceListener.onError(taskResult.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(taskResult.isSuccessful());
        });
    }

    @Override
    public void delete(String id, ServiceListener serviceListener) {
        db.collection(Constants.REF_TASKS)
                .document(id)
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        serviceListener.onSuccess();
                    }else {
                        Log.w(TAG, "Error deleting document.", task.getException());
                        serviceListener.onError(task.getException().getLocalizedMessage());
                    }
                    serviceListener.onCompleted(task.isSuccessful());
                });

    }
}
