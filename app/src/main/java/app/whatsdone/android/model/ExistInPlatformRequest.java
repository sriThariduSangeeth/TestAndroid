package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExistInPlatformRequest {

    private List<ContactRequestItem> contacts;

    @JsonProperty("contacts")
    public List<ContactRequestItem> getContacts() {
        return contacts;
    }

    @JsonProperty("contacts")
    public void setContacts(List<ContactRequestItem> contacts) {
        this.contacts = contacts;
    }

}
