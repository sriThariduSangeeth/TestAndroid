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
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.utils.Constants;
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
    public void getByTaskId(String id, ServiceListener serviceListener) {
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
                           event.setLogs(getChanges((List<HashMap<String, Object>>) doc.get(Constants.FIELD_LOG_LOGS)));

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

    private List<Change> getChanges(List<HashMap<String, Object>> data){
        List<Change> changes = new ArrayList<>();
        for (HashMap<String, Object> datum : data) {
            try {
                Change change = new Change(
                        (String) datum.get(Constants.FIELD_LOG_LOGS_BY_USER),
                        (String) datum.get(Constants.FIELD_LOG_LOGS_BY_USERNAME),
                        Change.ChangeType.valueOf((String) datum.get(Constants.FIELD_LOG_LOGS_TYPE)),
                        ((Timestamp) datum.get(Constants.FIELD_LOG_LOGS_DATE)).toDate(),
                        datum.get(Constants.FIELD_LOG_LOGS_VALUE_FROM) != null ?
                                datum.get(Constants.FIELD_LOG_LOGS_VALUE_FROM).toString() : "",
                        datum.get(Constants.FIELD_LOG_LOGS_VALUE_TO) != null ?
                                datum.get(Constants.FIELD_LOG_LOGS_VALUE_TO).toString() : ""

                );
                changes.add(change);
            }catch (Exception e){
                Timber.e(e);
            }
        }

        return changes;
    }
}
