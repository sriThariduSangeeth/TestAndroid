package whatsdone.app.whatsdone.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.ChatFragment;
import whatsdone.app.whatsdone.TaskInnerGroupFragment;

public class PagerAdapterInnerGroup extends FragmentStatePagerAdapter {

    private Context context;
    private final List<Fragment> innerFragmentList = new ArrayList<>();
    private final List<String> innerFragmentTitleList = new ArrayList<>();


    public PagerAdapterInnerGroup(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new TaskInnerGroupFragment();

            case 1:
                return new ChatFragment();
        }
        return innerFragmentList.get(position);
    }

    public void addInnerFragments(Fragment fragment, String title)
    {
        innerFragmentList.add(fragment);
        innerFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return innerFragmentTitleList.get(position);
    }
}
