package app.whatsdone.android.ui.presenter;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.TaskInnerGroup;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;

public class TaskInnerGroupPresenterImpl implements TaskInnerGroupPresenter{

    private TaskInnerGroupFragmentView view;

    @Override
    public void init(TaskInnerGroupFragmentView view) {
        this.view = view;
    }

    @Override
    public void loadTasksInner() {

        List<TaskInnerGroup> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TaskInnerGroup task = new TaskInnerGroup();
            task.setTaskName("Task " + i);
            tasks.add(task);
        }
        this.view.updateTaskInner(tasks);

    }
}
