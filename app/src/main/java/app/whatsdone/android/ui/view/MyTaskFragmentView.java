package app.whatsdone.android.ui.view;

import java.util.List;

import app.whatsdone.android.model.MyTask;

public interface MyTaskFragmentView {

    void updateTasks(List<MyTask> tasks);
}
