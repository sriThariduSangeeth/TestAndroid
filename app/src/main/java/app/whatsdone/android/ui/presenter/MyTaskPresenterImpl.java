package app.whatsdone.android.ui.presenter;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.view.MyTaskFragmentView;

public class MyTaskPresenterImpl implements MyTaskPresenter {
    private MyTaskFragmentView view;
    private TaskService service = new TaskServiceImpl();

    @Override
    public void init(MyTaskFragmentView view) {
        this.view = view;



    }

    @Override
    public void loadTasks() {

       service.subscribeForUser(new ServiceListener() {
           @Override
           public void onDataReceived(List<BaseEntity> tasks) {

               view.updateTasks(tasks);
           }
       });


    }
}
