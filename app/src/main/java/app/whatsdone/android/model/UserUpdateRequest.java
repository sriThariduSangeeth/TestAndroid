package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdateRequest {
    private String username;
    private String profileImage;
    private String phoneNumber;

    @JsonProperty("display_name")
    public String getUsername() {
        return username;
    }

    @JsonProperty("display_name")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("profile_image")
    public String getProfileImage() {
        return profileImage;
    }

    @JsonProperty("profile_image")
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @JsonProperty("phone_no")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone_no")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String username, String profileImage, String phoneNumber) {
        this.username = username;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
    }
}
