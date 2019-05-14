package whatsdone.app.whatsdone;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {R.drawable.group, R.drawable.task, R.drawable.settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Groups_Fragment(), "Groups");
        adapter.addFragment(new Tasks_Fragment(), "Tasks");
        adapter.addFragment(new Settings_Fragment(), "Settings");

        viewPager.setAdapter(adapter);


        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

}
