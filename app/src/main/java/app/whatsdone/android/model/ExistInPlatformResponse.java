package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExistInPlatformResponse {
    private List<String> existNumbers;
    private List<ExistUser> users;

    @JsonProperty("existNumbers")
    public List<String> getExistNumbers() {
        return existNumbers;
    }

    @JsonProperty("existNumbers")
    public void setExistNumbers(List<String> existNumbers) {
        this.existNumbers = existNumbers;
    }

    @JsonProperty("users")
    public List<ExistUser> getUsers() {
        return users;
    }

    @JsonProperty("users")
    public void setUsers(List<ExistUser> users) {
        this.users = users;
    }
}
