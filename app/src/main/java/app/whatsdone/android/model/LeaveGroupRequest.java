package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaveGroupRequest {
    private String groupId;

    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
