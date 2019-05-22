package whatsdone.app.whatsdone.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import whatsdone.app.whatsdone.InnerGroupActivity;
import whatsdone.app.whatsdone.R;
import whatsdone.app.whatsdone.model.Group;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    private List<Group> groups;
  //  private ItemClickListener monItemClickListener;
    private Context context;


    public GroupsRecyclerViewAdapter(List<Group> groups, Context context) {
        this.groups = groups;
        this.context = context;
    //    this.monItemClickListener = itemClickListener;

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
        holder.textView.setText(groups.get(position).getGroupName());

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


        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, InnerGroupActivity.class);
                context.startActivity(intent);
            }
        });
        /*
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
        }); */

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView textViewMenu;
      //  private ItemClickListener clickListener;
        private ImageView imageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.group_text);
            imageView = itemView.findViewById(R.id.image_view_group);
            textViewMenu = itemView.findViewById(R.id.group_text_view_menu);
            //this.itemClickListener = itemClickListener;

          //  itemView.setOnLongClickListener(this);
            //itemView.setOnClickListener(this);

        }


    }
}
