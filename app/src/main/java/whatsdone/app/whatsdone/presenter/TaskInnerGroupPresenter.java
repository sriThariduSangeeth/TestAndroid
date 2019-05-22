package whatsdone.app.whatsdone.presenter;

import whatsdone.app.whatsdone.view.TaskInnerGroupFragmentView;

public interface TaskInnerGroupPresenter {
    void init(TaskInnerGroupFragmentView view);
    void loadTasksInner();
}
