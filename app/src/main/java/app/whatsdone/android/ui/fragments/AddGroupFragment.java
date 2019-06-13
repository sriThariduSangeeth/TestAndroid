package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;


public class AddGroupFragment extends BaseFragment {

    private Toolbar toolbar;

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
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.add_group));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(null);
                toolbar.setTitle(getString(R.string.app_name));
                getActivity().onBackPressed();
            }
        });



    }



    @Override
    public void save() {
        String currentUser = AuthServiceImpl.getCurrentUser().getDocumentID();
        group.setCreatedBy(currentUser);
      //  group.getAdmins().add(currentUser);
        if (group.getAdmins().isEmpty()) {
            group.getAdmins().add(currentUser);
        }


        presenter.create(this.group);
    }

    @Override
    public void checkUserForName() {
        teamName.setEnabled(true);
        toolbarTitle.setText(getString(R.string.add_new_group));
    }

    @Override
    public void checkUserForTeamImage() {

        showPictureDialog();

    }

    @Override
    public void checkUserToAddMembers() {


    }

}
