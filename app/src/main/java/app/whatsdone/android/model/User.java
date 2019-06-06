package app.whatsdone.android.model;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.whatsdone.android.utils.Constants;

enum UserStatus {
    available,
    busy,
    away
}

public class User implements BaseEntity, IUser {
    private String documentID;
    private String displayName;
    private String id;
    private String phoneNo;
    private String avatar;
    private boolean enableNotifications;
    private UserStatus status;
    private List<String> deviceTokens;

    public User(String id, String name, String avatar, boolean isOnline) {
        this.documentID = id;
        this.displayName = name;
        this.avatar = avatar;
        this.status = isOnline ? UserStatus.available : UserStatus.busy;
    }

    @Override
    public String getDocumentID() {
        return this.documentID;
    }

    @Override
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    @Override
    public String getId() {
        return documentID;
    }

    @Override
    public String getName() {
        return displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public UserStatus getStatus() {
        return status;
    }

    public List<String> getDeviceTokens() {
        return deviceTokens;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setDeviceTokens(List<String> deviceTokens) {
        this.deviceTokens = deviceTokens;
    }

    public Map<String, Object> toData(){
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_USER_PHONE_NO, phoneNo);
        data.put(Constants.FIELD_USER_AVATAR, avatar);
        data.put(Constants.FIELD_USER_DEVICE_TOKENS, deviceTokens);
        data.put(Constants.FIELD_USER_DISPLAY_NAME, displayName);
        return data;
    }

    public User(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.displayName = name;
        this.avatar = avatar;
        this.online = online;
    }

    public User(){

    }

    public User(String documentID, String displayName, String phoneNo, String avatar,
                boolean enableNotifications, UserStatus status, List<String> deviceTokens) {
        this.documentID = documentID;
        this.displayName = displayName;
        this.phoneNo = phoneNo;
        this.avatar = avatar;
        this.enableNotifications = enableNotifications;
        this.status = status;
        this.deviceTokens = deviceTokens;
    }

    public User(Map<String, Object> data, String documentID){
        if (data != null) {
            String displayName = "";
            String phoneNumber = "";
            String avatar = "";
            List<String> deviceTokens = new ArrayList<>();
            boolean enableUserNotifications = false;
            UserStatus status = UserStatus.available;

            try {
                displayName = (String) data.get(Constants.FIELD_USER_DISPLAY_NAME);
                phoneNumber = (String) data.get(Constants.FIELD_USER_PHONE_NO);
                avatar = (String) data.get(Constants.FIELD_USER_AVATAR);
                deviceTokens = (List<String>) data.get(Constants.FIELD_USER_DEVICE_TOKENS);
                if ((boolean) data.get(Constants.FIELd_USER_ENABLE_NOTIFICATIONS))
                    enableUserNotifications = true;
                status = (UserStatus) data.get(Constants.FIELd_USER_STATUS);
            }catch (Exception e){
                documentID = "";
            }

            this.documentID = documentID;
            this.displayName = displayName;
            this.phoneNo = phoneNumber;
            this.avatar = avatar;
            this.deviceTokens = deviceTokens;
            this.enableNotifications = enableUserNotifications;
            this.status = status;
        }
    }
}
