package app.whatsdone.release.chatApp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.whatsdone.release.chatApp.fragmentGroup.GroupFragment;
import app.whatsdone.release.chatApp.fragmentGroup.SettingFragment;
import app.whatsdone.release.chatApp.fragmentGroup.TaskFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                GroupFragment home = new GroupFragment();
                return home;
            case 1:
                TaskFragment about = new TaskFragment();
                return about;
            case 2:
                SettingFragment contact = new SettingFragment();
                return contact;
            default:
                return null;
        }
    }
}
