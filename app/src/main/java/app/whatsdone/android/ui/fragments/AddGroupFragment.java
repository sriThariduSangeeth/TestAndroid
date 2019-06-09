package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;


public class AddGroupFragment extends BaseFragment {


    public AddGroupFragment() {
        // Required empty public constructor
    }


    public static AddGroupFragment newInstance() {
        AddGroupFragment fragment = new AddGroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = new Group();

    }



    @Override
    public void save() {

        String currentUser = AuthServiceImpl.user.getDocumentID();

        contactNumbers.add(currentUser);
        group.setCreatedBy(currentUser);
        if(group.getAdmins().size()==0) {
            group.getAdmins().add(currentUser);
        }
        presenter.create(this.group);
    }

}
