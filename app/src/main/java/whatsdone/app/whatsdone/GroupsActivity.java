package whatsdone.app.whatsdone;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import whatsdone.app.whatsdone.adapters.PagerAdapter;
import whatsdone.app.whatsdone.adapters.SwipeController;

public class GroupsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context=this;
    private Toolbar toolbar;

   // private int[] tabIcons = {R.drawable.group, R.drawable.task, R.drawable.settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_groups);
       viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        adapter = new PagerAdapter(getSupportFragmentManager());
        // adapter.addFragment(new GroupFragment(), "Group");
        adapter.addFragment(new GroupContainerFragment(), "Groups");
        adapter.addFragment(new MyTaskContainerFragment(), "My Tasks");
        adapter.addFragment(new SettingFragment(), "Settings");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

/*
        adapter = new PagerAdapter(getSupportFragmentManager());
       // adapter.addFragment(new GroupFragment(), "Group");
        adapter.addFragment(new GroupContainerFragment(), "Groups");
        adapter.addFragment(new MyTaskContainerFragment(), "My Tasks");
        adapter.addFragment(new SettingFragment(), "Settings");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
*/

        //tabLayout.setOnTabSelectedListener(this);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.container, new MyTaskFragment(), "fragment");
//        ft.commit();

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // viewpager swipe disable
         /*
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.setCurrentItem(viewPager.getCurrentItem());

                return true;
            }
        });

        */

        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        RecyclerView recyclerView = findViewById(R.id.group_recycler_view);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        //swipe
       // viewPager.beginFakeDrag();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
