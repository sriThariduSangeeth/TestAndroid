package app.whatsdone.android.ui.adapters;

public abstract class TaskSwipeControllerAction {
    public void onTaskDeleteClicked(int position)
    {}

    public void onTaskInProgressClicked(int position)
    {}

    public void onTaskDoneClicked(int position)
    {}

    public void onTaskOnHoldClicked(int position)
    {}
}
