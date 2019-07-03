package app.whatsdone.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LogEvent;
import app.whatsdone.android.services.LogService;
import app.whatsdone.android.services.LogServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.adapters.MyActivityLogRecyclerViewAdapter;
import app.whatsdone.android.utils.SortUtil;
import timber.log.Timber;

import static app.whatsdone.android.utils.Constants.ARG_GROUP;
import static app.whatsdone.android.utils.SortUtil.sortChanges;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ActivityLogFragment extends Fragment implements ServiceListener {

    private static final String ARG_TASK_ID = "task_id";
    private String taskId = "";
    private OnListFragmentInteractionListener mListener;
    private List<Change> changes = new ArrayList<>();
    private LogService service = new LogServiceImpl();
    private MyActivityLogRecyclerViewAdapter adapter;
    private Group group;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActivityLogFragment() {
    }

    @SuppressWarnings("unused")
    public static ActivityLogFragment newInstance(String taskId, Group group) {
        ActivityLogFragment fragment = new ActivityLogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TASK_ID, taskId);
        args.putParcelable(ARG_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            taskId = getArguments().getString(ARG_TASK_ID);
            group = getArguments().getParcelable(ARG_GROUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activitylog_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            this.adapter = new MyActivityLogRecyclerViewAdapter(changes, mListener);
            recyclerView.setAdapter(this.adapter);
        }
        setupToolbar();

        service.getByTaskId(taskId, group, this);

        return view;
    }

    private void setupToolbar() {
        this.setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_task_title);
        toolbarTitle.setText(R.string.activity_log);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbarTitle.setClickable(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Change item);
    }

    @Override
    public void onDataReceived(BaseEntity entity) {

        LogEvent event = (LogEvent)entity;
        Timber.d("%s",event.getLogs().size());
        this.changes.addAll(sortChanges(event.getLogs()));
        this.adapter.notifyDataSetChanged();
    }
}
