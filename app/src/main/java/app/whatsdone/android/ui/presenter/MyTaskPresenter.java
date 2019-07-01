package app.whatsdone.android.ui.presenter;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.view.MyTaskFragmentView;

public interface MyTaskPresenter {

    void init(MyTaskFragmentView view);
    void unSubscribe();
    void loadTasks();
    void setStatus(Task task, Task.TaskStatus status);
    void delete(Task task);
}
