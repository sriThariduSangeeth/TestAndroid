package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteMembersResponse {
    private boolean success;

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
