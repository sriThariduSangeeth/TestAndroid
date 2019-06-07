package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExistInPlatformResponse {
    private List<String> existNumbers;

    @JsonProperty("existNumbers")
    public List<String> getExistNumbers() {
        return existNumbers;
    }

    @JsonProperty("existNumbers")
    public void setExistNumbers(List<String> existNumbers) {
        this.existNumbers = existNumbers;
    }
}
