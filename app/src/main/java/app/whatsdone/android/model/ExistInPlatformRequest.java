package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExistInPlatformRequest {

    private List<String> contacts;

    @JsonProperty("contacts")
    public List<String> getContacts() {
        return contacts;
    }

    @JsonProperty("contacts")
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

}
