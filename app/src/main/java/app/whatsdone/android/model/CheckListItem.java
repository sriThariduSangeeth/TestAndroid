package app.whatsdone.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckListItem implements Parcelable {
    private String title;
    private boolean isCompleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CheckListItem(){}

    protected CheckListItem(Parcel in) {
        title = in.readString();
        isCompleted = in.readByte() != 0x00;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (isCompleted ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CheckListItem> CREATOR = new Parcelable.Creator<CheckListItem>() {
        @Override
        public CheckListItem createFromParcel(Parcel in) {
            return new CheckListItem(in);
        }

        @Override
        public CheckListItem[] newArray(int size) {
            return new CheckListItem[size];
        }
    };
}
