package app.whatsdone.android.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.fragments.AddGroupFragment;
import app.whatsdone.android.ui.fragments.AddTaskFragment;
import app.whatsdone.android.ui.fragments.EditTaskFragment;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragment;
import app.whatsdone.android.utils.Constants;

public class InnerGroupTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_group_task);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);

        if(getIntent().getExtras() != null) {

            Group group = getIntent().getExtras().getParcelable(Constants.ARG_GROUP);
            Task task = getIntent().getExtras().getParcelable(Constants.ARG_TASK);
            String action = getIntent().getExtras().getString(Constants.ARG_ACTION);
            Fragment fragment;
            if(action != null && action.equals(Constants.ACTION_VIEW_GROUP)) {
                fragment = AddGroupFragment.newInstance();
            }else if(action != null && action.equals(Constants.ACTION_ADD_TASK)){
                fragment = AddTaskFragment.newInstance(group);
            }else if(action != null && action.equals(Constants.ACTION_VIEW_TASK)){
                fragment = EditTaskFragment.newInstance(group, task, true);
            }

            else {
                fragment = InnerGroupTaskFragment.newInstance(group);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.task_container, fragment).commit();
        }
    }
}
