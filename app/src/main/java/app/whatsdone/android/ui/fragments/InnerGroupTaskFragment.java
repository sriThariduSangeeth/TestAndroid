package app.whatsdone.android.ui.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.activity.InnerGroupDiscussionActivity;
import app.whatsdone.android.ui.adapters.TaskInnerGroupRecyclerViewAdapter;
import app.whatsdone.android.ui.adapters.TaskSwipeController;
import app.whatsdone.android.ui.adapters.TaskSwipeControllerAction;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenterImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.DateUtil;
import app.whatsdone.android.utils.LocalState;
import timber.log.Timber;

public class InnerGroupTaskFragment extends Fragment implements TaskInnerGroupFragmentView {

    public ArrayList<String> listOfTask = new ArrayList<>();
    private List<BaseEntity> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    private TaskInnerGroupPresenter taskInnerGroupPresenter;
    private RecyclerView myRecycler;
    private TaskSwipeController taskSwipeController;
    private Group group = new Group();
    public EditText groupName;
    private TaskService service = new TaskServiceImpl();
    private TextView toolbarTextView;
    private List<BaseEntity> doneTaskList = new ArrayList<>();
    private List<BaseEntity> notDoneTaskList = new ArrayList<>();

    public static InnerGroupTaskFragment newInstance(Group group){

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

        View view =  inflater.inflate(R.layout.fragment_inner_group_task, container, false);

        FloatingActionButton mainFab = view.findViewById(R.id.add_new_task);
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbarTextView = getActivity().findViewById(R.id.toolbar_task_title);
        groupName = (EditText) view.findViewById(R.id.group_name_edit_text) ;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        myRecycler = view.findViewById(R.id.task_inner_group_recycler_view);
        this.taskInnerGroupPresenter = new TaskInnerGroupPresenterImpl();
        this.taskInnerGroupPresenter.init(this);
        this.taskInnerGroupPresenter.loadTasksInner(group.getDocumentID());

        //fab
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = AddTaskFragment.newInstance(group);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();
            }
        });

        setupRecyclerView();

        if(group.getGroupName().equals("Personal"))
        {
            setHasOptionsMenu(false);
        }

       return view;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


            inflater.inflate(R.menu.task_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {
         Intent intent = new Intent(getContext(), InnerGroupDiscussionActivity.class);
         intent.putExtra(Constants.REF_TEAMS, group);
         startActivity(intent);

         return true;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println(" detach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toolbar.setOnClickListener(null);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle("Whats Done");
        taskInnerGroupPresenter.unSubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void updateTaskInner(List<BaseEntity> tasks) {
        this.taskInnerGroups.clear();
        this.listOfTask.clear();
        taskInnerGroups.addAll(sort(tasks));
        listOfTask.addAll(putTaskListToArray(tasks));
        adapter.notifyDataSetChanged();
    }


    private Collection<? extends BaseEntity> sort(List<BaseEntity> tasks) {
        LocalState.getInstance().syncTasks(tasks);
        List<BaseEntity> unreadTasks = new ArrayList<>();
        List<BaseEntity> readTasks = new ArrayList<>();
        List<BaseEntity> doneTasks = new ArrayList<>();
        for (BaseEntity task : tasks) {
            if(((Task)task).getStatus() == Task.TaskStatus.DONE){
                doneTasks.add(task);
            }else {
                if(((Task)task).isUnreadTask())
                    unreadTasks.add(task);
                else
                    readTasks.add(task);
            }
        }

        // List1 = no done tasks
        // List 2 = done tasks
        // list1 sort by unread then by task label
        // append list 1 + list 2

        Collections.sort(unreadTasks, overdueTaskCompare);
        Collections.sort(readTasks, overdueTaskCompare);

        unreadTasks.addAll(readTasks);
        unreadTasks.addAll(doneTasks);

        return unreadTasks;
    }

    public static Comparator<BaseEntity> overdueTaskCompare =
            (entity1, entity2) -> {
                Task task1 = (Task)entity1;
                Task task2 = (Task)entity2;

                int task1Label = TaskInnerGroupRecyclerViewAdapter.getStatusIndicatorText(task1);
                int task2Label = TaskInnerGroupRecyclerViewAdapter.getStatusIndicatorText(task2);
                return task1Label == R.string.task_overdue ? -1 : task2Label == R.string.task_overdue ? 1
                        : task1Label == R.string.task_due_soon ? -1 : task2Label == R.string.task_due_soon ? 1
                        : 0;
            };


    private List<String> putTaskListToArray(List<BaseEntity> list){
        List<String> tasksList = new ArrayList<>();
        for (BaseEntity task: list) {
            tasksList.add(((Task) task).getTitle());
        }
        return tasksList;
    };

    private void setupRecyclerView()
    {
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskInnerGroupRecyclerViewAdapter(taskInnerGroups, getContext(), group);
        myRecycler.setAdapter(adapter);

        taskSwipeController = new TaskSwipeController(new TaskSwipeControllerAction() {

            @Override
            public void onTaskDeleteClicked(int position) {
                //task delete
                String id = taskInnerGroups.get(position).getDocumentID();
                taskInnerGroups.remove(position);
                adapter.notifyDataSetChanged();

                service.delete(id, new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        Timber.d("%s task deleted", id);
                    }
                });

            }

            @Override
            public void onTaskOnHoldClicked(int position) {
                //task change status to On Hold
                Task task = (Task) taskInnerGroups.get(position);
                task.setStatus(Task.TaskStatus.ON_HOLD);
                service.update(task, new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),"Updated Status",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onTaskInProgressClicked(int position) {
                //Task change status to In Progress
                Task task = new Task();
                task = (Task) taskInnerGroups.get(position);
                task.setStatus(Task.TaskStatus.IN_PROGRESS);
                service.update(task, new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),"Updated Status",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onTaskDoneClicked(int position) {
                //Task change status to Done
                Task task = new Task();
                task = (Task) taskInnerGroups.get(position);
                task.setStatus(Task.TaskStatus.DONE);
                service.update(task, new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),"Updated Status",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(taskSwipeController);
        itemTouchhelper.attachToRecyclerView(myRecycler);

        myRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                taskSwipeController.onDraw(c);
                taskSwipeController.setContext(getContext());
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

       // setHasOptionsMenu(true);
    }


}

