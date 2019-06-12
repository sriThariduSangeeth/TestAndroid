package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.User;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import timber.log.Timber;

public class AddTaskFragment extends TaskFragmentBase {

    public static AddTaskFragment newInstance(Group group){

        AddTaskFragment instance = new AddTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if(arg != null) {
            this.group = arg.getParcelable("group");
            if(group != null) {
                task.setGroupId(group.getDocumentID());
                task.setGroupName(group.getGroupName());
            }
        }
    }

    public void save(){
        User current = AuthServiceImpl.getCurrentUser();
        String currentUserId = current.getDocumentID();
        task.setAssignedBy(currentUserId);
        task.setCreatedBy(currentUserId);
        if(task.getAssignedUser() == null || task.getAssignedUser().isEmpty()){
            task.setAssignedUser(currentUserId);
            task.setAssignedUserName(current.getDisplayName());
            task.setAssignedUserImage(current.getAvatar());
        }

        service.create(task, new ServiceListener() {
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
