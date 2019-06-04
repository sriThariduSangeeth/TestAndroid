package app.whatsdone.android.ui.presenter;


import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.MyTask;
import app.whatsdone.android.ui.view.MyTaskFragmentView;

public class MyTaskPresenterImpl implements MyTaskPresenter {
    private MyTaskFragmentView view;

    @Override
    public void initi(MyTaskFragmentView view) {
        this.view = view;



    }

    @Override
    public void loadTasks() {

        List<MyTask> tasks = new ArrayList<>();

        for (int i=0; i<20 ; i++)
        {
            MyTask task = new MyTask("");
            task.setTaskName("MyTask " +i);
            tasks.add(task);

        }

        this.view.updateTasks(tasks);

    }
}
