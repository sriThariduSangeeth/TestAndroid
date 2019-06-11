package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import timber.log.Timber;

public class EditTaskFragment extends TaskFragmentBase {

    public static EditTaskFragment newInstance(Group group, Task task){

        EditTaskFragment instance = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        args.putParcelable("task", task);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if(arg != null) {
            this.group = arg.getParcelable("group");
            this.task = arg.getParcelable("task");
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
