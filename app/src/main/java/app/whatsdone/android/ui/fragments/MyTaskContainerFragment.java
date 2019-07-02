package app.whatsdone.android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.utils.Constants;


public class MyTaskContainerFragment extends Fragment implements MyTaskFragment.OnMyTaskFragmentInteractionListener{
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
    
    @Override
    public void onTaskClicked(Task task) {
        Intent intent = new Intent(getActivity(), InnerGroupTaskActivity.class);
        intent.putExtra(Constants.ARG_ACTION, Constants.ACTION_VIEW_TASK);
        intent.putExtra(Constants.ARG_TASK, task);
        getActivity().startActivity(intent);

    }
}
