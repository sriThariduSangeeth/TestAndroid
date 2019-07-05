package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.services.UserService;
import app.whatsdone.android.services.UserServiceImpl;
import app.whatsdone.android.ui.fragments.GroupContainerFragment;
import app.whatsdone.android.ui.fragments.GroupFragment;
import app.whatsdone.android.ui.fragments.MyTaskContainerFragment;
import app.whatsdone.android.ui.fragments.MyTaskFragment;
import app.whatsdone.android.ui.fragments.SettingFragment;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.LocalState;
import app.whatsdone.android.utils.UIUtil;
import timber.log.Timber;

import static app.whatsdone.android.utils.Constants.ARG_TASK;

public class GroupsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private GroupFragment groupContainerFragment;
    private MyTaskFragment myTaskContainerFragment;
    private SettingFragment settingFragment;
    GroupService groupService = GroupServiceImpl.getInstance();
    UserService userService = new UserServiceImpl();
    private Group notificationNavigationGroup;
    private TaskService taskService = new TaskServiceImpl();
    private List<BaseEntity> notificationNavigationTasks= new ArrayList<>();



    // private int[] tabIcons = {R.drawable.group, R.drawable.task, R.drawable.settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LocalState.getInstance().reloadFromDisk();
        tabLayout = findViewById(R.id.tab_layout);

        setupTabLayout();
        bindWidgetsWithAnEvent();


        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(null);

        Fragment fragment = groupContainerFragment;

        if(getIntent().getExtras() != null){
            Bundle args = getIntent().getExtras();
            if(args.containsKey(Constants.ARG_ACTION)){
                if(args.getString(Constants.ARG_ACTION).equals(Constants.ACTION_VIEW_TASK)){
                    fragment = myTaskContainerFragment;
                }
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, fragment).commit();
        ContactUtil.getInstance().getPermission(GroupsActivity.this);
        checkNotifications();
    }

    private void checkNotifications() {
        if(Constants.ALWAYS_NOTIFY){
            userService.enableNotifications();
        }
    }

    private void bindWidgetsWithAnEvent()
    {
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               int position = tab.getPosition();
               toolbar.setNavigationIcon(null);
               toolbar.setTitle(getString(R.string.app_name));
               switch (position)
               {
                   case 0:
                       getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, groupContainerFragment).commit();
                       break;

                   case 1:
                       getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, myTaskContainerFragment).commit();
                       break;

                   case 2:
                       getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, settingFragment).commit();
                       break;

               }
               UIUtil.hideSoftKeyboard(GroupsActivity.this);
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });
    }


    private void setupTabLayout()
    {
        groupContainerFragment = new GroupFragment();
        myTaskContainerFragment = new MyTaskFragment();
        settingFragment = new SettingFragment();

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.group)),true);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.my_tasks)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.settings)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart");
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //actionbar home button
            case android.R.id.home:
                Intent homeIntent = new Intent(this, GroupsActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                //onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalState.getInstance().persistData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle args = intent.getExtras();

        if(args != null && args.containsKey(Constants.ARG_ACTION)){
            if(args.getString(Constants.ARG_ACTION).equals(Constants.ACTION_VIEW_TASK)){

                getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, myTaskContainerFragment).commit();
                tabLayout.getTabAt(1).select();
            }
            else if(args.getString(Constants.ARG_ACTION).equals(Constants.ACTION_VIEW_DISCUSSION)){

                String groupId=args.getString(Constants.ARG_GROUP_ID);

                groupService.getGroupById(groupId, new ServiceListener() {
                    @Override
                    public void onDataReceived(BaseEntity entity) {
                        notificationNavigationGroup = (Group) entity;
                        Log.d("my group",notificationNavigationGroup.getGroupName());

                        taskService.getByGroupId(groupId, new ServiceListener() {
                            @Override
                            public void onDataReceived(List<BaseEntity> entities) {
                                notificationNavigationTasks= entities;
                                Intent DiscussionIntent;
                                DiscussionIntent = new Intent(GroupsActivity.this, InnerGroupDiscussionActivity.class);
                                DiscussionIntent.putExtra(Constants.ARG_GROUP, notificationNavigationGroup);
                                DiscussionIntent.putExtra(ARG_TASK, Lists.transform(notificationNavigationTasks, t -> (Task)t).toArray());
                                startActivity(DiscussionIntent);
                            }
                        });

                    }
                });





            }
        }
    }
}
