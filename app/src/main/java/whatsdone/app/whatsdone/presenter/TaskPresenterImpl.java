package whatsdone.app.whatsdone.presenter;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.model.Task;
import whatsdone.app.whatsdone.view.TaskFragmentView;

public class TaskPresenterImpl  implements TaskPresenter{
    private TaskFragmentView view;

    @Override
    public void initi(TaskFragmentView view) {
        this.view = view;

        System.out.println("init task");

    }

    @Override
    public void loadTasks() {

        List<Task> tasks = new ArrayList<>();

        for (int i=0; i<20 ; i++)
        {
            Task task = new Task();
            task.setTaskName("Task " +i);
            tasks.add(task);

        }

        this.view.updateTasks(tasks);
        System.out.println("load tasks");
    }
}
