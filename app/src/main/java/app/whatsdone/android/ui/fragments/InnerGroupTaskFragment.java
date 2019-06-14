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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
import app.whatsdone.android.ui.presenter.AddEditGroupPresenter;
import app.whatsdone.android.ui.presenter.AddEditGroupPresenterImpl;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenterImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;
import app.whatsdone.android.utils.Constants;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class InnerGroupTaskFragment extends Fragment implements TaskInnerGroupFragmentView {

    private static Group groupobj;
    private List<BaseEntity> taskInnerGroups = new ArrayList<>();
    //private List<Task> taskListInnerGroups = new ArrayList<>();
    private ArrayList<String> listOfTask = new ArrayList<>();
//    private TaskList taskList = new TaskList();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private FloatingActionButton mainFab;
    private Toolbar toolbar;
    private MenuItem menuItem;
    private TaskInnerGroupPresenter taskInnerGroupPresenter;
    private RecyclerView myRecycler;
    private TaskSwipeController taskSwipeController;
    private Fragment fragment;
    private GroupFragment groupFragment;
    private Group group = new Group();
    private EditGroupFragment editFragment = new EditGroupFragment();
    private CircleImageView  circleImageView;
    public EditText groupName;
    private List<String> contacts = new ArrayList<String>();
    private ListView contactListView;
    private AddEditGroupPresenter presenter;
    private TaskService service = new TaskServiceImpl();
    private TextView toolbarTextView;

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

        mainFab = view.findViewById(R.id.add_new_task);
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbarTextView = getActivity().findViewById(R.id.toolbar_task_title);
        circleImageView = (CircleImageView) view.findViewById(R.id.group_photo_image_view);
        groupName = (EditText) view.findViewById(R.id.group_name_edit_text) ;
        contactListView = (ListView) view.findViewById(R.id.add_members_list_view);

        Bundle args = getArguments();
        this.group = args.getParcelable("group");
        LocalState.getInstance().markTasksRead(group.getDocumentID(), group.getTaskCount());
        toolbarTextView.setText(group.getGroupName());
        toolbarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) getContext();
                Fragment myFragment = EditGroupFragment.newInstance(group);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();

                toolbarTextView.setClickable(false);
            }

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
        presenter = new AddEditGroupPresenterImpl();

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
       return view;

    }

    private List<String> putTaskListToArray(List<BaseEntity> list){
        List<String> tasksList = new ArrayList<>();
        for (BaseEntity task: list) {
            tasksList.add(((Task) task).getTitle());
        }

        return tasksList;
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.task_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {

        switch (item.getItemId())
        {
            case R.id.discussion:
                //open discussion activity
                Intent intent = new Intent(getContext(), InnerGroupDiscussionActivity.class);
                intent.putExtra(Constants.REF_TEAMS, group);
                intent.putStringArrayListExtra(Constants.REF_TASKS, listOfTask);
                startActivity(intent);

                return true;


             default:
                 break;
        }

        return false;
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
        taskInnerGroups.addAll(tasks);
        listOfTask.addAll(putTaskListToArray(tasks));
        adapter.notifyDataSetChanged();
    }

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

