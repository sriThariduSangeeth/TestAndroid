package app.whatsdone.android.model;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.List;

public class LogEvent implements BaseEntity {
    private String documentID = "";
    private List<Change> logs = new ArrayList<>();
    private String groupId = "";

    @Override
    public String getDocumentID() {
        return this.documentID;
    }

    @Override
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    @PropertyName("logs")
    public List<Change> getLogs() {
        return logs;
    }

    @PropertyName("logs")
    public void setLogs(List<Change> logs) {
        this.logs = logs;
    }

    @PropertyName("group_id")
    public String getGroupId() {
        return groupId;
    }

    @PropertyName("group_id")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
