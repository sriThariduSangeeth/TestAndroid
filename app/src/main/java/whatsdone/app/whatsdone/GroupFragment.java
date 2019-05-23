package whatsdone.app.whatsdone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.adapters.GroupsRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.Group;
import whatsdone.app.whatsdone.presenter.GroupPresenter;
import whatsdone.app.whatsdone.presenter.GroupPresenterImpl;
import whatsdone.app.whatsdone.presenter.ItemClickListener;
import whatsdone.app.whatsdone.view.GroupFragmentView;


public class GroupFragment extends Fragment implements GroupFragmentView {

    private List<Group> groups = new ArrayList<>();
    private GroupsRecyclerViewAdapter adapter;
    private GroupPresenter presenter;
    private OnGroupFragmentInteractionListener listener;

    private ItemClickListener clickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_, container, false);

        RecyclerView myrecycler = view.findViewById(R.id.group_recycler_view);
        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Group group =new Group();
            group.setGroupName("Group " + i);
            groups.add(group);
        }

        adapter = new GroupsRecyclerViewAdapter(groups, getContext());
//        adapter = new GroupsRecyclerViewAdapter(groups,this);
        myrecycler.setAdapter(adapter);
        System.out.println("oncreateview");




        this.presenter = new GroupPresenterImpl();
        this.presenter.init(this);
        this.presenter.loadGroups();

      //  this.presenter.onItemClick(this,groups.size());


        view.findViewById(R.id.fab_add_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                if(listener != null) {
                    System.out.println("listener");
                    listener.onAddClicked();
                }

            }
        });
     //   setHasOptionsMenu(true);
        return view;





    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_menu_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void updateGroups(List<Group> groups) {
        this.groups = groups;
        adapter.notifyDataSetChanged();

        System.out.println("updategroups");
    }
    public interface OnGroupFragmentInteractionListener {

        void onAddClicked();

    }

    public void setListener(OnGroupFragmentInteractionListener handler) {
            listener = handler;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
