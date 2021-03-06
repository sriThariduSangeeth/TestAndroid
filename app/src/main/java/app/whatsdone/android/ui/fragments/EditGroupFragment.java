package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.utils.Constants;


public class EditGroupFragment extends BaseFragment{

    public EditGroupFragment() {

    }

    public static EditGroupFragment newInstance(Group group) {

        EditGroupFragment fragment = new EditGroupFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.group = getArguments().getParcelable(Constants.ARG_GROUP);
        }



    }

    @Override
    public void save() {

        presenter.update(this.group);
    }

    @Override
    public void checkUserForName() {
        System.out.println("  " + AuthServiceImpl.getCurrentUser().getPhoneNo());
        if (!AuthServiceImpl.getCurrentUser().getPhoneNo().equals(group.getCreatedBy())) {
            teamName.setEnabled(false);
        }

        toolbarTitle.setText(getString(R.string.group_settings));

    }

    @Override
    public void checkUserForTeamImage() {

        if(AuthServiceImpl.getCurrentUser().getPhoneNo().equals(group.getCreatedBy()))
            showPictureDialog();


        else
            Toast.makeText(getContext(), "Only admin can edit team details", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void checkUserToAddMembers() {
        if(!AuthServiceImpl.getCurrentUser().getPhoneNo().equals(group.getCreatedBy()) || isPersonalGroup()) {
            addMembers.setEnabled(false);
            addMembers.setVisibility(View.GONE);
            swipeListView.setEnabled(true);
            teamName.setEnabled(false);
           // swipeListView.setActivated(false);
            swipeListView.setClickable(false);
            saveFab.setEnabled(false);
            saveFab.hide();
        }

    }

    private boolean isPersonalGroup() {
        return group.getDocumentID().equals(AuthServiceImpl.getCurrentUser().getDocumentID());
    }


}
