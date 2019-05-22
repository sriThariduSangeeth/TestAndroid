package whatsdone.app.whatsdone.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.GroupFragment;
import whatsdone.app.whatsdone.MyTaskFragment;
import whatsdone.app.whatsdone.SettingFragment;

public class PagerAdapter extends FragmentStatePagerAdapter
{
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    private Context context;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GroupFragment();
               // return new MyTaskFragment();
            case 1:
               // return new GroupFragment();
                return new MyTaskFragment();
            case 2:
                return new SettingFragment();
        }
        return fragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);

    }

    @Override
    public int getCount() {
        return 3;
    }


    }

