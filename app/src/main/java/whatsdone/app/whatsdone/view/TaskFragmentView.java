package whatsdone.app.whatsdone.view;

import java.util.List;

import whatsdone.app.whatsdone.model.Task;

public interface TaskFragmentView {

    void updateTasks(List<Task> tasks);
}
