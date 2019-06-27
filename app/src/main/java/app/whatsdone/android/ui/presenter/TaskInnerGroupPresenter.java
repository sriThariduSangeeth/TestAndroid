package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;

public interface TaskInnerGroupPresenter {
    void init(TaskInnerGroupFragmentView view, Group group);
    void loadTasksInner(String groupId);
    void deleteTaskInner(String id);
    void setStatus(Task task, Task.TaskStatus status);

    void unSubscribe();
}
