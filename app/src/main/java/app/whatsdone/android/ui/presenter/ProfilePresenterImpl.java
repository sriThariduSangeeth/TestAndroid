package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.services.UserService;
import app.whatsdone.android.ui.view.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {

    private ProfileView view;

    public ProfilePresenterImpl(ProfileView view){

        this.view = view;
    }

    @Override
    public void updateProfile(String displayName) {
        UserService service = null;
        //service.update(null,null);

        this.view.onProfileUpdated();
    }
}
