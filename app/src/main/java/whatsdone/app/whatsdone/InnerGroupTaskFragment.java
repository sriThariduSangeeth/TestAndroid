package whatsdone.app.whatsdone;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import whatsdone.app.whatsdone.adapters.TaskInnerGroupRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.TaskInnerGroup;

public class InnerGroupTaskFragment extends Fragment {

    private List<TaskInnerGroup> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private FloatingActionButton mainFab;
    private Toolbar toolbar;
    private MenuItem menuItem;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_inner_group_task, container, false);

        mainFab = view.findViewById(R.id.add_new_task);

        RecyclerView myrecycler = view.findViewById(R.id.task_inner_group_recycler_view);
        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        List<TaskInnerGroup> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TaskInnerGroup task = new TaskInnerGroup();
            task.setTaskName("Task " + i);
            tasks.add(task);
        }


        adapter = new TaskInnerGroupRecyclerViewAdapter(tasks, getContext());
        myrecycler.setAdapter(adapter);

        //fab
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new CreateNewTaskFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.group_container, myFragment).addToBackStack(null).commit();




            }
        });


        toolbar =  getActivity().findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.task_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
