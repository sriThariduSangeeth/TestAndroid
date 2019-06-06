package app.whatsdone.android.ui.presenter;


import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.view.MyTaskFragmentView;

public class MyTaskPresenterImpl implements MyTaskPresenter {
    private MyTaskFragmentView view;

    @Override
    public void init(MyTaskFragmentView view) {
        this.view = view;



    }

    @Override
    public void loadTasks() {

        List<Task> tasks = new ArrayList<>();

        for (int i=0; i<20 ; i++)
        {
            Task task = new Task();
            task.setTitle("Task " +i);
            tasks.add(task);

        }

        this.view.updateTasks(tasks);

    }
}
