package app.whatsdone.android.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.activity.InnerGroupDiscussionActivity;
import app.whatsdone.android.ui.adapters.TaskInnerGroupRecyclerViewAdapter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenterImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactReaderUtil;
import app.whatsdone.android.utils.LocalState;
import app.whatsdone.android.utils.UrlUtils;
import timber.log.Timber;

import static app.whatsdone.android.utils.Constants.ARG_TASK;
import static app.whatsdone.android.utils.SortUtil.sort;


public class InnerGroupTaskFragment extends Fragment implements TaskInnerGroupFragmentView, InnerGroupTaskFragmentListener {

    private List<BaseEntity> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    private TaskInnerGroupPresenter presenter;
    private RecyclerView myRecycler;
    private Group group = new Group();
    public EditText groupName;
    private TextView toolbarTextView;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Task task = new Task();
    private TaskService taskService = new TaskServiceImpl();


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
        this.presenter.init(this, group);
        this.presenter.loadTasksInner(group.getDocumentID());

        //fab
        mainFab.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment myFragment = AddTaskFragment.newInstance(group);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();
        });

        setupRecyclerView();

        if (group.getGroupName().equals("Personal")) {
            setHasOptionsMenu(false);
        }


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalState.getInstance().syncTasks(taskInnerGroups);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), InnerGroupDiscussionActivity.class);
        intent.putExtra(Constants.ARG_GROUP, group);
        intent.putExtra(ARG_TASK, Lists.transform(taskInnerGroups, t -> (Task)t).toArray());
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
        List<BaseEntity> sorted = sort(tasks);
        taskInnerGroups.addAll(sorted);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskInnerGroupRecyclerViewAdapter(taskInnerGroups, getContext(), group, this, getFragmentManager());
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

    @Override
    public void onTaskClicked(Task task) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalState.getInstance().markTasksRead(group.getDocumentID(), group.getTaskCount());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    new ContactReaderUtil(data, getContext(), task).selectContact(task -> {
                        task.setAssignedBy(AuthServiceImpl.getCurrentUser().getDocumentID());
                        task.setAssignedUserImage(UrlUtils.getUserImage(task.getAssignedUser()));
                        taskService.update(task, new ServiceListener() {
                            @Override
                            public void onSuccess() {
                                Timber.d("user updated");
                            }
                        });
                    });
                    break;
                }
        }

    }

    public void onContactButtonClicked(Task task) {
        this.task = task;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        } else {

            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onContactSelected(Task task) {
        task.setAcknowledged(false);
        taskService.update(task, new ServiceListener() {

            @Override
            public void onSuccess() {
                Timber.d("user updated");
            }
        });
    }
}

