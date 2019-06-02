package app.whatsdone.android.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.whatsdone.android.R;


public class MyTaskContainerFragment extends Fragment implements MyTaskDetailsFragment.OnFragmentInteractionListener, MyTaskFragment.OnMyTaskFragmentInteractionListener{
    Fragment fragment;

    FragmentManager fragmentManager;


    public MyTaskContainerFragment() {
        // Required empty public constructor
    }

    public static MyTaskContainerFragment newInstance(String param1, String param2) {
        MyTaskContainerFragment fragment = new MyTaskContainerFragment();
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
        View view =   inflater.inflate(R.layout.fragment_my_task_container, container, false);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new MyTaskFragment();
        System.out.println("setting listener in my task");
        ((MyTaskFragment)fragment).setListener(this);
        fragmentTransaction.add(R.id.my_task_container, fragment);
        fragmentTransaction.commit();



        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTaskClicked() {
        System.out.println("Add Task clicked");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new MyTaskDetailsFragment();
        ((MyTaskDetailsFragment)fragment).setListener(this);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.my_task_container, fragment);
        fragmentTransaction.commit();

    }
}
