package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.utils.DateUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    private List<BaseEntity> groups;
    private Context context;

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
        return new RecyclerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
      //  holder.progressplay.setProgress(0);
        final Group group = (Group) groups.get(position);
        holder.groupNameTextView.setText(group.getGroupName());

        int unreadTasksCount = group.getUnreadTaskCount();
        int unreadDiscussionCount = group.getUnreadDiscussionCount();
        holder.taskCount.setText(String.format(Locale.getDefault(), "%d",unreadTasksCount ));
        holder.discussionCount.setText(String.format(Locale.getDefault(),"%d", unreadDiscussionCount));


        if(unreadTasksCount == 0){
            holder.taskCount.setBackground(context.getResources().getDrawable(R.drawable.shape_circle_inactive));
        }else {
            holder.taskCount.setBackground(context.getResources().getDrawable(R.drawable.shape_circle));
        }

        if(unreadDiscussionCount == 0){
            holder.groupIcon.setImageResource(R.mipmap.ic_chat_inactive);
        }else{
            holder.groupIcon.setImageResource(R.mipmap.ic_chat_active);
        }


        holder.dueDate.setText(DateUtil.formatted(group.getUpdatedDate(), null));
      //  holder.setIsRecyclable(false);
        holder.imageView.setTag(group);
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        //int color1 = generator.getRandomColor();

        int color2 = generator.getColor(group.getGroupName());
        System.out.println(holder.imageView.getLayoutParams().height);
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .width(holder.imageView.getLayoutParams().width)
                .height(holder.imageView.getLayoutParams().height)
                .endConfig()
                .rect();
        TextDrawable ic1 = builder.build(group.getGroupName().substring(0,1), color2);
        try {
            if(!TextUtils.isEmpty(group.getAvatar())) {
                Picasso.get().load(group.getAvatar()).placeholder(ic1).into(holder.imageView,getCallBack(holder.imageView) );
            }else {

                holder.imageView.setImageDrawable(ic1);



              //  holder.imageView.setImageDrawable(holder.imageView.getContext().getResources().getDrawable(R.drawable.user_group_man_woman3x));
            }

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Timber.tag("EXE").e("exception%s", exception.getMessage());
        }

        holder.setGroup(group);

    }

    private Callback getCallBack(final ImageView imageView) {
        return new Callback() {
            @Override
            public void onSuccess() {
                imageView.setAlpha(0f);
                imageView.animate().setDuration(500).alpha(1f).start();
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public Group getGroup(int swipedPosition) {
        return (Group)groups.get(swipedPosition);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public View view;
        private TextView groupNameTextView, taskCount, discussionCount, dueDate;
        private ImageView imageView, groupIcon;
        private Group group;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            groupNameTextView = itemView.findViewById(R.id.group_text);
            taskCount = itemView.findViewById(R.id.unread_tasks_counter);
            discussionCount = itemView.findViewById(R.id.unread_discussion_counter);
            imageView =(CircleImageView) itemView.findViewById(R.id.image_view_group);
            groupIcon = itemView.findViewById(R.id.group_chat_icon);
            dueDate = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(v -> {
                //group.setTeamImage(getImageData(imageView));

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

    private Bitmap getImageData(ImageView imageView){
        //Get the data from an ImageView as bytes
        if (imageView == null) return null;
        Drawable drawable = imageView.getDrawable();

        if(drawable == null) return null;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        return ((BitmapDrawable) drawable).getBitmap();
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
