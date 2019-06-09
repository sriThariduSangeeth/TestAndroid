package app.whatsdone.android.ui.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import app.whatsdone.android.model.User;
import app.whatsdone.android.model.UserStatus;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.ContactServiceImpl;
import app.whatsdone.android.services.UserService;
import app.whatsdone.android.services.UserServiceImpl;
import app.whatsdone.android.ui.view.SettingsView;
import app.whatsdone.android.ui.viewmodel.SettingsViewModel;

public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private final SettingsView view;
    private UserService service = new UserServiceImpl();
    private AuthService authService = new AuthServiceImpl();
    private ContactService contactService = new ContactServiceImpl();
    private boolean isSaving = false;
    private boolean userLoaded = false;

    public SettingsPresenterImpl(SettingsView view){
        this.view = view;
    }

    @Override
    public void save(SettingsViewModel model) {
        Log.d(TAG, "save clicked");

        // debounce the save call.
        if(!isSaving && userLoaded) {
            isSaving = true;
            User user = new User();
            user.setDocumentID(AuthServiceImpl.getCurrentUser().getDocumentID());
            user.setPhoneNo(AuthServiceImpl.getCurrentUser().getDocumentID());
            user.setDisplayName(model.getDisplayName());
            user.setEnableNotifications(model.isEnableNotifications());
            user.setStatus(UserStatus.forInt(model.status.get()));
            service.update(user, new AuthService.Listener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "user saved");
                }

                @Override
                public void onError(@Nullable String error) {
                    Log.d(TAG, error);
                }

                @Override
                public void onCompleted(@Nullable Map<String, Object> data) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isSaving = false;
                        }
                    }, 600);
                }
            });
        }
    }


    @Override
    public void onImageEdit() {
        this.view.onImageEdit();
    }

    @Override
    public void syncContacts() {
        contactService.syncContacts(new ArrayList<>(), new ContactService.Listener() {
            @Override
            public void onContactsSynced(int added, int deleted) {
                Log.d(TAG, "added: " + added + "deleted: "+ deleted);
            }
        });
    }

    @Override
    public void logout() {
        authService.logout();
        view.onLogout();
    }

    @Override
    public void initUser() {
        service.getById(AuthServiceImpl.getCurrentUser().getDocumentID(), new UserService.Listener() {
            @Override
            public void onUserRetrieved(User user) {
                System.out.println("user "+  user.getDisplayName());
                if(!user.getDocumentID().isEmpty()){
                    view.onProfileLoaded(user);

                }
                userLoaded = true;
            }
        });
    }

}
