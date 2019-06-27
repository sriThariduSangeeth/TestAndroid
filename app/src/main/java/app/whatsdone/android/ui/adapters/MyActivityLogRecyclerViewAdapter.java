package app.whatsdone.android.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.ui.fragments.ActivityLogFragment.OnListFragmentInteractionListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.UrlUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.Url;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static app.whatsdone.android.utils.SharedPreferencesUtil.get;

public class MyActivityLogRecyclerViewAdapter extends RecyclerView.Adapter<MyActivityLogRecyclerViewAdapter.ViewHolder> {

    private final List<Change> mValues;
    private final OnListFragmentInteractionListener mListener;
    private UrlUtils urlUtils = new UrlUtils();

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
        Date date = mValues.get(position).getDate();
        holder.mItem = mValues.get(position);
      //  holder.mIdView.setText(String.format(Locale.getDefault(), "%d", position + 1));
        holder.mContentView.setText(generateText(mValues.get(position)));
        holder.date.setText(DateFormat.getDateTimeInstance().format(date) );
      //  DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date)
            Picasso.get().load(UrlUtils.getUserImage(mValues.get(position).getByUser())).into(holder.imageView);

        if(!URLUtil.isValidUrl(UrlUtils.getUserImage(mValues.get(position).getByUser())))
            Picasso.get().load(R.drawable.user_group_man_woman3x).into(holder.imageView);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    private String generateText(Change change) {
        return String.format("%s %s %s to %s ",
                change.getByUserName(),
                change.getType(),
                change.getValueFrom(),
                change.getValueTo());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final TextView date;
        final CircleImageView imageView;
        Change mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            date = view.findViewById(R.id.updated_date);
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
            imageView = view.findViewById(R.id.user_image);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
