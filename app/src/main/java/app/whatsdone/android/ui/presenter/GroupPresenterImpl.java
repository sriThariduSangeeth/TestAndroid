package app.whatsdone.android.ui.presenter;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.view.GroupFragmentView;

public class GroupPresenterImpl implements GroupPresenter{
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    private GroupFragmentView view;
    private Activity context;
    GroupService service = new GroupServiceImpl();


    @Override
    public void init(GroupFragmentView view, Activity context) {
        this.view = view;
        this.context = context;
        ((GroupServiceImpl) service).setContext(context);
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

