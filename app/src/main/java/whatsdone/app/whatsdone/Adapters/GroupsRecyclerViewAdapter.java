package whatsdone.app.whatsdone.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.R;
import whatsdone.app.whatsdone.model.Group;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.recyclerViewHolder>
{
    private List<Group> groups;

    // private OnGroupListner onGroupListner;

    public GroupsRecyclerViewAdapter(List<Group> groups)
    {
        this.groups = groups;
        //this.onGroupListner = onGroupListner;
    }


    @NonNull
    @Override
    public recyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_layout, viewGroup, false);

        return new recyclerViewHolder(view);
        //return new recyclerViewHolder(view, onGroupListner);
    }

    @Override
    public void onBindViewHolder(recyclerViewHolder holder, int position) {
        holder.textView.setText(groups.get(position).getGroupName());
   //     holder.timestamp.setText(groups.get(position).getTimeStamp());
       // holder.imageView.setImageResource();
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class recyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        private ImageView imageView;
     //   OnGroupListner onGroupListner;

        public recyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_group);
            imageView = itemView.findViewById(R.id.image_view);

       //     this.onGroupListner = onGroupListner;

         //   timeStamp = itemView.findViewById(R.id.note_timestamp);
          //  itemView.setOnClickListener(this);


            //onclick
        }


    }


}
