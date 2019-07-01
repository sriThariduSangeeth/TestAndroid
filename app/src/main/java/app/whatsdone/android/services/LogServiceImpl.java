package app.whatsdone.android.services;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.DateUtil;
import timber.log.Timber;

public class LogServiceImpl implements LogService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void create(BaseEntity entity, ServiceListener listener) {

        db.collection(Constants.REF_LOGS).document(entity.getDocumentID())
                .set(entity)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) listener.onSuccess();
                    else listener.onError(task.getException().getLocalizedMessage());
                });
    }

    @Override
    public void update(BaseEntity entity, ServiceListener listener) {
        LogEvent event = (LogEvent)entity;
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_LOG_GROUP_ID, event.getGroupId());
        data.put(Constants.FIELD_LOG_LOGS, FieldValue.arrayUnion(event.getLogs().toArray()));
        db.collection(Constants.REF_LOGS).document(entity.getDocumentID())
                .set(data, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) listener.onSuccess();
                    else listener.onError(task.getException().getLocalizedMessage());
                });
    }

    @Override
    public void delete(String documentID, ServiceListener listener) {
        db.collection(Constants.REF_LOGS).document(documentID)
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) listener.onSuccess();
                    else listener.onError(task.getException().getLocalizedMessage());
                });
    }

    @Override
    public void getByTaskId(String id, Group group, ServiceListener serviceListener) {
        db.collection(Constants.REF_LOGS).document(id)
                .get()
                .addOnCompleteListener(task -> {
                   if(!task.isSuccessful()) {
                       Timber.e(task.getException());
                       serviceListener.onError(task.getException().getLocalizedMessage());
                       return;
                   }

                   if(task.getResult() != null && task.getResult().exists()){


                       try {
                           DocumentSnapshot doc = task.getResult();
                           LogEvent event = new LogEvent();
                           event.setDocumentID(id);
                           event.setGroupId(doc.getString(Constants.FIELD_LOG_GROUP_ID));
                           event.setLogs(getChanges((List<HashMap<String, Object>>) doc.get(Constants.FIELD_LOG_LOGS), group));

                           serviceListener.onDataReceived(event);
                       } catch (Exception e) {
                           serviceListener.onError(e.getLocalizedMessage());
                           Timber.e(e);
                       }
                   }else {
                       serviceListener.onError("log does not exits for this task");
                   }


                });
    }

    private List<Change> getChanges(List<HashMap<String, Object>> data, Group group){
        List<Change> changes = new ArrayList<>();
        for (HashMap<String, Object> datum : data) {
            try {
                Change.ChangeType changeType = Change.ChangeType.valueOf((String) datum.get(Constants.FIELD_LOG_LOGS_TYPE));
                String byUser = (String) datum.get(Constants.FIELD_LOG_LOGS_BY_USER);
                Change change = new Change(
                        byUser,
                        ContactUtil.getInstance().resolveContact(byUser, group.getMemberDetails()).getDisplayName(),
                        changeType,
                        ((Timestamp) datum.get(Constants.FIELD_LOG_LOGS_DATE)).toDate(),
                        datum.get(Constants.FIELD_LOG_LOGS_VALUE_FROM) != null ?
                                getValueForChangeType(datum.get(Constants.FIELD_LOG_LOGS_VALUE_FROM), changeType): "",
                        datum.get(Constants.FIELD_LOG_LOGS_VALUE_TO) != null ?
                                getValueForChangeType(datum.get(Constants.FIELD_LOG_LOGS_VALUE_TO), changeType) : ""

                );
                changes.add(change);
            }catch (Exception e){
                Timber.d("%s", datum);
                Timber.e(e);
            }
        }

        return changes;
    }

    private String getValueForChangeType(Object data, Change.ChangeType changeType){
        switch (changeType){
            case STATUS_CHANGE:
                return Task.TaskStatus.fromInt(Integer.parseInt(data.toString())).name();
            case DUE_CHANGE:
                return DateUtil.formatted(((Timestamp)data).toDate(),null);
                default:
                    return String.valueOf(data);
        }
    }
}
