package whatsdone.app.whatsdone;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import whatsdone.app.whatsdone.adapters.PagerAdapterInnerGroup;

public class InnerGroupActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private PagerAdapterInnerGroup pagerAdapterInnerGroup;
    private TabLayout tabLayoutInnerGroup;
    private ViewPager viewPagerInnerGroup;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_inner);

        viewPagerInnerGroup = (ViewPager) findViewById(R.id.view_pager_inner_group);
        tabLayoutInnerGroup = (TabLayout) findViewById(R.id.tab_layout_inner_group);

        pagerAdapterInnerGroup = new PagerAdapterInnerGroup(getSupportFragmentManager());

        pagerAdapterInnerGroup.addInnerFragments(new ChatFragment(), "Chats");
        pagerAdapterInnerGroup.addInnerFragments(new TaskInnerGroupFragment(), "Tasks");

        viewPagerInnerGroup.setAdapter(pagerAdapterInnerGroup);
        tabLayoutInnerGroup.setupWithViewPager(viewPagerInnerGroup);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
