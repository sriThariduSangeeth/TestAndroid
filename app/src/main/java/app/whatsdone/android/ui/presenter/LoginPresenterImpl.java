package app.whatsdone.android.ui.presenter;

import android.app.Activity;
import android.content.Context;

import java.util.concurrent.Executor;

import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.viewmodel.LoginViewModel;

public class LoginPresenterImpl implements LoginPresenter {
    Activity context;

    public void setContext(Activity context) {
        this.context = context;
    }

    @Override
    public void onNextClicked(LoginViewModel model) {
        System.out.println(model.getCountryCode() + " " + model.getPhoneNo());
        AuthService service = new AuthServiceImpl();

        ((AuthServiceImpl) service).setContext(context);
        service.register("+" + model.getCountryCode() + model.getPhoneNo(), new ServiceListener() {
            @Override
            public void onSuccess() {
                System.out.println("verified");
            }
        });
    }
}
