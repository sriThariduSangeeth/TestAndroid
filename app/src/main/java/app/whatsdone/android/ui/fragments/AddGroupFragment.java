package app.whatsdone.android.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.ContactServiceImpl;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = new Group();
        toolbar =  getActivity().findViewById(R.id.toolbar);
//        toolbar.setTitle("Add Group");
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setNavigationIcon(null);
                toolbar.setTitle("Whats Done");
                getActivity().onBackPressed();
            }
        });




    }



    @Override
    public void save() {

        String currentUser = AuthServiceImpl.getCurrentUser().getDocumentID();

        contactNumbers.add(currentUser);
        group.setCreatedBy(currentUser);
        if (group.getAdmins().size() == 0) {
            group.getAdmins().add(currentUser);
        }


        presenter.create(this.group);
    }

//    @Override
//    public void checkUserForName() {
//
//    }
//
//    @Override
//    public void checkUserForTeamImage() {
//
//            showPictureDialog();
//
//
//
//    }
//
//
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
}
