package app.whatsdone.android.ui.presenter;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.TaskInnerGroup;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;

public class TaskInnerGroupPresenterImpl implements TaskInnerGroupPresenter{
    private static final String TAG = TaskInnerGroupPresenterImpl.class.getSimpleName();
    private TaskInnerGroupFragmentView view;
    TaskService service = new TaskServiceImpl();

    @Override
    public void init(TaskInnerGroupFragmentView view) {
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
    public void unSubscribe() {
        service.unSubscribe();
    }
}
