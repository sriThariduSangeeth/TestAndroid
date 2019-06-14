package app.whatsdone.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class ExistUser implements Parcelable {
    private String phoneNumber;
    private String displayName;
    private String isInvited;


    @JsonProperty("phone_no")
    @PropertyName("phone_no")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone_no")
    @PropertyName("phone_no")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("display_name")
    @PropertyName("display_name")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    @PropertyName("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("is_invited")
    @PropertyName("is_invited")
    public String getIsInvited() {
        return isInvited;
    }

    @JsonProperty("is_invited")
    @PropertyName("is_invited")
    public void setIsInvited(String isInvited) {
        this.isInvited = isInvited;
    }

    public ExistUser(){

    }

    protected ExistUser(Parcel in) {
        phoneNumber = in.readString();
        displayName = in.readString();
        isInvited = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(displayName);
        dest.writeString(isInvited);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExistUser> CREATOR = new Parcelable.Creator<ExistUser>() {
        @Override
        public ExistUser createFromParcel(Parcel in) {
            return new ExistUser(in);
        }

        @Override
        public ExistUser[] newArray(int size) {
            return new ExistUser[size];
        }
    };
}
