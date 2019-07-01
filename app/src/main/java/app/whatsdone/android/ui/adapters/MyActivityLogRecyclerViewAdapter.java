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
      holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
        holder.imageView.setImageResource(generateImageResource(mValues.get(position)));
    }

    private static String getValueFromText(String val){
        return val.equals("")||val==null?"":"from "+val;
    }

    private String generateText(Change change) {

        switch (change.getType()){
            case ASSIGNEE_CHANGE:
                return String.format("%s %s %s %s","Changed assignee ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());
            case CHECKLIST_CHANGE:
               // return String.format("%s %s %s %s","Checklist items count changed from ",change.getValueFrom(),"to ",change.getValueTo());
                return String.format("%s %s %s %s","Checklist items count changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());
            //return ;
            case CREATED:
                return String.format("%s %s ","Task created by ",change.getByUserName());
            //return "Task created by";
            case DETAIL_CHANGE:
                return String.format("%s %s %s %s","Description changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());
            //return "Description changed from";
            case DUE_CHANGE:
                return String.format("%s %s %s %s","Due date changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());

            //return "Due date changed from";
            case STATUS_CHANGE:
                return String.format("%s %s %s %s","Task status changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());

            //return "Task status changed from";
            case TITLE_CHANGE:
                return String.format("%s %s %s %s","Title changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());

            //return "Title changed from";
            case ACKNOWLEGDE_CHANGE:
            return String.format("%s %s %s %s","Acknoledge changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo());

            default:
                return "";

        }







//        return String.format("%s %s %s to %s ",
//                change.getByUserName(),
//                change.getType(),
//                change.getValueFrom(),
//                change.getValueTo());
    }

    private int generateImageResource(Change change) {

        switch (change.getType()){
            case ASSIGNEE_CHANGE:
                return R.drawable.discussion;
            case CHECKLIST_CHANGE:
                return R.drawable.discussion;
            case CREATED:
                return R.drawable.chat_icon;
            case DETAIL_CHANGE:
                return R.drawable.discussion;
            case DUE_CHANGE:
                return R.drawable.chat_icon;
            case STATUS_CHANGE:
                return R.drawable.chat_icon;
            case TITLE_CHANGE:
                return R.drawable.chat_icon;
            case ACKNOWLEGDE_CHANGE:
                return R.drawable.chat_icon;
            default:
                return R.drawable.chat_icon;

        }







//        return String.format("%s %s %s to %s ",
//                change.getByUserName(),
//                change.getType(),
//                change.getValueFrom(),
//                change.getValueTo());
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

        Change mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            date = view.findViewById(R.id.updated_date);
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
