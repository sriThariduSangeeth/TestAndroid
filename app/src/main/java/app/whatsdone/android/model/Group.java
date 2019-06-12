package app.whatsdone.android.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group implements Parcelable, BaseEntity {

    private String documentID = "";
    private String groupName = "";
    private String avatar = "";
    private int discussionCount = 0;
    private int taskCount = 0;
    private int unreadDiscussionCount = 0;
    private int unreadTaskCount = 0;
    private Date updatedDate;
    private List<String> members = new ArrayList<>();
    private String createdBy = "";
    private List<String> admins = new ArrayList<>();
    private Bitmap teamImage;
    private boolean imageChanged = false;

  public void Group(String groupName , String groupId , String groupAvatar ){
      this.documentID = groupId;
      this.groupName = groupName;
      this.avatar = groupAvatar;
  }

  public Bitmap getTeamImage() {
        return teamImage;
    }

    public void setTeamImage(Bitmap teamImage) {
        this.teamImage = teamImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String getDocumentID() {
        return this.documentID;
    }

    @Override
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getDiscussionCount() {
        return discussionCount;
    }

    public void setDiscussionCount(int discussionCount) {
        this.discussionCount = discussionCount;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getUnreadDiscussionCount() {
        return unreadDiscussionCount;
    }

    public void setUnreadDiscussionCount(int unreadDiscussionCount) {
        this.unreadDiscussionCount = unreadDiscussionCount;
    }

    public int getUnreadTaskCount() {
        return unreadTaskCount;
    }

    public void setUnreadTaskCount(int unreadTaskCount) {
        this.unreadTaskCount = unreadTaskCount;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public boolean isImageChanged() {
        return imageChanged;
    }

    public void setImageChanged(boolean imageChanged) {
        this.imageChanged = imageChanged;
    }

    public Group() {

    }

    protected Group(Parcel in) {
        documentID = in.readString();
        groupName = in.readString();
        avatar = in.readString();
        discussionCount = in.readInt();
        taskCount = in.readInt();
        unreadDiscussionCount = in.readInt();
        unreadTaskCount = in.readInt();
        long tmpUpdatedDate = in.readLong();
        updatedDate = tmpUpdatedDate != -1 ? new Date(tmpUpdatedDate) : null;
        if (in.readByte() == 0x01) {
            members = new ArrayList<String>();
            in.readList(members, String.class.getClassLoader());
        } else {
            members = null;
        }
        createdBy = in.readString();
        if (in.readByte() == 0x01) {
            admins = new ArrayList<String>();
            in.readList(admins, String.class.getClassLoader());
        } else {
            admins = null;
        }
        teamImage = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentID);
        dest.writeString(groupName);
        dest.writeString(avatar);
        dest.writeInt(discussionCount);
        dest.writeInt(taskCount);
        dest.writeInt(unreadDiscussionCount);
        dest.writeInt(unreadTaskCount);
        dest.writeLong(updatedDate != null ? updatedDate.getTime() : -1L);
        if (members == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(members);
        }
        dest.writeString(createdBy);
        if (admins == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(admins);
        }
        dest.writeValue(teamImage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}