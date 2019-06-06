package app.whatsdone.android.model;

import java.util.Date;
import java.util.List;

public class Group implements BaseEntity {

  private String documentID = "";
  private String groupName;
  private String avatar;
  private int discussionCount = 0;
  private int taskCount = 0;
  private int unreadDiscussionCount = 0;
  private int unreadTaskCount = 0;
  private Date updatedDate;
  private List<String> members;
  private String createdBy;
  private List<String> admins;

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
}
