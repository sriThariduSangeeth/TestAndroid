package app.whatsdone.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteAssigneeRequest {
    private String assignee;
    private String taskId;
    private String taskTitle;
    private String groupTitle;

    @JsonProperty("assignee")
    public String getAssignee() {
        return assignee;
    }


    @JsonProperty("assignee")
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }


    @JsonProperty("taskId")
    public String getTaskId() {
        return taskId;
    }



    @JsonProperty("taskId")
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }



    @JsonProperty("taskTitle")
    public String getTaskTitle() {
        return taskTitle;
    }



    @JsonProperty("taskTitle")
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    @JsonProperty("groupTitle")
    public String getGroupTitle() {
        return groupTitle;
    }


    @JsonProperty("groupTitle")
    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
