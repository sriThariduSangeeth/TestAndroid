package app.whatsdone.android.ui.view;

import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;

public interface GroupFragmentView {
    void updateGroups(List<BaseEntity> groups);
}
