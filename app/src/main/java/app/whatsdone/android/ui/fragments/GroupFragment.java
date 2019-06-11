package app.whatsdone.android.ui.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.adapters.GroupSwipeController;
import app.whatsdone.android.ui.adapters.GroupSwipeControllerActions;
import app.whatsdone.android.ui.adapters.GroupsRecyclerViewAdapter;
import app.whatsdone.android.ui.presenter.GroupPresenter;
import app.whatsdone.android.ui.presenter.GroupPresenterImpl;
import app.whatsdone.android.ui.view.GroupFragmentView;
import timber.log.Timber;


public class GroupFragment extends Fragment implements GroupFragmentView {

    private List<BaseEntity> groups = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager;
    private GroupPresenter presenter;
    private OnGroupFragmentInteractionListener listener;
    private GroupSwipeController groupSwipeController;
    private RecyclerView myrecycler;
    private GroupsRecyclerViewAdapter mAdapter;


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
        this.presenter.init(this);


        view.findViewById(R.id.fab_add_group).setOnClickListener(v -> {

            if (listener != null) {
                listener.onAddClicked();
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


    @Override
    public void onGroupDeleted() {

    }

    @Override
    public void onDeleteError() {

    }

    @Override
    public void onGroupLeave() {
        mAdapter.notifyDataSetChanged();

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

    private void setupRecyclerView() {

        layoutManager = new LinearLayoutManager(getContext());
        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GroupsRecyclerViewAdapter(groups, getContext());
        myrecycler.setAdapter(mAdapter);


        groupSwipeController = new GroupSwipeController(new GroupSwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

            }

            @Override
            public void onLeftClicked(int position) {
                try {
                    Group group = adapter.getGroup(position);
                    if (group.getCreatedBy().equals(AuthServiceImpl.getCurrentUser().getDocumentID())) {


                        presenter.deleteTeam(groups.get(position).getDocumentID());
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position, groups.size());
                        adapter.notifyDataSetChanged();
                    } else {
                        presenter.leaveTeam(group.getDocumentID());
                        mAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    Timber.e(e);
                }

            }
        }, myrecycler);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(groupSwipeController);
        itemTouchHelper.attachToRecyclerView(myrecycler);

        myrecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                groupSwipeController.onDraw(c);
            }


        });
        presenter.subscribe();
    }


}

