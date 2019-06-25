package app.whatsdone.android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.model.User;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceBase;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.activity.InnerGroupDiscussionActivity;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.DateUtil;
import app.whatsdone.android.utils.LocalState;
import app.whatsdone.android.utils.ObjectComparer;
import timber.log.Timber;

public class EditTaskFragment extends TaskFragmentBase {

    public static EditTaskFragment newInstance(Group group, Task task, boolean isMyTask) {

        EditTaskFragment instance = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_GROUP, group);
        args.putParcelable(Constants.ARG_TASK, task);
        args.putBoolean(Constants.ACTION_VIEW_TASK, true);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.single_task_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = ActivityLogFragment.newInstance(task.getDocumentID());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.task_container, fragment).commit();

        return true;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            this.group = arg.getParcelable(Constants.ARG_GROUP);
            this.task = arg.getParcelable(Constants.ARG_TASK);
            this.original = this.task.getClone();

            LocalState.getInstance().setTaskRead(this.task);
            this.isFromMyTasks = arg.getBoolean(Constants.ACTION_VIEW_TASK, false);
            if (task.getGroupId().equals(AuthServiceImpl.getCurrentUser().getDocumentID())) {
                isPersonalTask = true;
            }

            if (group == null && isFromMyTasks) {
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

    public void save() {
        LogEvent event = ObjectComparer.isEqual(original, task, group.getDocumentID());

        if (event.getLogs().size() > 0)
            service.update(task, new ServiceListener() {
                @Override
                public void onSuccess() {

                    addLogs(event);

                    Timber.d("task created");
                    List<String> members = group.getMembers();
                    if (!members.contains(task.getAssignedUser())) {
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

    private void addLogs(LogEvent event) {

        logService.update(event, new ServiceListener() {
            @Override
            public void onSuccess() {
                Timber.d("log added");
            }
        });
    }
}
