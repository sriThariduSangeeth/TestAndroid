package app.whatsdone.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;

import app.whatsdone.android.R;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.ui.adapters.GroupsRecyclerViewAdapter;
import app.whatsdone.android.ui.fragments.AddGroupFragment;
import app.whatsdone.android.ui.fragments.GroupContainerFragment;
import app.whatsdone.android.ui.fragments.GroupFragment;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragment;
import app.whatsdone.android.ui.fragments.MyTaskContainerFragment;
import app.whatsdone.android.ui.fragments.MyTaskFragment;
import app.whatsdone.android.ui.fragments.SettingFragment;

public class GroupsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Context context=this;
    private Toolbar toolbar;
    private static GroupsActivity instance;
    private GroupContainerFragment groupContainerFragment;
    private MyTaskContainerFragment myTaskContainerFragment;
    private SettingFragment settingFragment;
    private GroupFragment groupFragment;
    private MyTaskFragment myTaskFragment;
    private GroupsRecyclerViewAdapter recyclerViewAdapter;
    private AddGroupFragment addGroupFragment = new AddGroupFragment();
    private InnerGroupTaskFragment innerGroupTaskFragment;



   // private int[] tabIcons = {R.drawable.group, R.drawable.task, R.drawable.settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        tabLayout = findViewById(R.id.tab_layout);

        setupTabLayout();
        bindWidgetsWithAnEvent();


        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_constraint_layout, groupContainerFragment).commit();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        System.out.println("Width  =" + width);
        System.out.println("height = "+ height);

    }

    private void bindWidgetsWithAnEvent()
    {
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               int position = tab.getPosition();

               switch (tab.getPosition())
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

    public static GroupsActivity getInstance()
    {
        return instance;
    }


    private void setupTabLayout()
    {
        groupContainerFragment = new GroupContainerFragment();
        myTaskContainerFragment = new MyTaskContainerFragment();
        settingFragment = new SettingFragment();

        tabLayout.addTab(tabLayout.newTab().setText("Teams"),true);
        tabLayout.addTab(tabLayout.newTab().setText("My Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
       // tabLayout.addTab(tabLayout.newTab().setText("Teams").setIcon(R.drawable.group_tab_icon),true);

    }

//
//    @Override
//    public void onBackPressed() {
//        FragmentManager fragmentManager;
//        fragmentManager = getSupportFragmentManager();
//
//        addGroupFragment = (AddGroupFragment) getSupportFragmentManager().findFragmentByTag("Add_Group_Fragment");
//        if(addGroupFragment !=null && addGroupFragment.isVisible())
//        {
//            fragmentManager.popBackStack();
//        }
//
//    }

}
