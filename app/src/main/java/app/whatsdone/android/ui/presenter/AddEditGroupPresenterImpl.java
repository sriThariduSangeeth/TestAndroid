package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.ContactServiceImpl;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.StorageService;
import app.whatsdone.android.services.StorageServiceImpl;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;
import timber.log.Timber;

public class AddEditGroupPresenterImpl implements AddEditGroupPresenter {
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    private BaseGroupFragmentView view;
    private GroupService service = GroupServiceImpl.getInstance();
    private StorageService storageService = new StorageServiceImpl();
    private ContactService contactService = new ContactServiceImpl();

    @Override
    public void init(BaseGroupFragmentView view, Activity context) {
        this.view = view;
    }

    @Override
    public void create(Group group) {
        view.onGroupSaved();
        String documentId = service.add();
        group.setDocumentID(documentId);

        if (group.getTeamImage() != null) {
            storageService.uploadGroupImage(group.getTeamImage(), documentId, new StorageService.Listener() {
                @Override
                public void onSuccess(String url) {
                    Timber.d("Image upload success %s", documentId);
                }
            });
        }

        try {
            service.create(group, new ServiceListener() {
                @Override
                public void onSuccess() {
                    checkExistInPlatform(group);
                }

                @Override
                public void onError(@Nullable String error) {
                    view.onGroupError(error);
                }
            });

        } catch (Exception exe) {
            view.onGroupError(exe.getMessage());
            Timber.d(exe);
        }
    }

    @Override
    public void update(Group group) {
        view.onGroupSaved();
        if (group.isImageChanged()) {
            storageService.uploadGroupImage(group.getTeamImage(), group.getDocumentID(), new StorageService.Listener() {
                @Override
                public void onSuccess(String url) {
                    Timber.d("Image upload success %s", group.getDocumentID());
                }
            });
        }

        service.update(group, new ServiceListener() {
            @Override
            public void onSuccess() {
                checkExistInPlatform(group);
            }

            @Override
            public void onError(@Nullable String error) {

                view.onGroupError(error);
            }
        });
    }

    public void checkExistInPlatform(Group group) {
        contactService.existsInPlatform(group.getMembers(), new ContactService.Listener() {
            @Override
            public void onCompleteSearch(List<ExistUser> users, List<String> isExisting) {
                List<String> newUsers = new ArrayList<>();
                for (String user :
                        group.getMembers()) {
                    if (!group.getOriginalMembers().contains(user)){
                        newUsers.add(user);
                    }
                }
                if(newUsers.size() > 0)
                    sendInviteToMembers(newUsers, group);
                service.update(group, users, new ServiceListener() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }
        });

    }

    public void sendInviteToMembers(List<String> newMembers, Group group) {
        contactService.notifyMembers(newMembers, group, new ContactService.Listener() {
            @Override
            public void onInvited() {

            }
        });

    }


    public void setContext(Activity context) {
    }


}
