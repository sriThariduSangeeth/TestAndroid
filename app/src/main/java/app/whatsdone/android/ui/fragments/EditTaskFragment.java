package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.utils.Constants;
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

        if (!event.getLogs().isEmpty())
            service.update(task, new ServiceListener() {
                @Override
                public void onSuccess() {
                    Timber.d("task updated");
                }

                @Override
                public void onError(@Nullable String error) {
                    Timber.e(error);
                }
            });
    }

}
