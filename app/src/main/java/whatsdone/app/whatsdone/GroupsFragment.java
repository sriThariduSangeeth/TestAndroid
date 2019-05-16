package whatsdone.app.whatsdone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import whatsdone.app.whatsdone.Adapters.GroupsRecyclerViewAdapter;
import whatsdone.app.whatsdone.model.Groups;


public class GroupsFragment extends Fragment {

    private ArrayList<Groups> groups = new ArrayList<>();
    private static RecyclerView myrecycler;
    private View view;
    private ArrayList<String> groupNames = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_, container, false);
/*
        myrecycler  = container.findViewById(R.id.recycler_view_groups);

        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        myrecycler.setAdapter(new GroupsRecyclerViewAdapter(groups));


      RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_groups);
      GroupsRecyclerViewAdapter groupsRecyclerViewAdapter = new GroupsRecyclerViewAdapter(groups);
      recyclerView.setAdapter(groupsRecyclerViewAdapter);
      RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
      recyclerView.setLayoutManager(layoutManager);


 */
      //  setRecyclerView();
        fillGroups();
        RecyclerView myrecycler = view.findViewById(R.id.recycler_view_groups);
        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        myrecycler.setAdapter(new GroupsRecyclerViewAdapter(groups));

        return view;
    }

    private void fillGroups()
    {
        Groups g= new Groups();
        g.setGroupName("Group 1");
        groups.add(g);


        g = new Groups();
        g.setGroupName("Group 2");
        groups.add(g);



    }

    /*

    private void setRecyclerView()
    {
        myrecycler = (RecyclerView) view.findViewById(R.id.recycler_view_groups);
        myrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));




    }
    */


}
