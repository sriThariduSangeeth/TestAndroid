package app.whatsdone.android.ui.presenter;


import java.util.ArrayList;
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
import app.whatsdone.android.ui.view.MyTaskFragmentView;
import app.whatsdone.android.utils.ObjectComparer;
import timber.log.Timber;

public class MyTaskPresenterImpl implements MyTaskPresenter {
    private MyTaskFragmentView view;
    TaskService service = new TaskServiceImpl();
    LogService logService = new LogServiceImpl();
    Task original = new Task();
    Group group;


    @Override
    public void init(MyTaskFragmentView view) {

        this.view = view;
    }

    @Override
    public void unSubscribe() {
        service.unSubscribe();
    }

    @Override
    public void loadTasks() {

       service.subscribeForUser(new ServiceListener() {
           @Override
           public void onDataReceived(List<BaseEntity> tasks) {
               view.updateTasks(tasks);
           }
       });
    }

    @Override
    public void delete(Task task) {
        service.delete(task.getDocumentID(), new ServiceListener() {
            @Override
            public void onSuccess() {
                Timber.d("Task deleted %s", task.getTitle());
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

        LogEvent event = ObjectComparer.isEqual(original, task, task.getDocumentID());

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
}
