package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContactSyncRequest {

    private List<ContactRequestItem> contacts;
    private String byUser;

    @JsonProperty("contacts")
    public List<ContactRequestItem> getContacts() {
        return contacts;
    }

    @JsonProperty("contacts")
    public void setContacts(List<ContactRequestItem> contacts) {
        this.contacts = contacts;
    }

    @JsonProperty("byUser")
    public String getByUser() {
        return byUser;
    }

    @JsonProperty("byUser")
    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

}
