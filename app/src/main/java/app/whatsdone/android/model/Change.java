package app.whatsdone.android.model;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Change {

    public enum ChangeType {
        CREATED,
        TITLE_CHANGE,
        DETAIL_CHANGE,
        STATUS_CHANGE,
        DUE_CHANGE,
        ASSIGNEE_CHANGE,
        CHECKLIST_CHANGE
    }

    private String byUser;
    private String byUserName;
    private ChangeType type;
    private Date date;
    private String valueFrom;
    private String valueTo;

    @PropertyName("by_user")
    public String getByUser() {
        return byUser;
    }

    @PropertyName("by_user")
    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    @PropertyName("by_user_name")
    public String getByUserName() {
        return byUserName;
    }

    @PropertyName("by_user_name")
    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    @PropertyName("type")
    public ChangeType getType() {
        return type;
    }

    @PropertyName("type")
    public void setType(ChangeType type) {
        this.type = type;
    }

    @PropertyName("ts")
    public Date getDate() {
        return date;
    }

    @PropertyName("ts")
    public void setDate(Date date) {
        this.date = date;
    }

    @PropertyName("value_from")
    public String getValueFrom() {
        return valueFrom;
    }

    @PropertyName("value_from")
    public void setValueFrom(String valueFrom) {
        this.valueFrom = valueFrom;
    }

    @PropertyName("value_to")
    public String getValueTo() {
        return valueTo;
    }

    @PropertyName("value_to")
    public void setValueTo(String valueTo) {
        this.valueTo = valueTo;
    }

    public Change(String byUser, String byUserName, ChangeType type, Date date, String valueFrom, String valueTo) {
        this.byUser = byUser;
        this.byUserName = byUserName;
        this.type = type;
        this.date = date;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public Change() {
    }
}
