package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.ui.view.AddGroupFragmentView;

public interface AddGroupPresenter {
    void init(AddGroupFragmentView view, Activity context);
    void create(Group group);
    void updateTeam(Group group);

}
