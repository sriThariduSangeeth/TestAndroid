package whatsdone.app.whatsdone.presenter;

import whatsdone.app.whatsdone.view.GroupFragmentView;

public interface GroupPresenter {
    void init(GroupFragmentView view);
    void loadGroups();
}
