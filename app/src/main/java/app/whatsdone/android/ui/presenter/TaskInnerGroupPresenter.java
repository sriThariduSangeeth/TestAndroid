package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;

public interface TaskInnerGroupPresenter {
    void init(TaskInnerGroupFragmentView view);
    void loadTasksInner(String groupId);
    void deleteTaskInner();

    void unSubscribe();
}
