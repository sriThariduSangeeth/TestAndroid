package whatsdone.app.whatsdone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.TasksRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.Task;
import whatsdone.app.whatsdone.presenter.TaskPresenter;
import whatsdone.app.whatsdone.presenter.TaskPresenterImpl;
import whatsdone.app.whatsdone.view.TaskFragmentView;


public class TaskFragment extends Fragment implements TaskFragmentView{


    private List<Task> tasks = new ArrayList<>();
    private TasksRecyclerViewAdapter tasksAdapter;
    private TaskPresenter taskPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_tasks_, container, false);

        List<Task> tasks = new ArrayList<>();

        for (int i=0; i<20 ; i++)
        {
            Task task = new Task();
            task.setTaskName("Task " +i);
            tasks.add(task);

        }
        this.tasks.addAll(tasks);

        RecyclerView recycler = view.findViewById(R.id.task_recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new TasksRecyclerViewAdapter(tasks);
        recycler.setAdapter(tasksAdapter);
        this.taskPresenter = new TaskPresenterImpl();
        this.taskPresenter.initi(this);
        this.taskPresenter.loadTasks();




      return view;
    }

    @Override
    public void updateTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
        tasksAdapter.notifyDataSetChanged();
        System.out.println("update tasks");
    }
}
