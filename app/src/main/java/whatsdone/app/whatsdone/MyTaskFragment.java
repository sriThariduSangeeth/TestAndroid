package whatsdone.app.whatsdone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.MyTasksRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.MyTask;
import whatsdone.app.whatsdone.presenter.MyTaskPresenter;
import whatsdone.app.whatsdone.presenter.MyTaskPresenterImpl;
import whatsdone.app.whatsdone.view.MyTaskFragmentView;


public class MyTaskFragment extends Fragment implements MyTaskFragmentView {


    private List<MyTask> tasks = new ArrayList<>();
    private MyTasksRecyclerViewAdapter tasksAdapter;
    private MyTaskPresenter taskPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_task_my_, container, false);

        List<MyTask> tasks = new ArrayList<>();

        for (int i=0; i<20 ; i++)
        {
            MyTask task = new MyTask();
            task.setTaskName("My task " +i);
            tasks.add(task);

        }
        this.tasks.addAll(tasks);

        RecyclerView recycler = view.findViewById(R.id.task_recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new MyTasksRecyclerViewAdapter(tasks);
        recycler.setAdapter(tasksAdapter);
        this.taskPresenter = new MyTaskPresenterImpl();
        this.taskPresenter.initi(this);
        this.taskPresenter.loadTasks();



        setHasOptionsMenu(true);
      return view;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mytask_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void updateTasks(List<MyTask> tasks) {
        this.tasks.addAll(tasks);
        tasksAdapter.notifyDataSetChanged();
        System.out.println("update tasks");
    }
}
