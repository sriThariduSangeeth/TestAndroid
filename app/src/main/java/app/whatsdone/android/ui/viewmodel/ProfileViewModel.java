package app.whatsdone.android.ui.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.whatsdone.android.BR;

public class ProfileViewModel extends BaseObservable {
    private String displayName;

    public ProfileViewModel(String displayName){
        this.displayName = displayName;
    }

    @Bindable
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        notifyPropertyChanged(BR.displayName);
    }
}
