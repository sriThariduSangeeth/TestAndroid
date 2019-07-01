package app.whatsdone.android.ui.presenter;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.LogService;
import app.whatsdone.android.services.LogServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;
import app.whatsdone.android.utils.ObjectComparer;
import timber.log.Timber;

public class TaskInnerGroupPresenterImpl implements TaskInnerGroupPresenter {
    private static final String TAG = TaskInnerGroupPresenterImpl.class.getSimpleName();
    private TaskInnerGroupFragmentView view;
    TaskService service = new TaskServiceImpl();
    LogService logService = new LogServiceImpl();
    Task original = new Task();

    Group group;

    @Override
    public void init(TaskInnerGroupFragmentView view, Group group) {

        this.group = group;
        this.view = view;

    }

    @Override
    public void loadTasksInner(String groupId) {
        service.subscribeForGroup(groupId, new ServiceListener() {
            @Override
            public void onDataReceived(List<BaseEntity> tasks) {
                view.updateTaskInner(tasks);
            }

            @Override
            public void onError(@Nullable String error) {
                Log.d(TAG, error);
            }
        });
    }

    @Override
    public void deleteTaskInner(String id) {
        service.delete(id, new ServiceListener() {
            @Override
            public void onSuccess() {
                Timber.d("Task deleted %s", id);
            }

            @Override
            public void onError(@Nullable String error) {
                Timber.e(error);
            }
        });
    }

    @Override
    public void setStatus(Task task, Task.TaskStatus status) {

        this.original = task.getClone();
        task.setStatus(status);

        LogEvent event = ObjectComparer.isEqual(original, task, group.getDocumentID());

        if (!event.getLogs().isEmpty())
        service.update(task, new ServiceListener() {
            @Override
            public void onSuccess() {
                addLogs(event);
                Timber.d("Task updated %s", task.getTitle());
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
    @Override
    public void unSubscribe() {
        service.unSubscribe();
    }


}
