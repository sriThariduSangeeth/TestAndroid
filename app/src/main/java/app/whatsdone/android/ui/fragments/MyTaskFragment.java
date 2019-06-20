package app.whatsdone.android.ui.fragments;

import android.content.Intent;
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
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.ui.adapters.MyTasksRecyclerViewAdapter;
import app.whatsdone.android.ui.adapters.SwipeListener;
import app.whatsdone.android.ui.adapters.TaskSwipeController;
import app.whatsdone.android.ui.adapters.TaskSwipeControllerAction;
import app.whatsdone.android.ui.presenter.MyTaskPresenter;
import app.whatsdone.android.ui.presenter.MyTaskPresenterImpl;
import app.whatsdone.android.ui.view.MyTaskFragmentView;
import app.whatsdone.android.utils.Constants;
import timber.log.Timber;

import static app.whatsdone.android.utils.SortUtil.sort;


public class MyTaskFragment extends Fragment implements MyTaskFragmentView, SwipeListener {


    private List<BaseEntity> tasks = new ArrayList<>();
    private MyTasksRecyclerViewAdapter tasksAdapter;
    private MyTaskPresenter taskPresenter;
    private OnMyTaskFragmentInteractionListener listener;
    private RecyclerView recycler;

    public MyTaskFragment() {
    }

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

        this.taskPresenter = new MyTaskPresenterImpl();
        this.taskPresenter.init(this);
        this.taskPresenter.loadTasks();

        view.findViewById(R.id.add_new_my_task_fab).setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Intent intent = new Intent(getActivity(), InnerGroupTaskActivity.class);
            intent.putExtra(Constants.ARG_ACTION, Constants.ACTION_ADD_TASK);
            intent.putExtra(Constants.ARG_GROUP, GroupServiceImpl.getPersonalGroup());
            getActivity().startActivity(intent);

        });

        setHasOptionsMenu(false);
        setupRecyclerView();
      return view;
    }


    @Override
    public void updateTasks(List<BaseEntity> tasks) {
        this.tasks.clear();
        this.tasks.addAll(sort(tasks));
        tasksAdapter.notifyDataSetChanged();

    }

    @Override
    public void onTaskClicked(Task task) {
        System.out.println("MyTaskFrament onTaskClicked");
        if( this.listener != null)
            this.listener.onTaskClicked(task);
    }

    @Override
    public void onDelete(Task task) {
        taskPresenter.delete(task);
    }

    @Override
    public void onChangeStatus(Task task, Task.TaskStatus status) {
        taskPresenter.setStatus(task, status);
    }

    public interface OnMyTaskFragmentInteractionListener {
        void onTaskClicked(Task task);
    }
    private void setupRecyclerView()
    {
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new MyTasksRecyclerViewAdapter(tasks , getContext());
        tasksAdapter.setListener(this);
        recycler.setAdapter(tasksAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        taskPresenter.unSubscribe();
    }

}

