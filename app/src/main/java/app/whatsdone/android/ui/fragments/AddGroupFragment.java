package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;


public class AddGroupFragment extends BaseFragment {


    public AddGroupFragment() {
        // Required empty public constructor
    }


    public static AddGroupFragment newInstance() {
        AddGroupFragment fragment = new AddGroupFragment();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
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
        presenter.create(this.group);
    }

}
