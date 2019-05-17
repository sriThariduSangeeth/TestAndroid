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

import whatsdone.app.whatsdone.adapters.GroupsRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.Group;
import whatsdone.app.whatsdone.presenter.GroupPresenter;
import whatsdone.app.whatsdone.presenter.GroupPresenterImpl;
import whatsdone.app.whatsdone.presenter.OnItemClick;
import whatsdone.app.whatsdone.view.GroupFragmentView;


public class GroupFragment extends Fragment implements GroupFragmentView,OnItemClick{

    private List<Group> groups = new ArrayList<>();
    private GroupsRecyclerViewAdapter adapter;
    private GroupPresenter presenter;




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

        adapter = new GroupsRecyclerViewAdapter(groups,this);
        myrecycler.setAdapter(adapter);
        System.out.println("oncreateview");


        this.presenter = new GroupPresenterImpl();
        this.presenter.init(this);
        this.presenter.loadGroups();

      //  this.presenter.onItemClick(this,groups.size());



        return view;
    }



    @Override
    public void updateGroups(List<Group> groups) {
        this.groups = groups;
        adapter.notifyDataSetChanged();

        System.out.println("updategroups");
    }


    @Override
    public void onItemClick(int position) {

        System.out.println("item clicked");
    }
}
