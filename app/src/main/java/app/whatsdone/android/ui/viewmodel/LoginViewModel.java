package app.whatsdone.android.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import app.whatsdone.android.BR;

public class LoginViewModel extends BaseObservable {
    private String countryCode;
    private String phoneNo;

    public LoginViewModel(){
        countryCode = "94";
        phoneNo = "714411600";
    }

    @Bindable
    public String getCountryCode(){
        return countryCode;
    }

    public void setCountryCode(String value){
        System.out.println(value);
        if (countryCode != value) {
            countryCode = value;
            notifyPropertyChanged(BR.countryCode);
        }
    }

    @Bindable
    public String getPhoneNo(){
        return phoneNo;
    }

    public void setPhoneNo(String value){
        System.out.println(value);
        if (phoneNo != value) {
            phoneNo = value;
            notifyPropertyChanged(BR.phoneNo);
        }
    }

}
