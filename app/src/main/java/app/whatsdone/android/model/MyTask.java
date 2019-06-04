package app.whatsdone.android.model;

public class MyTask {

  //  public static final int text_type = 1;
    private String taskName;

    public MyTask(String name)
    {
        this.taskName = name;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
