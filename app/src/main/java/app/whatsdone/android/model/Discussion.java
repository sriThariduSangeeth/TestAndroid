package app.whatsdone.android.model;

import android.app.Dialog;

public class Discussion implements BaseEntity {

    private String userName;
    private String userId;
    private String groupId;
    private String message;
    private String postedAt;
    private String avatar;

//    public Discussion(String userName ,String userId , String groupId , String message , String postedAt , String avatar){
//        this.userName = userName;
//        this.userId = userId;
//        this.groupId = groupId;
//        this.message = message;
//        this.postedAt = postedAt;
//        this.avatar = avatar;
//
//    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId =  String.valueOf(groupId);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    @Override
    public String getDocumentID() {
        return null;
    }

    @Override
    public void setDocumentID(String documentID) {

    }
}
