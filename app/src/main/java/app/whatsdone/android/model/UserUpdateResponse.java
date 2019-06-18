package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdateResponse {
    private boolean success;
    private int count;

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(int count) {
        this.count = count;
    }
}
