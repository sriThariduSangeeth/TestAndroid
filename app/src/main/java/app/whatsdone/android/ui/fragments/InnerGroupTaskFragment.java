package app.whatsdone.android.ui.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.activity.InnerGroupDiscussionActivity;
import app.whatsdone.android.ui.adapters.TaskInnerGroupRecyclerViewAdapter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenterImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.LocalState;

import static app.whatsdone.android.utils.SortUtil.sort;

public class InnerGroupTaskFragment extends Fragment implements TaskInnerGroupFragmentView, TaskInnerGroupRecyclerViewAdapter.SwipeListener
{

    public ArrayList<String> listOfTask = new ArrayList<>();
    private List<BaseEntity> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    private TaskInnerGroupPresenter presenter;
    private RecyclerView myRecycler;
    private Group group = new Group();
    public EditText groupName;
    private TextView toolbarTextView;

    public static InnerGroupTaskFragment newInstance(Group group) {

        InnerGroupTaskFragment instance = new InnerGroupTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //OnBackPressedCallback callback = new OnBackPressedCallback(true);
        setHasOptionsMenu(true);
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inner_group_task, container, false);

        FloatingActionButton mainFab = view.findViewById(R.id.add_new_task);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbarTextView = getActivity().findViewById(R.id.toolbar_task_title);
        groupName = view.findViewById(R.id.group_name_edit_text);

        Bundle args = getArguments();
        this.group = args.getParcelable("group");
        LocalState.getInstance().markTasksRead(group.getDocumentID(), group.getTaskCount());
        toolbarTextView.setText(group.getGroupName());
        toolbar.setOnClickListener(v -> {

            AppCompatActivity activity = (AppCompatActivity) getContext();
            Fragment myFragment = EditGroupFragment.newInstance(group);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();

            toolbarTextView.setClickable(false);
        });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        myRecycler = view.findViewById(R.id.task_inner_group_recycler_view);
        this.presenter = new TaskInnerGroupPresenterImpl();
        this.presenter.init(this);
        this.presenter.loadTasksInner(group.getDocumentID());

        //fab
        mainFab.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment myFragment = AddTaskFragment.newInstance(group);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();
        });

        setupRecyclerView();
        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.task_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), InnerGroupDiscussionActivity.class);
        intent.putExtra(Constants.REF_TEAMS, group);
        startActivity(intent);

        return true;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbar.setOnClickListener(null);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle("Whats Done");
        presenter.unSubscribe();
    }

    @Override
    public void updateTaskInner(List<BaseEntity> tasks) {
        this.taskInnerGroups.clear();
        this.listOfTask.clear();
        taskInnerGroups.addAll(sort(tasks));
        listOfTask.addAll(putTaskListToArray(tasks));
        adapter.notifyDataSetChanged();
    }

    private List<String> putTaskListToArray(List<BaseEntity> list) {
        List<String> tasksList = new ArrayList<>();
        for (BaseEntity task : list) {
            tasksList.add(((Task) task).getTitle());
        }
        return tasksList;
    }

    private void setupRecyclerView() {
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskInnerGroupRecyclerViewAdapter(taskInnerGroups, getContext(), group, this);
        myRecycler.setAdapter(adapter);
    }

    @Override
    public void onDelete(Task task) {
        presenter.deleteTaskInner(task.getDocumentID());
    }

    @Override
    public void onChangeStatus(Task task, Task.TaskStatus status) {
        presenter.setStatus(task, status);
    }
}

