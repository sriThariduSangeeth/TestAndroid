package app.whatsdone.android.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.ui.fragments.ActivityLogFragment.OnListFragmentInteractionListener;

import java.util.List;
import java.util.Locale;

public class MyActivityLogRecyclerViewAdapter extends RecyclerView.Adapter<MyActivityLogRecyclerViewAdapter.ViewHolder> {

    private final List<Change> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyActivityLogRecyclerViewAdapter(List<Change> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activitylog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.format(Locale.getDefault(), "%d", position + 1));
        holder.mContentView.setText(generateText(mValues.get(position)));

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    private String generateText(Change change) {
        return String.format("%s %s %s to %s at %s",
                change.getByUserName(),
                change.getType(),
                change.getValueFrom(),
                change.getValueTo(),
                change.getDate());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        Change mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
