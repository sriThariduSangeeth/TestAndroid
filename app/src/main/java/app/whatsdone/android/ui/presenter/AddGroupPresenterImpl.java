package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import javax.annotation.Nullable;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.fragments.AddGroupFragment;
import app.whatsdone.android.ui.view.AddGroupFragmentView;
import app.whatsdone.android.ui.view.GroupFragmentView;

public class AddGroupPresenterImpl implements AddGroupPresenter {
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    private AddGroupFragmentView view;
    private Activity context;
    GroupService service = new GroupServiceImpl();

    @Override
    public void create(Group group) {
        ((GroupServiceImpl) service).setContext(context);

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

    public void setContext(Activity context) {
        this.context = context;
    }
}
