package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.model.User;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
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
        String currentUserId = AuthServiceImpl.getCurrentUser().getDocumentID();
        if(arg != null) {
            if(arg.getParcelable("group") != null) {
                this.group = arg.getParcelable("group");
                if (group != null) {
                    task.setGroupId(group.getDocumentID());
                    task.setGroupName(group.getGroupName());
                    task.setAssignedBy(currentUserId);

                    if (group.getDocumentID().equals(currentUserId)) {
                        this.isPersonalTask = true;
                        this.isFromMyTasks = true;
                        if (!group.getCreatedBy().equals(currentUserId)) {
                            group.setUpdatedDate(new Date());
                            group.getMembers().add(currentUserId);
                            group.getAdmins().add(currentUserId);
                            group.setCreatedBy(currentUserId);
                            group.setAvatar(AuthServiceImpl.getCurrentUser().getAvatar());
                            groupService.create(group, new ServiceListener() {
                                @Override
                                public void onSuccess() {
                                    Timber.d("%s created.", group.getGroupName());
                                    GroupServiceImpl.personalGroup = group;
                                }

                                @Override
                                public void onError(@javax.annotation.Nullable String error) {
                                    Timber.e(error);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    public void save(){
        User current = AuthServiceImpl.getCurrentUser();
        String currentUserId = current.getDocumentID();
        String id = service.add();
        task.setDocumentID(id);
        task.setCreatedBy(currentUserId);

        if(task.getAssignedUser() == null || task.getAssignedUser().isEmpty()){
            task.setAssignedUser(currentUserId);
            task.setAssignedUserName(current.getDisplayName());
            task.setAssignedUserImage(current.getAvatar());
        }

        service.create(task, new ServiceListener() {
            @Override
            public void onSuccess() {
                LogEvent event = new LogEvent();
                event.setDocumentID(task.getDocumentID());
                event.setGroupId(group.getDocumentID());
                event.getLogs().add(
                        new Change(
                                current.getDocumentID(),
                                current.getDisplayName(),
                                Change.ChangeType.CREATED, new Date(),
                                "", "" ));
                logService.create(event, new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        Timber.d("log added");
                    }
                });

                inviteAssignee();

            }

            @Override
            public void onError(@Nullable String error) {
                Timber.e(error);
            }
        });
    }


}
