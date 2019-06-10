package app.whatsdone.android.ui.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.adapters.MyTasksRecyclerViewAdapter;
import app.whatsdone.android.ui.adapters.TaskSwipeController;
import app.whatsdone.android.ui.adapters.TaskSwipeControllerAction;
import app.whatsdone.android.ui.presenter.MyTaskPresenter;
import app.whatsdone.android.ui.presenter.MyTaskPresenterImpl;
import app.whatsdone.android.ui.view.MyTaskFragmentView;


public class MyTaskFragment extends Fragment implements MyTaskFragmentView, MyTasksRecyclerViewAdapter.OnMyTaskFragmentInteractionListener {


    private List<BaseEntity> tasks = new ArrayList<>();
    private MyTasksRecyclerViewAdapter tasksAdapter;
    private MyTaskPresenter taskPresenter;
    private FloatingActionButton addFab;
    private TaskSwipeController taskSwipeController;
    private OnMyTaskFragmentInteractionListener listener;
    private RecyclerView recycler;
    private TaskService service = new TaskServiceImpl();

    public void setListener(OnMyTaskFragmentInteractionListener listener) {
        System.out.println("setting listener + " + listener.getClass().getCanonicalName());
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_task_my, container, false);

        recycler = view.findViewById(R.id.task_recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new MyTasksRecyclerViewAdapter(tasks , getContext());
        tasksAdapter.setListener(this);
        recycler.setAdapter(tasksAdapter);
        this.taskPresenter = new MyTaskPresenterImpl();
        this.taskPresenter.init(this);
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

        setHasOptionsMenu(false);
        setupRecyclerView();
      return view;
    }


    @Override
    public void updateTasks(List<BaseEntity> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        tasksAdapter.notifyDataSetChanged();

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


    private void setupRecyclerView()
    {
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new MyTasksRecyclerViewAdapter(tasks, getContext());
        recycler.setAdapter(tasksAdapter);

        taskSwipeController = new TaskSwipeController(new TaskSwipeControllerAction() {

            @Override
            public void onTaskDeleteClicked(int position) {
                //task delete

                service.delete(tasks.get(position).getDocumentID(), new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        tasksAdapter.tasks.remove(position);
                        tasksAdapter.notifyItemRemoved(position);
                        tasksAdapter.notifyItemRangeChanged(position, tasksAdapter.getItemCount());
                    }
                });

            }

            @Override
            public void onTaskOnHoldClicked(int position) {
                //task change status to On Hold

            }

            @Override
            public void onTaskInProgressClicked(int position) {
                //Task change status to In Progress
            }

            @Override
            public void onTaskDoneClicked(int position) {
                //Task change status to Done
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(taskSwipeController);
        itemTouchhelper.attachToRecyclerView(recycler);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                taskSwipeController.onDraw(c);
                taskSwipeController.setContext(getContext());
            }
        });

    }


}

