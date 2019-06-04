package app.whatsdone.android.model;

public class LoginModel {
    private String countryCode;
    private String phoneNo;

    public LoginModel(){
        this.countryCode = "";
        this.phoneNo = "";
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
