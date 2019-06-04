package app.whatsdone.android.model;

import android.widget.ImageView;

public class Group {

   // public static final int text_type = 0;


    private int groupId;
    private String groupName;
    private int timeStamp;
    private ImageView groupImage;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
      return groupName;
  }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ImageView getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(ImageView groupImage) {
        this.groupImage = groupImage;
    }
}
