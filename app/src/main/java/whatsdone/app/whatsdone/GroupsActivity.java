package whatsdone.app.whatsdone;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import whatsdone.app.whatsdone.adapters.PagerAdapter;

public class GroupsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener  {

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context=this;

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
        adapter.addFragment(new MyTaskFragment(), "My Tasks");
        adapter.addFragment(new SettingFragment(), "Settings");

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);


        //tabLayout.setOnTabSelectedListener(this);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.container, new MyTaskFragment(), "fragment");
//        ft.commit();

    }
  /*  public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;


    }  */

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
