package whatsdone.app.whatsdone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import whatsdone.app.whatsdone.model.Groups;


public class GroupsFragment extends Fragment {

 //   private ArrayList<Groups> groups = new ArrayList<>();
 //   RecyclerView myrecycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups_, container, false);

      /*  myrecycler  = container.findViewById(R.id.recycler_view_groups);

        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        myrecycler.setAdapter(new GroupsRecyclerViewAdapter(listA));

       */


    }


}
