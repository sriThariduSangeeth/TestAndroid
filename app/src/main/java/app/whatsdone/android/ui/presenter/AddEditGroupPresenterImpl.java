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
import app.whatsdone.android.ui.view.BaseGroupFragmentView;

public class AddEditGroupPresenterImpl implements AddEditGroupPresenter {
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    private BaseGroupFragmentView view;
    private Activity context;
    private GroupService service = new GroupServiceImpl();
    private StorageService storageService = new StorageServiceImpl();



    @Override
    public void init(BaseGroupFragmentView view, Activity context) {
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


        try {


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

        } catch (Exception exe){ view.onGroupError(exe.getMessage());
            Log.d("My ", exe.getMessage());}



    }

    @Override
    public void update(Group group) {

        service.update(group, new ServiceListener() {
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


    public void setContext(Activity context) {
        this.context = context;
    }
}
