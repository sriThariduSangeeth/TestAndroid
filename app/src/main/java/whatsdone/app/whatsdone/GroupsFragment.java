package whatsdone.app.whatsdone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.Adapters.GroupsRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.Group;
import whatsdone.app.whatsdone.presenter.GroupPresenter;
import whatsdone.app.whatsdone.presenter.GroupPresenterImpl;
import whatsdone.app.whatsdone.view.GroupFragmentView;


public class GroupsFragment extends Fragment implements GroupFragmentView {

    private List<Group> groups = new ArrayList<>();
    private GroupsRecyclerViewAdapter adapter;
    private GroupPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_, container, false);

        RecyclerView myrecycler = view.findViewById(R.id.recycler_view_groups);
        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupsRecyclerViewAdapter(groups);
        myrecycler.setAdapter(adapter);


        this.presenter = new GroupPresenterImpl();
        this.presenter.init(this);
        this.presenter.loadGroups();

        return view;
    }


    @Override
    public void updateGroups(List<Group> groups) {
        this.groups = groups;
        adapter.notifyDataSetChanged();
    }



}
