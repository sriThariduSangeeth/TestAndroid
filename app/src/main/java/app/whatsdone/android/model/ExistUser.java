package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExistUser {
    private String phoneNumber;
    private String displayName;
    private String isInvited;

    @JsonProperty("phone_no")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone_no")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("is_invited")
    public String getIsInvited() {
        return isInvited;
    }

    @JsonProperty("is_invited")
    public void setIsInvited(String isInvited) {
        this.isInvited = isInvited;
    }
}
