package whatsdone.app.whatsdone.presenter;
import whatsdone.app.whatsdone.view.MyTaskFragmentView;

public interface MyTaskPresenter {

    void initi(MyTaskFragmentView view);
    void loadTasks();
}
