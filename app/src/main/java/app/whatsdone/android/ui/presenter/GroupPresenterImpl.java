package app.whatsdone.android.ui.presenter;

import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.view.GroupFragmentView;

public class GroupPresenterImpl implements GroupPresenter{
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    private GroupFragmentView view;
    GroupService service = new GroupServiceImpl();


    @Override
    public void init(GroupFragmentView view) {
        this.view = view;
        System.out.println("init");
    }

    @Override
    public void subscribe() {


        service.subscribe(new ServiceListener() {
            @Override
            public void onDataReceived(List<BaseEntity> entities) {
                view.updateGroups(entities);
            }
        });
    }

    @Override
    public void unSubscribe() {
        service.unSubscribe();
    }

}

