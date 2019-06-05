package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import app.whatsdone.android.ui.view.CodeVerifyView;

public interface CodeVerifyPresenter {
    void init(CodeVerifyView view, String verificationID);
    void verifyCode(String code);

    void setContext(Activity context);
}
