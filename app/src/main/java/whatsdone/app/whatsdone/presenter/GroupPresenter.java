package whatsdone.app.whatsdone.presenter;

import whatsdone.app.whatsdone.view.GroupFragmentView;

public interface GroupPresenter {
    void init(GroupFragmentView view);
    void loadGroups();

    //void onItemClick(GroupFragmentView view, int position);
    //void onLongItemClick(GroupFragmentView view, int position);

}
