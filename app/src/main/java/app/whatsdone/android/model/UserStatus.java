package app.whatsdone.android.model;

public enum UserStatus {
    Available(1),
    Busy(2),
    Away(3);

    private int value;

    UserStatus(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserStatus forInt(int id) {
        return values()[id - 1];
    }
}
