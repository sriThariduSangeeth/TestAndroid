package app.whatsdone.android.model;

public enum UserStatus {
    available(1),
    busy(2),
    away(3);

    private int value;

    UserStatus(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
