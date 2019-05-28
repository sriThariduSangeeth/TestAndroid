package whatsdone.app.whatsdone;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.TaskInnerGroupRecyclerViewAdapter;
import whatsdone.app.whatsdone.adapters.TaskSwipeController;
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

        //OnBackPressedCallback callback = new OnBackPressedCallback(true);
        setHasOptionsMenu(true);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_inner_group_task, container, false);

        mainFab = view.findViewById(R.id.add_new_task);
        toolbar =  getActivity().findViewById(R.id.toolbar);



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





       //swipe
        TaskSwipeController taskswipeController = new TaskSwipeController(null);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(taskswipeController);
         itemTouchhelper.attachToRecyclerView(myrecycler);

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
                System.out.println("discussion clicked");
                System.out.println("in click");


                return true;


            case R.id.settings:

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
        System.out.println("method on start ");

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("method onResume");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
