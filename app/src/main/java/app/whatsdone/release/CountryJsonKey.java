package app.whatsdone.release;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CountryJsonKey {

    private String name;
    private String dialCode;
    private String countycode;

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("dial_code")
    public String getDialCode() {
        return dialCode;
    }

    @JsonGetter("code")
    public String getCountycode() {
        return countycode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public void setCountycode(String countycode) {
        this.countycode = countycode;
    }


}
