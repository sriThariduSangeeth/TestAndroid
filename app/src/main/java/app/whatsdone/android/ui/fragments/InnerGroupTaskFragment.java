package app.whatsdone.android.ui.fragments;


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

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.ui.activity.InnerGroupDiscussionActivity;
import app.whatsdone.android.ui.adapters.TaskInnerGroupRecyclerViewAdapter;
import app.whatsdone.android.ui.adapters.TaskSwipeController;
import app.whatsdone.android.model.TaskInnerGroup;
import app.whatsdone.android.ui.adapters.TaskSwipeControllerAction;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenter;
import app.whatsdone.android.ui.presenter.TaskInnerGroupPresenterImpl;
import app.whatsdone.android.ui.view.TaskInnerGroupFragmentView;
import app.whatsdone.android.utils.Constants;

public class InnerGroupTaskFragment extends Fragment implements TaskInnerGroupFragmentView {

    private List<BaseEntity> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private FloatingActionButton mainFab;
    private Toolbar toolbar;
    private MenuItem menuItem;
    private TaskInnerGroupPresenter taskInnerGroupPresenter;
    private RecyclerView myRecycler;
    private TaskSwipeController taskSwipeController;
    private Fragment fragment;
    private GroupFragment groupFragment;


    public static InnerGroupTaskFragment newInstance(Group group){
        InnerGroupTaskFragment instance = new InnerGroupTaskFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_GROUP_ID, group.getDocumentID());
        args.putString(Constants.ARG_GROUP_NAME, group.getGroupName());
        instance.setArguments(args);
        return instance;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //OnBackPressedCallback callback = new OnBackPressedCallback(true);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_inner_group_task, container, false);

        mainFab = view.findViewById(R.id.add_new_task);
        toolbar =  getActivity().findViewById(R.id.toolbar);

        Bundle args = getArguments();
        String groupId = args.getString(Constants.ARG_GROUP_ID);
        String groupName = args.getString(Constants.ARG_GROUP_NAME);
        toolbar.setTitle(groupName);

        myRecycler = view.findViewById(R.id.task_inner_group_recycler_view);

        this.taskInnerGroupPresenter = new TaskInnerGroupPresenterImpl();
        this.taskInnerGroupPresenter.init(this);
        this.taskInnerGroupPresenter.loadTasksInner(groupId);

        //fab
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new CreateNewTaskFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.group_container, myFragment).addToBackStack(null).commit();




            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
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

        switch (item.getItemId())
        {
            case R.id.discussion:
//                //this is for open discussion
//                fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragment = new InnerGroupDiscussionFragment();
//                fragmentTransaction.add(R.id.discussion, fragment);
//                fragmentTransaction.commit();

//                AppCompatActivity activity = (AppCompatActivity) getContext();
//                Fragment myFragment = new CreateNewTaskFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.group_container, myFragment).addToBackStack(null).commit();
                Intent intent = new Intent(getContext(), InnerGroupDiscussionActivity.class);
                startActivity(intent);

                return true;


            case R.id.settings:

//                AppCompatActivity activity = (AppCompatActivity) getContext();
//                Fragment myFragment = new AddGroupFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.group_container, myFragment).addToBackStack(null).commit();
//

                System.out.println("settings clicked");
                return false;

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
        taskInnerGroupPresenter.unSubscribe();
        System.out.println("on destroy view");
    }

    @Override
    public void onStop() {
        super.onStop();

        //getTargetFragment().setMenuVisibility(true);
    }

    @Override
    public void updateTaskInner(List<BaseEntity> tasks) {
        this.taskInnerGroups.clear();
        taskInnerGroups.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView()
    {
        myRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskInnerGroupRecyclerViewAdapter(taskInnerGroups, getContext());
        myRecycler.setAdapter(adapter);

        taskSwipeController = new TaskSwipeController(new TaskSwipeControllerAction() {

            @Override
            public void onTaskDeleteClicked(int position) {
                System.out.println("delete");
                adapter.taskList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());

            }


            @Override
            public void onTaskOnHoldClicked(int position) {
                System.out.println("On Hold");

            }

            @Override
            public void onTaskInProgressClicked(int position) {
                System.out.println("In progress");
            }

            @Override
            public void onTaskDoneClicked(int position) {
                System.out.println("Done ");
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(taskSwipeController);
        itemTouchhelper.attachToRecyclerView(myRecycler);

        myRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                taskSwipeController.onDraw(c);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

       // setHasOptionsMenu(true);
    }
}
