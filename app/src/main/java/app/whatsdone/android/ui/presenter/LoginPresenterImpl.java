package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.viewmodel.LoginViewModel;

public class LoginPresenterImpl implements LoginPresenter {
    @Override
    public void onNextClicked(LoginViewModel model) {
        System.out.println(model.getCountryCode() + " " + model.getPhoneNo());
    }
}
