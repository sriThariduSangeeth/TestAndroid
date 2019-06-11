package app.whatsdone.android.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Group group = getArguments().getParcelable("group");
            this.group = group;
        }


//        teamName.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                checkUserForName();
//
//                return true;
//            }
//        });


    }

    @Override
    public void save() {

        presenter.update(this.group);
    }

//    @Override
//    public void checkUserForName() {
//
//            if (!AuthServiceImpl.getCurrentUser().toString().equals(group.getCreatedBy())) {
//                teamName.setEnabled(false);
//               // Toast.makeText(getContext(), "Only the creator can change Team image", Toast.LENGTH_SHORT).show();
//               // teamName.setError("Only the creator can change Team name");
//
//            }
//
//
//
//
//
//    }
//
//    @Override
//    public void checkUserForTeamImage() {
//        if(AuthServiceImpl.getCurrentUser().toString().equals(group.getCreatedBy()))
//            showPictureDialog();
//
//
//        else
//            Toast.makeText(getContext(), "Only admin can change the team photo", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//


}
