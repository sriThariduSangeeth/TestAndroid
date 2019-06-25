package app.whatsdone.android.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import timber.log.Timber;

public class InviteAssigneeUtil {


    private Task task;
    private ContactService contactService;
    private TaskService taskService;
    private Group group;
    private GroupService groupService;

    public InviteAssigneeUtil(Task task, ContactService contactService, TaskService taskService, Group group, GroupService groupService) {

        this.task = task;
        this.contactService = contactService;
        this.taskService = taskService;
        this.group = group;
        this.groupService = groupService;
    }

    public void invite() {
        List<String> members = new ArrayList<>();
        members.add(task.getAssignedUser());
        contactService.existsInPlatform(members, new ContactService.Listener() {
            @Override
            public void onCompleteSearch(List<ExistUser> users, List<String> isExisting) {
                if (users.size() == 1) {
                    ExistUser user = users.get(0);
                    task.setAssignedUserName(user.getDisplayName());
                    taskService.update(task, new ServiceListener() {
                        @Override
                        public void onSuccess() {
                            Timber.d("user updated");
                        }
                    });

                    addUserToGroup(user);
                } else {
                    contactService.inviteAssignee(task.getAssignedUser(), group, task, new ContactService.Listener() {
                        @Override
                        public void onInvited() {
                            Timber.d("user invited");
                            ExistUser user = new ExistUser();
                            user.setIsInvited(true);
                            user.setDisplayName(String.format("%s(%s)", task.getAssignedUser(), Constants.INVITED));
                            addUserToGroup(user);
                        }
                    });

                }


            }
        });
    }

    private void addUserToGroup(ExistUser user) {
        if(!group.getMembers().contains(task.getAssignedUser())){
            group.getMembers().add(user.getPhoneNumber());
            group.getMemberDetails().add(user);
            groupService.update(group, user, new ServiceListener() {
                @Override
                public void onSuccess() {
                    Timber.d("New member %s added", user);
                }

                @Override
                public void onError(@Nullable String error) {
                    Timber.e(error);
                }
            });
        }
    }
}
