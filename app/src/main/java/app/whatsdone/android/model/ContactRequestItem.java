package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactRequestItem {
    private String ContactNo;
    private String ContactName;

    @JsonProperty("contact_no")
    public String getContactNo() {
        return ContactNo;
    }

    @JsonProperty("contact_no")
    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    @JsonProperty("contact_name")
    public String getContactName() {
        return ContactName;
    }


    @JsonProperty("contact_name")
    public void setContactName(String contactName) {
        ContactName = contactName;
    }
}
