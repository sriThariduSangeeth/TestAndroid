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
            this.group = arg.getParcelable("group");
            if(group != null) {
                task.setGroupId(group.getDocumentID());
                task.setGroupName(group.getGroupName());
                task.setAssignedBy(currentUserId);

                if(group.getDocumentID().equals(currentUserId)){
                    this.isPersonalTask = true;
                    this.isFromMyTasks = true;
                    if(!group.getCreatedBy().equals(currentUserId)){
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

    public void save(){
        User current = AuthServiceImpl.getCurrentUser();
        String currentUserId = current.getDocumentID();
        String id = service.add();
        task.setDocumentID(id);
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

                Timber.d("task created");
                List<String> members = group.getMembers();
                List<ExistUser> users = new ArrayList<>();


                if(!members.contains(task.getAssignedUser())){
                    members.add(task.getAssignedUser());

                    DocumentSnapshot doc= null;
                    if (doc.get(Constants.FIELD_GROUP_MEMBERS_DETAILS) != null) {
                        List<HashMap> details = (List<HashMap>) doc.get(Constants.FIELD_GROUP_MEMBERS_DETAILS);

                        for (HashMap map :
                                details) {
                            String phone = (String)map.get(Constants.FIELD_GROUP_MEMBERS_DETAILS_PHONE);
                            Object isInvitedObject = map.get(Constants.FIELD_GROUP_MEMBERS_DETAILS_INVITED);
                            boolean isInvited = false;
                            if(isInvitedObject instanceof Boolean){
                                isInvited = (boolean) isInvitedObject;
                            }else if(isInvitedObject instanceof String) {
                                isInvited = ((String)isInvitedObject).equals("true");
                            }
                            String displayName = (String)map.get(Constants.FIELD_GROUP_MEMBERS_DETAILS_NAME);

                            ExistUser user = new ExistUser();
                            user.setDisplayName(displayName);
                            user.setIsInvited(isInvited);
                            user.setPhoneNumber(phone);
                            users.add(user);
                        }}

                    group.setMembers(members);
                    group.setMemberDetails(users);
                    GroupService groupService = new GroupServiceImpl();
                    groupService.update(group, new ServiceListener() {
                        @Override
                        public void onCompleted(boolean isSuccessful) {
                            Timber.d("group update completed: %s", isSuccessful);
                        }
                    });
                }


                inviteAssignee();

            }

            @Override
            public void onError(@Nullable String error) {
                Timber.e(error);
            }
        });
    }


}
