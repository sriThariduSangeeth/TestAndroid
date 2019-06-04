package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.view.CodeVerifyView;

public class CodeVerifyPresenterImpl implements CodeVerifyPresenter {

    private CodeVerifyView view;
    private String verificationID;
    private Activity context;

    @Override
    public void init(CodeVerifyView view, String verificationID) {
        this.view = view;
        this.verificationID = verificationID;
    }

    @Override
    public void verifyCode(String code) {
        AuthService service = new AuthServiceImpl();
        ((AuthServiceImpl) service).setContext(context);
        service.Login(this.verificationID, code, new AuthService.Listener() {
            @Override
            public void onSuccess() {
                view.onVerified();
            }
        });
    }

    @Override
    public void setContext(Activity context) {
        this.context = context;
    }
}
