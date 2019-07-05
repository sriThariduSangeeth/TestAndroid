package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdateResponse {
    private boolean success;
    private int count;

    @JsonIgnore
    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonIgnore
    @JsonProperty("success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonIgnore
    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonIgnore
    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }
}
