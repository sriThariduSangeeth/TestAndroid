package app.whatsdone.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.whatsdone.android.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GroupContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupContainerFragment extends Fragment implements
        AddGroupFragment.OnAddFragmentInteractionListener,
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        System.out.println("onAdd clicked");
        fragment = new AddGroupFragment();
        ((AddGroupFragment)fragment).setListener(this);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.group_container, fragment);
        fragmentTransaction.commit();
    }



}
