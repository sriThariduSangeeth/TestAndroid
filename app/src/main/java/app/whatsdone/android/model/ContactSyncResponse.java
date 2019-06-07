package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactSyncResponse {
    private boolean success;
    private int added;
    private int deleted;

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonProperty("added")
    public int getAdded() {
        return added;
    }

    @JsonProperty("added")
    public void setAdded(int added) {
        this.added = added;
    }

    @JsonProperty("deleted")
    public int getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
