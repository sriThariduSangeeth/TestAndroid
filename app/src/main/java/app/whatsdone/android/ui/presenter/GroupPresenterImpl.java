package app.whatsdone.android.ui.presenter;

import java.util.List;

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

    @Override
    public void deleteTeam(String documentId) {
        // ((GroupServiceImpl) service).setContext(context);

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

    @Override
    public void editTeam(Group group) {
        service.update(group, new ServiceListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(@Nullable String error) {

            }
        });
    }
}




