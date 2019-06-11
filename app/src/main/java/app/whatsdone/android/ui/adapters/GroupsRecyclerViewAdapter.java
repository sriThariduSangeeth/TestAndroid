package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragment;
import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.support.constraint.Constraints.TAG;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    public List<BaseEntity> groups;
    private Context context;
    private TextView groupNameTextView, taskCount, discussionCount, toolbarTextView;;
    private CircleImageView imageView;



    public GroupsRecyclerViewAdapter(List<BaseEntity> groups, Context context) {
        this.groups = groups;
        this.context = context;
        setHasStableIds(true);


    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.group_recycler_view_layout, viewGroup, false);

        imageView = view.findViewById(R.id.image_view_group);
       // toolbarTextView = view.findViewById(R.id.toolbar_title);



        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
      //  holder.progressplay.setProgress(0);
        Group group = (Group) groups.get(position);
        holder.groupNameTextView.setText(group.getGroupName());
        holder.taskCount.setText(group.getDiscussionCount()+"");
        holder.discussionCount.setText(group. getDiscussionCount()+"");


        try {
            if(!TextUtils.isEmpty(group.getAvatar())) {
                Picasso.get().load(group.getAvatar()).into(imageView);
                Timber.tag(TAG).d("onBindViewHolder: ");
                System.out.println(" Avatar " + group.getAvatar());
            }

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Timber.tag("EXE").e("exception%s", exception.getMessage());
        }

        holder.setGroup(group);

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public Group getGroup(int swipedPosition) {
        return (Group)groups.get(swipedPosition);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView groupNameTextView, taskCount, discussionCount;
        private ImageView imageView;
        private Group group;



        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            groupNameTextView = itemView.findViewById(R.id.group_text);
            taskCount = itemView.findViewById(R.id.unread_tasks_counter);
            discussionCount = itemView.findViewById(R.id.unread_discussion_counter);




            itemView.setOnClickListener(v -> {
                group.setTeamImage(getImageData(imageView));

                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                Intent intent = new Intent(activity, InnerGroupTaskActivity.class);
                intent.putExtra("group", group);
                activity.startActivity(intent);


            });

        }

        public Group getGroup() {
            return group;
        }

        public void setGroup(Group group) {
            this.group = group;
        }
    }

    public Bitmap getImageData(ImageView imageView){
        //Get the data from an ImageView as bytes
        if (imageView == null) return null;
        Drawable drawable = imageView.getDrawable();

        if(drawable == null) return null;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return  bitmap;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }
}
