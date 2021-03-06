package app.whatsdone.android.ui.presenter;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.view.GroupFragmentView;
import app.whatsdone.android.utils.Constants;

public class GroupPresenterImpl implements GroupPresenter{
    private GroupFragmentView view;
    private GroupService service = GroupServiceImpl.getInstance();


    @Override
    public void init(GroupFragmentView view) {
        this.view = view;
        System.out.println("init");
    }

    @Override
    public void subscribe() {


        service.subscribe(Constants.LISTENER_GROUP_ALL, new ServiceListener() {
            @Override
            public void onDataReceived(List<BaseEntity> entities) {
                view.updateGroups(entities);
            }
        });
    }

    @Override
    public void unSubscribe() {
        service.unSubscribe(Constants.LISTENER_GROUP_ALL);
    }

    @Override
    public void deleteTeam(String documentId) {
        try {
            service.delete(documentId, new ServiceListener() {
                @Override
                public void onSuccess() {
                    view.onGroupDeleted();

                }

                @Override
                public void onError(@Nullable String error) {

                    view.onDeleteError();
                }
            });

        }catch (Exception ex){
            view.onDeleteError();
        }
    }

    @Override
    public void leaveTeam(String groupId) {
        service.leave(groupId, new ServiceListener() {
            @Override
            public void onSuccess() {
                view.onGroupLeave();
            }

            @Override
            public void onError(@Nullable String error) {

            }
        });
    }
}




