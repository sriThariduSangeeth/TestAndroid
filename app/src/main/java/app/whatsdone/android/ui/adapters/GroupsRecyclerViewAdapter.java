package app.whatsdone.android.ui.adapters;

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

import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragment;
import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    public List<BaseEntity> groups;
  //  private ItemClickListener monItemClickListener;
    private Context context;



    public GroupsRecyclerViewAdapter(List<BaseEntity> groups, Context context) {
        this.groups = groups;
        this.context = context;


    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        View view = layoutInflater.inflate(R.layout.group_recycler_view_layout, viewGroup, false);
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        Group group = (Group) groups.get(position);
        holder.textView.setText(group.getGroupName());
        holder.setGroup(group);

        //popup menu with 3 dots
        /*
        holder.textViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.textViewMenu);
                popup.inflate(R.menu.group_item_options_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId())
                        {
                            case R.id.menu1:
                                return true;
                            case R.id.menu2:
                                return true;
                            case R.id.menu3:
                                return true;
                            default:
                                return false;


                        }


                    }
                });

                popup.show();
            }
        });
*/


    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        private ImageView imageView;
        private Group group;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.group_text);
            imageView = itemView.findViewById(R.id.image_view_group);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                    Fragment myFragment = InnerGroupTaskFragment.newInstance(group);

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.group_container, myFragment).addToBackStack(null).commit();

                }
            });

        }

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }
    }

}
