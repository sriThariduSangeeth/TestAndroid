package app.whatsdone.android.ui.presenter;

import android.graphics.Bitmap;

import javax.annotation.Nullable;

import app.whatsdone.android.model.User;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.StorageService;
import app.whatsdone.android.services.StorageServiceImpl;
import app.whatsdone.android.services.UserService;
import app.whatsdone.android.services.UserServiceImpl;
import app.whatsdone.android.ui.view.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {

    private ProfileView view;
    UserService service = new UserServiceImpl();
    AuthService authService = new AuthServiceImpl();
    StorageService storageService = new StorageServiceImpl();
    private Bitmap image;

    public ProfilePresenterImpl(ProfileView view){

        this.view = view;
    }

    @Override
    public void updateProfile(String displayName) {

        User user = AuthServiceImpl.getCurrentUser();
        user.setDisplayName(displayName);
        try{
            if(image != null) {
                storageService.uploadUserImage(image, new StorageService.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        user.setAvatar(url);
                        service.update(user, new UserService.Listener() {
                            @Override
                            public void onSuccess() {
                                authService.updateProfile(user, new AuthService.Listener() {
                                    @Override
                                    public void onSuccess() {
                                        view.dismiss();

                                    }

                                    @Override
                                    public void onError(@Nullable String error) {
                                        view.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onError(@Nullable String error) {
                                view.dismiss();
                            }
                        });
                    }

                });
            }else {
                service.update(user, new UserService.Listener() {
                    @Override
                    public void onSuccess() {
                        authService.updateProfile(user, new AuthService.Listener() {
                            @Override
                            public void onSuccess() {
                                view.dismiss();

                            }

                            @Override
                            public void onError(@Nullable String error) {
                                view.dismiss();

                            }
                        });
                    }

                    @Override
                    public void onError(@Nullable String error) {
                        view.dismiss();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        this.view.onProfileUpdated();
    }

    @Override
    public void setProfileImage(Bitmap image) {
        this.image = image;
    }
}
