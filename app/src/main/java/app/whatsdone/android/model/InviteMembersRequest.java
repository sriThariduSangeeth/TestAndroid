package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InviteMembersRequest {
    private List<String> members;
    private String groupId;
    private String groupTitle;

    @JsonProperty("members")
    public List<String> getMembers() {
        return members;
    }

    @JsonProperty("members")
    public void setMembers(List<String> members) {
        this.members = members;
    }

    @JsonProperty("groupId")
    public String getGroupId() {
        return groupId;
    }

    @JsonProperty("groupId")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("groupTitle")
    public String getGroupTitle() {
        return groupTitle;
    }

    @JsonProperty("groupTitle")
    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
