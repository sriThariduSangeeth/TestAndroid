package app.whatsdone.android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.whatsdone.android.R;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.utils.Constants;

import static app.whatsdone.android.utils.Constants.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GroupContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupContainerFragment extends Fragment implements
        BaseFragment.OnAddFragmentInteractionListener,
        GroupFragment.OnGroupFragmentInteractionListener{
    Fragment fragment;

   // private EditGroupFragment.OnEditFragmentInteractionListener handler;
    //private InnerGroupTaskFragment.OnGroupEditFragmentInteractionListener handler;

    FragmentManager fragmentManager;

    public GroupContainerFragment() {
        // Required empty public constructor
    }


    public static GroupContainerFragment newInstance(String param1, String param2) {
        GroupContainerFragment fragment = new GroupContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_group_container, container, false);
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new GroupFragment();
        ((GroupFragment)fragment).setListener(this);
        fragmentTransaction.add(R.id.group_container, fragment);
        fragmentTransaction.commit();


        return view;
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onAddClicked() {
        Intent intent = new Intent(getActivity(), InnerGroupTaskActivity.class);
        intent.putExtra(ARG_ACTION, ACTION_VIEW_GROUP);
        getActivity().startActivity(intent);
    }



}
