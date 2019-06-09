package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = new Group();
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Add Group");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
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

        presenter.create(this.group);
    }

}
