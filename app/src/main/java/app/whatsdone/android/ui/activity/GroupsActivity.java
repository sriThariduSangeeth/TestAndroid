package app.whatsdone.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;

import java.sql.Time;

import app.whatsdone.android.R;
import app.whatsdone.android.ui.adapters.GroupsRecyclerViewAdapter;
import app.whatsdone.android.ui.fragments.AddGroupFragment;
import app.whatsdone.android.ui.fragments.BaseFragment;
import app.whatsdone.android.ui.fragments.GroupContainerFragment;
import app.whatsdone.android.ui.fragments.GroupFragment;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragment;
import app.whatsdone.android.ui.fragments.MyTaskContainerFragment;
import app.whatsdone.android.ui.fragments.MyTaskFragment;
import app.whatsdone.android.ui.fragments.SettingFragment;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.LocalState;
import timber.log.Timber;

public class GroupsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private GroupContainerFragment groupContainerFragment;
    private MyTaskContainerFragment myTaskContainerFragment;
    private SettingFragment settingFragment;


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
        groupContainerFragment = new GroupContainerFragment();
        myTaskContainerFragment = new MyTaskContainerFragment();
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
                tabLayout.getTabAt(1).select();
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, myTaskContainerFragment).commit();
            }
        }
    }
}
