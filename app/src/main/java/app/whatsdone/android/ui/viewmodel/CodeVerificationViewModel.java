package app.whatsdone.android.ui.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import app.whatsdone.android.BR;

public class CodeVerificationViewModel extends BaseObservable {
    private String code = "";

    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(!this.code.equals(code)) {
            this.code = code;
            notifyPropertyChanged(BR.code);
        }
    }
}
