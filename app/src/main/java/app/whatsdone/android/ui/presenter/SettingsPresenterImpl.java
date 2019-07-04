package app.whatsdone.android.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import app.whatsdone.android.model.User;
import app.whatsdone.android.model.UserStatus;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.ContactServiceImpl;
import app.whatsdone.android.services.StorageService;
import app.whatsdone.android.services.StorageServiceImpl;
import app.whatsdone.android.services.UserService;
import app.whatsdone.android.services.UserServiceImpl;
import app.whatsdone.android.ui.view.SettingsView;
import app.whatsdone.android.ui.viewmodel.SettingsViewModel;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import timber.log.Timber;

public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private final SettingsView view;
    private SettingsViewModel model;
    private UserService service = new UserServiceImpl();
    private AuthService authService = new AuthServiceImpl();
    private ContactService contactService = new ContactServiceImpl();
    private boolean isSaving = false;
    private boolean userLoaded = false;
    private boolean isChanged = false;
    private Context context;

    public SettingsPresenterImpl(SettingsView view, SettingsViewModel model, Context context){
        this.view = view;
        this.model = model;
        this.context = context;
    }

    @Override
    public void save(SettingsViewModel model) {
        Timber.tag(TAG).d("save clicked");

        // debounce the save call.
        if(!isSaving && userLoaded && isChanged) {
            isSaving = true;
            User user = new User();
            user.setDocumentID(AuthServiceImpl.getCurrentUser().getDocumentID());
            user.setPhoneNo(AuthServiceImpl.getCurrentUser().getDocumentID());
            user.setDisplayName(model.getDisplayName());
            user.setAvatar(model.getAvatar());
            user.setEnableNotifications(model.isEnableNotifications());
            user.setStatus(UserStatus.forInt(model.status.get()));
            service.update(user, new AuthService.Listener() {
                @Override
                public void onSuccess() {
                    Timber.d("user saved");
                    Toast toast = Toast.makeText(context, "Your changes have been saved successfully", Toast.LENGTH_LONG);
                    toast.show();
                    authService.updateProfile(user, new AuthService.Listener() {
                        @Override
                        public void onSuccess() {
                            Timber.d("profile saved");
                        }
                    });
                }

                @Override
                public void onError(@Nullable String error) {
                    Timber.d(error);
                }

                @Override
                public void onCompleted(boolean isSuccessful) {
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
        contactService.syncContacts(ContactUtil.getInstance().getContacts(), new ContactService.Listener() {
            @Override
            public void onContactsSynced(int added, int deleted) {
                Timber.tag(TAG).d("added: " + added + "deleted: " + deleted);
            }
        });
    }

    @Override
    public void logout() {
        authService.logout();
        SharedPreferencesUtil.save(Constants.SHARED_TOKEN, "");
        view.onLogout();
    }

    @Override
    public void initUser() {
        service.getById(AuthServiceImpl.getCurrentUser().getDocumentID(), new UserService.Listener() {
            @Override
            public void onUserRetrieved(User user) {
                System.out.println("user "+  user.getDisplayName());
                if(user.getDocumentID() != null && !user.getDocumentID().isEmpty()){
                    view.onProfileLoaded(user);

                }
                userLoaded = true;
            }
        });
    }

    @Override
    public void onChanged() {
        if(userLoaded)
            this.isChanged = true;
    }

    @Override
    public void toggleNotifications() {
        if(model.isEnableNotifications()){
            service.enableNotifications();
            isChanged = true;
        }else {
            service.disableNotifications();
            isChanged = true;
        }
    }

    @Override
    public void uploadUserImage(Bitmap image) {
        this.isChanged = true;
        StorageService storageService = new StorageServiceImpl();
        storageService.uploadUserImage(image, new StorageService.Listener() {
            @Override
            public void onSuccess(String url) {
                Timber.d(url);
                User user = AuthServiceImpl.getCurrentUser();
                user.setAvatar(url);
                authService.updateProfile(user, new AuthService.Listener() {
                    @Override
                    public void onSuccess() {
                        model.setAvatar(url);
                    }
                });

            }

            @Override
            public void onError(String error) {
                Timber.e(error);
            }
        });
    }

}
