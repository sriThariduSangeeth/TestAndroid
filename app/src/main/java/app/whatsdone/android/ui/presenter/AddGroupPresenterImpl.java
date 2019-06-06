package app.whatsdone.android.ui.presenter;

import android.app.Activity;
import android.util.Log;

import javax.annotation.Nullable;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.StorageService;
import app.whatsdone.android.services.StorageServiceImpl;
import app.whatsdone.android.ui.fragments.AddGroupFragment;
import app.whatsdone.android.ui.view.AddGroupFragmentView;
import app.whatsdone.android.ui.view.GroupFragmentView;

public class AddGroupPresenterImpl implements AddGroupPresenter {
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    private AddGroupFragmentView view;
    private Activity context;
    private GroupService service = new GroupServiceImpl();
    private StorageService storageService = new StorageServiceImpl();



    @Override
    public void init(AddGroupFragmentView view, Activity context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void create(Group group) {
        String documentId = service.add();
        group.setDocumentID(documentId);

        if(group.getTeamImage() != null) {
            storageService.uploadGroupImage(group.getTeamImage(), documentId, new StorageService.Listener() {
                @Override
                public void onSuccess(String url) {
                    Log.d(TAG, "Image upload success " + documentId);
                }
            });
        }

        service.create(group, new ServiceListener() {
            @Override
            public void onSuccess() {
                view.onGroupSaved();

            }

            @Override
            public void onError(@Nullable String error) {

                view.onGroupError(error);
            }
        });




    }

    @Override
    public void updateTeam(Group group) {

    }



    public void setContext(Activity context) {
        this.context = context;
    }
}
