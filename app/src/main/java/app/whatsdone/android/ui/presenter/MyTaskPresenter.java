package app.whatsdone.android.ui.presenter;
import app.whatsdone.android.ui.view.MyTaskFragmentView;

public interface MyTaskPresenter {

    void init(MyTaskFragmentView view);
    void loadTasks();
}
