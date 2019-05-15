package whatsdone.app.whatsdone;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import whatsdone.app.whatsdone.model.Groups;

public class GroupsActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context=this;
  //  private String text;
    private ArrayList<Groups> groups= new ArrayList<>();
    private int[] tabIcons = {R.drawable.group, R.drawable.task, R.drawable.settings};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new GroupsFragment(), "Groups");
        adapter.addFragment(new TasksFragment(), "Tasks");
        adapter.addFragment(new SettingsFragment(), "Settings");

        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    /*   fillGroups();

        RecyclerView myrecycler = findViewById(R.id.recycler_view_groups);
        myrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        GroupsRecyclerViewAdapter myadapter = new GroupsRecyclerViewAdapter(groups);
        myrecycler.setAdapter(myadapter);
*/

    }


    private void fillGroups()
    {
        Groups g= new Groups();
        g.setGroupName("Group 1");
        groups.add(g);


        g = new Groups();
        g.setGroupName("Group 2");
        groups.add(g);

    }

}
