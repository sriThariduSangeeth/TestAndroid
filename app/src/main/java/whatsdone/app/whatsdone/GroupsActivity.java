package whatsdone.app.whatsdone;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import whatsdone.app.whatsdone.adapters.GroupsRecyclerViewAdapter;
import whatsdone.app.whatsdone.adapters.SwipeController;

public class GroupsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Context context=this;
    private Toolbar toolbar;
    public static GroupsActivity instance;
    private GroupContainerFragment groupContainerFragment;
    private MyTaskContainerFragment myTaskContainerFragment;
    private SettingFragment settingFragment;
    private GroupFragment groupFragment;
    private MyTaskFragment myTaskFragment;
    private GroupsRecyclerViewAdapter recyclerViewAdapter;



   // private int[] tabIcons = {R.drawable.group, R.drawable.task, R.drawable.settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

       // getAllWidgets();
        setupTabLayout();
        bindWidgetsWithAnEvent();


        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_ll, groupContainerFragment).commit();


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
                       getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_ll, groupContainerFragment).commit();
                       break;

                   case 1:
                       getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_ll, myTaskContainerFragment).commit();
                       break;

                   case 2:
                       getSupportFragmentManager().beginTransaction().replace(R.id.activity_groups_ll, settingFragment).commit();
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
       // groupFragment = new GroupFragment();
      //  myTaskFragment = new MyTaskFragment();

        tabLayout.addTab(tabLayout.newTab().setText("Groups").setIcon(R.drawable.group_tab_icon),true);
        tabLayout.addTab(tabLayout.newTab().setText("My Tasks").setIcon(R.drawable.task_tab_icon));
        tabLayout.addTab(tabLayout.newTab().setText("Settings").setIcon(R.drawable.settings_tab_icon));
    }

    public void setupRecyclerView()
    {
        // List<Group> groups = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.group_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerViewAdapter);

        SwipeController swipeController;
        swipeController = new SwipeController(new SwipeControllerActions()
        {
            @Override
            public void onRightClicked(int position) {
                recyclerViewAdapter.groups.remove(position);
                recyclerViewAdapter.notifyItemRemoved(position);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
    }


}
