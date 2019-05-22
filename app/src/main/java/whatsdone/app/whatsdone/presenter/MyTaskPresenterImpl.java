package whatsdone.app.whatsdone.presenter;


import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.model.MyTask;
import whatsdone.app.whatsdone.view.MyTaskFragmentView;

public class MyTaskPresenterImpl implements MyTaskPresenter {
    private MyTaskFragmentView view;

    @Override
    public void initi(MyTaskFragmentView view) {
        this.view = view;

        System.out.println("init task");

    }

    @Override
    public void loadTasks() {

        List<MyTask> tasks = new ArrayList<>();

        for (int i=0; i<20 ; i++)
        {
            MyTask task = new MyTask();
            task.setTaskName("MyTask " +i);
            tasks.add(task);

        }

        this.view.updateTasks(tasks);
        System.out.println("load tasks");
    }
}
