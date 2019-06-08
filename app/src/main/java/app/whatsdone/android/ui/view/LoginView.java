package app.whatsdone.android.ui.view;

public interface LoginView {
    void onCodeSent(String verificationId);
    void onVerificationCompleted(String token);

    void onValidationFailed();
    void disableButton();
}
