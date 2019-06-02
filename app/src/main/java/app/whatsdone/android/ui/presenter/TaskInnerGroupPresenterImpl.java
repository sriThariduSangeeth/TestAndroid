package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;

public class TaskInnerGroupPresenterImpl implements TaskInnerGroupPresenter{

    private TaskInnerGroupFragmentView view;

    @Override
    public void init(TaskInnerGroupFragmentView view) {
        this.view = view;
    }

    @Override
    public void loadTasksInner() {

    }
}
