package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceBase;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.LocalState;
import timber.log.Timber;

public class EditTaskFragment extends TaskFragmentBase {

    public static EditTaskFragment newInstance(Group group, Task task, boolean isMyTask){

        EditTaskFragment instance = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_GROUP, group);
        args.putParcelable(Constants.ARG_TASK, task);
        args.putBoolean(Constants.ACTION_VIEW_TASK, true);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if(arg != null) {
            this.group = arg.getParcelable(Constants.ARG_GROUP);
            this.task = arg.getParcelable(Constants.ARG_TASK);
            LocalState.getInstance().setTaskRead(this.task);
            this.isFromMyTasks = arg.getBoolean(Constants.ACTION_VIEW_TASK, false);
            if(task.getGroupId().equals(AuthServiceImpl.getCurrentUser().getDocumentID())){
                isPersonalTask = true;
            }

            if(group == null && isFromMyTasks){
                groupService.getGroupById(task.getGroupId(), new ServiceListener() {
                    @Override
                    public void onDataReceived(BaseEntity entity) {
                        group = (Group) entity;
                    }
                });
            }
        }
        this.title = "Edit Task";



    }

    public void save(){
        service.update(task, new ServiceListener() {
            @Override
            public void onSuccess() {
                Timber.d("task created");
                List<String> members = group.getMembers();
                if(!members.contains(task.getAssignedUser())){
                    members.add(task.getAssignedUser());

                    GroupService groupService = new GroupServiceImpl();
                    groupService.update(group, new ServiceListener() {
                        @Override
                        public void onCompleted(boolean isSuccessful) {
                            Timber.d("group update completed: %s", isSuccessful);
                        }
                    });
                }

            }

            @Override
            public void onError(@Nullable String error) {
                Timber.e(error);
            }
        });
    }
}
