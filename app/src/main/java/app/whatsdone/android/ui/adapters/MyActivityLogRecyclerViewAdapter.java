package app.whatsdone.android.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Change;
import app.whatsdone.android.ui.fragments.ActivityLogFragment.OnListFragmentInteractionListener;
import app.whatsdone.android.utils.IconFactory;
import app.whatsdone.android.utils.TextDrawable;
import app.whatsdone.android.utils.TextUtil;
import de.hdodenhof.circleimageview.CircleImageView;
public class MyActivityLogRecyclerViewAdapter extends RecyclerView.Adapter<MyActivityLogRecyclerViewAdapter.ViewHolder> {

    private final List<Change> mValues;
    private final OnListFragmentInteractionListener mListener;
    private IconFactory iconFactory = IconFactory.getInstance();

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
        final Change group = mValues.get(position);
        Date date = mValues.get(position).getDate();
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(generateText(mValues.get(position)));
        holder.date.setText(DateFormat.getDateTimeInstance().format(date) );

      holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
        holder.imageView.setTag(mValues.get(position));
        TextDrawable ic1 = iconFactory.get(holder.imageView,group);
        holder.imageView.setImageDrawable(ic1);
    }

    private static String getValueFromText(String val){
        return TextUtil.isNullOrEmpty(val) ? "": "from " + val;
    }

    private String generateText(Change change) {
        String user = change.getByUserName();

        switch (change.getType()){
            case ASSIGNEE_CHANGE:
                return String.format("Changed assignee %s to %s by %s", getValueFromText(change.getValueFrom()),change.getValueTo(), user);
            case CHECKLIST_CHANGE:
               // return String.format("%s %s %s %s","Checklist items count changed from ",change.getValueFrom(),"to ",change.getValueTo());
                return String.format("%s %s %s %s by %s","Checklist items count changed ",getValueFromText(change.getValueFrom()),"to "
                        ,change.getValueTo(), user);
            //return ;
            case CREATED:
                return String.format("%s %s ","Task created by ",change.getByUserName());
            //return "Task created by";
            case DETAIL_CHANGE:
                return String.format("%s %s %s %s by %s","Description changed",getValueFromText(change.getValueFrom()),"to ",change.getValueTo(),user );
            //return "Description changed from";
            case DUE_CHANGE:
                return String.format("%s %s %s %s by %s","Due date changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo(), user);

            //return "Due date changed from";
            case STATUS_CHANGE:
                return String.format("%s %s %s %s by %s","Task status changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo(), user);

            //return "Task status changed from";
            case TITLE_CHANGE:
                return String.format("%s %s %s %s by %s","Title changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo(), user);

            //return "Title changed from";
            case ACKNOWLEGDE_CHANGE:
            return String.format("%s %s %s %s by %s","Acknowledge changed ",getValueFromText(change.getValueFrom()),"to ",change.getValueTo(), user);

            default:

        return String.format("The task property %s was changed from %s to %s by %s",
                change.getType(),
                change.getValueFrom(),
                change.getValueTo(),
                user);

        }


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

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mContentView;
        final TextView date;
        final CircleImageView imageView;


        Change mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            date = view.findViewById(R.id.updated_date);
            mContentView = view.findViewById(R.id.content);
            imageView=view.findViewById(R.id.image);


        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
