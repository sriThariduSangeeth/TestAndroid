package whatsdone.app.whatsdone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import whatsdone.app.whatsdone.Group_fragment_0;
import whatsdone.app.whatsdone.R;
import whatsdone.app.whatsdone.model.Group;
import whatsdone.app.whatsdone.presenter.ItemClickListener;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    private List<Group> groups;
    private ItemClickListener monItemClickListener;



    public GroupsRecyclerViewAdapter(List<Group> groups, ItemClickListener itemClickListener) {
        this.groups = groups;
        this.monItemClickListener = itemClickListener;

    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.group_recycler_view_layout, viewGroup, false);

        return new RecyclerViewHolder(view, monItemClickListener);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textView.setText(groups.get(position).getGroupName());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick)
                {
                    System.out.println("Long clicked");
                }
                else
                {
                    System.out.println("clicked");
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new Group_fragment_0();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_groups, myFragment).addToBackStack(null).commit();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView textView;
        private ItemClickListener clickListener;
        private ImageView imageView;

        public RecyclerViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            textView = itemView.findViewById(R.id.group_text);
            imageView = itemView.findViewById(R.id.image_view_group);
            //this.itemClickListener = itemClickListener;

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener)
        {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(),true);
            return true;
        }
    }
}
