package app.whatsdone.android.ui.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.ui.adapters.GroupsRecyclerViewAdapter;
import app.whatsdone.android.ui.adapters.GroupSwipeController;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.ui.presenter.GroupPresenter;
import app.whatsdone.android.ui.presenter.GroupPresenterImpl;
import app.whatsdone.android.ui.view.GroupFragmentView;
import app.whatsdone.android.ui.adapters.GroupSwipeControllerActions;


public class GroupFragment extends Fragment implements GroupFragmentView {

    private List<BaseEntity> groups = new ArrayList<>();
    private GroupsRecyclerViewAdapter adapter;
    private GroupPresenter presenter;
    private OnGroupFragmentInteractionListener listener;
    private Toolbar toolbar;
    private View view;
    private GroupSwipeController groupSwipeController;
    private RecyclerView myrecycler;
    //private GroupSwipeControllerActions buttonsActions = null;


//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//
//        menu.clear();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_, container, false);

        myrecycler = view.findViewById(R.id.group_recycler_view);

        this.presenter = new GroupPresenterImpl();
        this.presenter.init(this, getActivity());


        view.findViewById(R.id.fab_add_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener != null) {

                    listener.onAddClicked();
                }

            }
        });

        setupRecyclerView();
        return view;

    }


    @Override
    public void updateGroups(List<BaseEntity> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        adapter.notifyDataSetChanged();


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
        presenter.unSubscribe();
    }

    private void setupRecyclerView()
    {


        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupsRecyclerViewAdapter(groups, getContext());
        myrecycler.setAdapter(adapter);


        groupSwipeController = new GroupSwipeController(new GroupSwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                System.out.println("hi");
            }

            @Override
            public void onLeftClicked(int position) {
                adapter.groups.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(groupSwipeController);
        itemTouchHelper.attachToRecyclerView(myrecycler);

        myrecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                groupSwipeController.onDraw(c);
            }
        });
       // presenter.subscribe();
    }


}

