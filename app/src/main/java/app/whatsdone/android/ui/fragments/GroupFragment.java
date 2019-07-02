package app.whatsdone.android.ui.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import app.whatsdone.android.utils.LocalState;
import timber.log.Timber;


public class GroupFragment extends Fragment implements GroupFragmentView {

    private List<BaseEntity> groups = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager;
    private GroupPresenter presenter;
    private OnGroupFragmentInteractionListener listener;
    private GroupSwipeController groupSwipeController;
    private RecyclerView myrecycler;
    private GroupsRecyclerViewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new GroupPresenterImpl();
        this.presenter.init(this);
        this.presenter.subscribe();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_, container, false);

        myrecycler = view.findViewById(R.id.group_recycler_view);




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
        if(this.adapter != null) {
            this.groups.clear();
            LocalState.getInstance().syncGroups(groups);
            this.groups.addAll(groups);
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onGroupDeleted() {

    }

    @Override
    public void onDeleteError() {

    }

    @Override
    public void onGroupLeave() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalState.getInstance().syncGroups(groups);
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {

        layoutManager = new LinearLayoutManager(getContext());
        myrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupsRecyclerViewAdapter(groups, getContext());
        myrecycler.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(myrecycler.getContext(),
                DividerItemDecoration.VERTICAL);
        myrecycler.addItemDecoration(dividerItemDecoration);
//        adapter.setHasStableIds(true);
       // myrecycler.setItemViewCacheSize(10);


        groupSwipeController = new GroupSwipeController(new GroupSwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

            }

            @Override
            public void onLeftClicked(int position) {
                try {
                    Group group = adapter.getGroup(position);
                    String id = group.getDocumentID();
                    //groups.remove(position);
                    //adapter.notifyDataSetChanged();
                    if (group.getCreatedBy().equals(AuthServiceImpl.getCurrentUser().getDocumentID())) {
                        presenter.deleteTeam(id);

                    } else {
                        presenter.leaveTeam(id);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}

