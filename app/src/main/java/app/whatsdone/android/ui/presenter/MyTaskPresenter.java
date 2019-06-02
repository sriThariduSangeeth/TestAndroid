package app.whatsdone.android.ui.presenter;
import app.whatsdone.android.ui.view.MyTaskFragmentView;

public interface MyTaskPresenter {

    void initi(MyTaskFragmentView view);
    void loadTasks();
}
