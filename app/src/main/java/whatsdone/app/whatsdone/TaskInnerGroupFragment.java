package whatsdone.app.whatsdone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.TaskInnerGroupRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.TaskInnerGroup;
import whatsdone.app.whatsdone.presenter.TaskInnerGroupPresenter;
import whatsdone.app.whatsdone.presenter.TaskInnerGroupPresenterImpl;
import whatsdone.app.whatsdone.view.TaskInnerGroupFragmentView;

public class TaskInnerGroupFragment extends Fragment implements TaskInnerGroupFragmentView {

    private List<TaskInnerGroup> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter taskInnerGroupRecyclerViewAdapter;
    private TaskInnerGroupPresenter taskInnerGroupPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task, container, false);

        RecyclerView taskInnerGroupRecyclerView = view.findViewById(R.id.task_inner_group_recycler_view);
        taskInnerGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<TaskInnerGroup> tasks = new ArrayList<>();
        for(int i=0 ; i<5; i++)
        {
            TaskInnerGroup task = new TaskInnerGroup();
            task.setTaskName("Task " +i);
            tasks.add(task);
        }

        taskInnerGroupRecyclerViewAdapter = new TaskInnerGroupRecyclerViewAdapter(tasks, getContext());
        taskInnerGroupRecyclerView.setAdapter(taskInnerGroupRecyclerViewAdapter);

        this.taskInnerGroupPresenter = new TaskInnerGroupPresenterImpl();
        this.taskInnerGroupPresenter.init(this);
        this.taskInnerGroupPresenter.loadTasksInner();



        return view;
    }


    @Override
    public void updateTaskInner(List<TaskInnerGroup> tasks) {
        this.taskInnerGroups = tasks;
        taskInnerGroupRecyclerViewAdapter.notifyDataSetChanged();
    }


}
