package app.whatsdone.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.whatsdone.android.utils.TextUtil;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Task implements BaseEntity, Parcelable {

    public Task getClone() {
        Task task;
        try {
            Parcel parcel = Parcel.obtain();
            this.writeToParcel(parcel,0);
            parcel.setDataPosition(0);
            task = Task.CREATOR.createFromParcel(parcel);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return task;
    }

    private Task deserialize(String data) {
        if(TextUtil.isNullOrEmpty(data)) return null;

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        Task obj = null;

        try {
            obj = mapper.readValue(data, Task.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private static  String serialize(Object obj, boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        String output = "";
        try {

            if (pretty) {
                output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }else {
                output = mapper.writeValueAsString(obj);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return output;
    }

    public enum TaskStatus {
        TODO(0),
        IN_PROGRESS(10),
        ON_HOLD(20),
        DONE(30);

        private final int value;

        TaskStatus(int value) {
            this.value = value;
        }

        public static TaskStatus fromInt(int intValue) {
            switch (intValue){
                case 0:
                    return TaskStatus.TODO;
                case 10:
                    return TaskStatus.IN_PROGRESS;
                case 20:
                    return TaskStatus.ON_HOLD;
                case 30:
                    return TaskStatus.DONE;

                    default:
                        return TaskStatus.TODO;
            }
        }

        public static int getIndex(TaskStatus status) {
            switch (status) {

                case TODO:
                    return 0;
                case IN_PROGRESS:
                    return 1;
                case ON_HOLD:
                    return 2;
                case DONE:
                    return 3;
            }
            return 0;
        }

        public int getValue() {
            return value;
        }
    }

    private String documentID = "";
    private String title = "";
    private String description = "";
    private Date dueDate = new Date();
    private String groupId;
    private String groupName;
    private String assignedUser = "";
    private String assignedUserName = "";
    private String assignedUserImage = "";
    private String assigneeComment = "";
    private String assignedBy;
    private String createdBy;
    private TaskStatus status = TaskStatus.TODO;
    private Date updatedDate;
    private List<CheckListItem> checkList = new ArrayList<>();
    private boolean unreadTask = false;
    private boolean acknowledge = false;



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

    public String getAssigneeComment() {
        return assigneeComment;
    }

    public void setAssigneeComment(String assigneeComment) {
        this.assigneeComment = assigneeComment;
    }

    public void setUnreadTask(boolean unreadTask) {
        this.unreadTask = unreadTask;
    }

    public boolean isUnreadTask() {
        return unreadTask;
    }

    public void setAcknowledged(boolean acknowledge) {
        this.acknowledge = acknowledge;
    }

    public boolean isAcknowledged() {
        return acknowledge;
    }


    public Task() {
    }



    protected Task(Parcel in) {
        documentID = in.readString();
        title = in.readString();
        description = in.readString();
        long tmpDueDate = in.readLong();
        dueDate = tmpDueDate != -1 ? new Date(tmpDueDate) : null;
        groupId = in.readString();
        groupName = in.readString();
        assignedUser = in.readString();
        assignedUserName = in.readString();
        assignedUserImage = in.readString();
        assigneeComment = in.readString();
        assignedBy = in.readString();
        createdBy = in.readString();
        acknowledge = in.readByte() != 0x00;
        status = TaskStatus.valueOf(in.readString());

        long tmpUpdatedDate = in.readLong();
        updatedDate = tmpUpdatedDate != -1 ? new Date(tmpUpdatedDate) : null;
        if (in.readByte() == 0x01) {
            checkList = new ArrayList<>();
            in.readList(checkList, CheckListItem.class.getClassLoader());
        } else {
            checkList = new ArrayList<>();;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentID);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(dueDate != null ? dueDate.getTime() : -1L);
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeString(assignedUser);
        dest.writeString(assignedUserName);
        dest.writeString(assignedUserImage);
        dest.writeString(assigneeComment);
        dest.writeString(assignedBy);
        dest.writeString(createdBy);
        dest.writeByte((byte) (acknowledge ? 0x01 : 0x00));
        dest.writeString(status.name());
        dest.writeLong(updatedDate != null ? updatedDate.getTime() : -1L);
        if (checkList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(checkList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

}
