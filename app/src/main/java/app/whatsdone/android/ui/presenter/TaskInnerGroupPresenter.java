package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;

public interface TaskInnerGroupPresenter {
    void init(TaskInnerGroupFragmentView view);
    void loadTasksInner();
}
