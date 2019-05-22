package whatsdone.app.whatsdone.presenter;

import whatsdone.app.whatsdone.view.TaskInnerGroupFragmentView;

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
