package app.whatsdone.android.ui.view;

import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;

public interface MyTaskFragmentView {

    void updateTasks(List<BaseEntity> tasks);
}
