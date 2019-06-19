package app.whatsdone.android.ui.adapters;

import app.whatsdone.android.model.Task;

public interface SwipeListener {
    void onDelete(Task task);

    void onChangeStatus(Task task, Task.TaskStatus status);

    void onTaskClicked(Task task);
}