package app.whatsdone.android.ui.presenter;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.ui.model.Group;
import app.whatsdone.android.ui.view.GroupFragmentView;

public class GroupPresenterImpl implements GroupPresenter{
    private GroupFragmentView view;

    @Override
    public void init(GroupFragmentView view) {
        this.view = view;
        System.out.println("init");
    }

    @Override
    public void loadGroups() {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Group group =new Group();
            group.setGroupName("Group " + i);
            groups.add(group);
        }
        System.out.println("loadGroups");
        this.view.updateGroups(groups);
    }
/*
    @Override
    public void onItemClick(GroupFragmentView view, int position) {
        System.out.println("item click");
    }

    @Override
    public void onLongItemClick(GroupFragmentView view, int position) {
        System.out.println("long click");
    }  */

}

