package app.whatsdone.android.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import app.whatsdone.android.BR;

public class CodeVerificationViewModel extends BaseObservable {
    private String code;

    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
