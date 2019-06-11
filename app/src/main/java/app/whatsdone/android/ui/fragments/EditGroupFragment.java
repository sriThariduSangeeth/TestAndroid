package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthService;


public class EditGroupFragment extends BaseFragment{

    public EditGroupFragment() {

    }

    public static EditGroupFragment newInstance(Group group) {

        EditGroupFragment fragment = new EditGroupFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", group);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Group group = getArguments().getParcelable("group");
            this.group = group;
        }

    }

    @Override
    public void save() {

        presenter.update(this.group);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
}
