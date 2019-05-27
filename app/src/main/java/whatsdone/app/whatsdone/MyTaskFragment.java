package whatsdone.app.whatsdone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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


public class MyTaskFragment extends Fragment implements MyTaskFragmentView, MyTasksRecyclerViewAdapter.OnMyTaskFragmentInteractionListener {


    private List<MyTask> tasks = new ArrayList<>();
    private MyTasksRecyclerViewAdapter tasksAdapter;
    private MyTaskPresenter taskPresenter;
    private FloatingActionButton addFab;
    private OnMyTaskFragmentInteractionListener listener;

    public void setListener(OnMyTaskFragmentInteractionListener listener) {
        System.out.println("setting listener + " + listener.getClass().getCanonicalName());
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_task_my_, container, false);

        List<MyTask> tasks = new ArrayList<>();
        System.out.println("my task on create view");
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
        tasksAdapter.setListener(this);
        recycler.setAdapter(tasksAdapter);
        this.taskPresenter = new MyTaskPresenterImpl();
        this.taskPresenter.initi(this);
        this.taskPresenter.loadTasks();


//add new my task frag

        view.findViewById(R.id.add_new_my_task_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new CreateNewTaskFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.my_task_container, myFragment).addToBackStack(null).commit();

            }
        });

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

    @Override
    public void onTaskClicked() {
        System.out.println("MyTaskFrament onTaskClicked");
        if( this.listener != null)
            this.listener.onTaskClicked();
    }




    public interface OnMyTaskFragmentInteractionListener {

        void onTaskClicked();

    }




}

