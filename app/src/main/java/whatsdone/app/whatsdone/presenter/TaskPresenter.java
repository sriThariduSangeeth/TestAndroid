package whatsdone.app.whatsdone.presenter;
import whatsdone.app.whatsdone.view.TaskFragmentView ;

public interface TaskPresenter {

    void initi(TaskFragmentView view);
    void loadTasks();
}
