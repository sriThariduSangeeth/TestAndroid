package whatsdone.app.whatsdone.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import whatsdone.app.whatsdone.R;
import whatsdone.app.whatsdone.model.Group;
import whatsdone.app.whatsdone.presenter.OnItemClick;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    private List<Group> groups;
    private OnItemClick monItemClick;



    public GroupsRecyclerViewAdapter(List<Group> groups, OnItemClick onItemClick) {
        this.groups = groups;
        this.monItemClick = onItemClick;

    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.group_recycler_view_layout, viewGroup, false);

        return new RecyclerViewHolder(view, monItemClick);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textView.setText(groups.get(position).getGroupName());

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;

        OnItemClick onItemClick;

        public RecyclerViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);

            textView = itemView.findViewById(R.id.group_text);

            this.onItemClick = onItemClick;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(getAdapterPosition());
        }
    }
}
