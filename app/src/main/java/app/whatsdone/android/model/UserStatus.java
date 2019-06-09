package app.whatsdone.android.model;

public enum UserStatus {
    Available(0),
    Busy(1),
    Away(2);

    private int value;

    UserStatus(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserStatus forInt(int id) {
        return values()[id];
    }
}
