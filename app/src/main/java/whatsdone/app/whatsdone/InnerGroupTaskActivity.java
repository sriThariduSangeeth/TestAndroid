package whatsdone.app.whatsdone;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.TaskInnerGroupRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.TaskInnerGroup;

public class InnerGroupTaskActivity extends AppCompatActivity {

    private List<TaskInnerGroup> taskInnerGroups = new ArrayList<>();
    private TaskInnerGroupRecyclerViewAdapter adapter;
    private FloatingActionButton mainFab, createNewTaskFab, viewDiscussionsFab;
    private TextView textViewDiscussion, textViewNewTask;
    boolean isOpen = false;
    Animation fab_open;
    Animation fab_close;
    Animation fab_rotate_anticlock;
    Animation fab_rotate_clock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_group_task);

        mainFab = findViewById(R.id.fab_inner_group_task);
        createNewTaskFab = findViewById(R.id.create_new_task_fab);
        viewDiscussionsFab = findViewById(R.id.view_discussion_fab);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);

        fab_rotate_anticlock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_anticlock);
        fab_rotate_clock = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_rotate_clock);


        textViewDiscussion = (TextView) findViewById(R.id.discussion_text_view);
        textViewNewTask = (TextView) findViewById(R.id.task_text_view);

        //animations for fab

        textViewDiscussion.setVisibility(View.INVISIBLE);
        textViewNewTask.setVisibility(View.INVISIBLE);

        //createNewTaskFab.setVisibility(View.INVISIBLE);
        createNewTaskFab.startAnimation(fab_close);
        viewDiscussionsFab.startAnimation(fab_close);

        //fab
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isOpen)
                {
                    textViewDiscussion.setVisibility(View.INVISIBLE);
                    textViewNewTask.setVisibility(View.INVISIBLE);

                    //createNewTaskFab.setVisibility(View.INVISIBLE);
                   createNewTaskFab.startAnimation(fab_close);
                    viewDiscussionsFab.startAnimation(fab_close);
                    mainFab.startAnimation(fab_rotate_anticlock);

                    viewDiscussionsFab.setClickable(false);
                    createNewTaskFab.setClickable(false);

                    isOpen = false;


                }

                else
                {
                    textViewDiscussion.setVisibility(View.VISIBLE);
                    textViewNewTask.setVisibility(View.VISIBLE);

                    createNewTaskFab.startAnimation(fab_open);
                    viewDiscussionsFab.startAnimation(fab_open);
                    mainFab.startAnimation(fab_rotate_clock);

                    viewDiscussionsFab.setClickable(true);
                    createNewTaskFab.setClickable(true);

                    isOpen = true;
                }



                // Snackbar.make(v,"Hello", Snackbar.LENGTH_LONG).show();
            }
        });

        viewDiscussionsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, InnerGroupDiscussionActivity.class);
                context.startActivity(intent);
               // Toast.makeText(getApplicationContext(), "Discussion", Toast.LENGTH_SHORT).show();

            }
        });

        createNewTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CreateNewTaskActivity.class);
                context.startActivity(intent);

                //Toast.makeText(getApplicationContext(), "new task", Toast.LENGTH_SHORT).show();


            }
        });


        RecyclerView myrecycler = findViewById(R.id.task_inner_group_recycler_view);
        myrecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        List<TaskInnerGroup> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TaskInnerGroup task =new TaskInnerGroup();
            task.setTaskName("Task " + i);
            tasks.add(task);
        }


        adapter =new TaskInnerGroupRecyclerViewAdapter(tasks, getBaseContext());
        myrecycler.setAdapter(adapter);


       //back button
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
       // return super.onSupportNavigateUp();
    }
}
