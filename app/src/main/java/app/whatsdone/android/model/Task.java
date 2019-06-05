package app.whatsdone.android.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task implements BaseEntity {

    enum TaskStatus {
        TODO(10),
        IN_PROGRESS(20),
        ON_HOLD(30),
        DONE(50);

        private final int value;

        TaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String documentID;
    private String title;
    private String description;
    private Date dueDate;
    private String groupId;
    private String groupName;
    private String assignedUser;
    private String assignedUserName;
    private String assignedUserImage;
    private String assignedBy;
    private String createdBy;
    private TaskStatus status;
    private Date updatedDate;
    private List<CheckListItem> checkList;



    @Override
    public String getDocumentID() {
        return this.documentID;
    }

    @Override
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String taskName) {
        this.title = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public String getAssignedUserImage() {
        return assignedUserImage;
    }

    public void setAssignedUserImage(String assignedUserImage) {
        this.assignedUserImage = assignedUserImage;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<CheckListItem> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckListItem> checkList) {
        this.checkList = checkList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Task() {
    }


}
