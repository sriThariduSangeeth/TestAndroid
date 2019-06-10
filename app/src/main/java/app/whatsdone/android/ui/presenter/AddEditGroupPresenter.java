package app.whatsdone.android.ui.presenter;

import android.app.Activity;

import java.util.List;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;

public interface AddEditGroupPresenter {
    void init(BaseGroupFragmentView view, Activity context);
    void create(Group group);
    void update(Group group);

}
