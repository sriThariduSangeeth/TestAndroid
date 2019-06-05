package app.whatsdone.android.ui.presenter;

import app.whatsdone.android.ui.view.GroupFragmentView;

public interface GroupPresenter {
    void init(GroupFragmentView view);
    void loadGroups();
    void addNewGroup(String nameGroup);
    void deleteGroup();

    //void onItemClick(GroupFragmentView view, int position);
    //void onLongItemClick(GroupFragmentView view, int position);

}
