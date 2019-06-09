package app.whatsdone.android.ui.presenter;

import android.app.Activity;
import android.widget.EditText;

import java.util.Map;

import javax.annotation.Nullable;

import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.view.LoginView;
import app.whatsdone.android.ui.viewmodel.LoginViewModel;

public class LoginPresenterImpl implements LoginPresenter {
    Activity context;
    LoginView view;

    public LoginPresenterImpl(LoginView view, Activity context){
        this.context = context;
        this.view = view;
    }


    @Override
    public void onNextClicked(LoginViewModel model) {
        System.out.println(model.getCountryCode() + " " + model.getPhoneNo());
        AuthService service = new AuthServiceImpl();

        if(model.getPhoneNo() != null && !model.getPhoneNo().isEmpty())
        {
            view.disableButton();

        }else
        {
            view.onValidationFailed();
        }


        service.setContext(context);
        service.register("+" + model.getCountryCode() + model.getPhoneNo(), new AuthService.Listener() {
            @Override
            public void onCodeSent(String mVerificationId) {
                view.onCodeSent(mVerificationId);
            }

            @Override
            public void onSuccess() {
                view.onVerificationCompleted("");
            }

            @Override
            public void onError(@Nullable String error) {
                view.enableNext();
            }
        });
    }
}
