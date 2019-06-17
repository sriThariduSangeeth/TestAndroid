package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.ui.fragments.InnerGroupTaskFragment;
import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.utils.DateUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.support.constraint.Constraints.TAG;
import static com.github.ivbaranov.mli.MaterialLetterIcon.Shape.CIRCLE;

public class GroupsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewAdapter.RecyclerViewHolder> {
    public List<BaseEntity> groups;
    private Context context;
    private TextView groupNameTextView, taskCount, discussionCount, toolbarTextView;;
    private RecyclerView.LayoutManager linearLayoutManager;
   // private GroupsRecyclerViewAdapter groupsRecyclerViewAdapter;


    private RecyclerView.Adapter adapter;

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
        try {
            if(!TextUtils.isEmpty(group.getAvatar())) {
                holder.imageViewIcon.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                Picasso.get().load(group.getAvatar()).placeholder(R.drawable.user_group_man_woman3x).into(holder.imageView,getCallBack(holder.imageView) );
            }else {
                holder.imageViewIcon.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.imageViewIcon
                        .setShapeColor(context.getResources().getColor(R.color.default_icon_color));
                holder.imageViewIcon.setShapeType(CIRCLE);
                holder.imageViewIcon.setLetter(group.getGroupName().substring(0,1));
                holder.imageViewIcon.setLetterColor(context.getResources().getColor(R.color.white));
                holder.imageViewIcon.setLetterSize(20);



              //  holder.imageView.setImageDrawable(holder.imageView.getContext().getResources().getDrawable(R.drawable.user_group_man_woman3x));
            }

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Timber.tag("EXE").e("exception%s", exception.getMessage());
        }

        holder.setGroup(group);

    }

    public Callback getCallBack(final ImageView imageView) {
        return new Callback() {
            @Override
            public void onSuccess() {
                ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .75f, ScaleAnimation.RELATIVE_TO_SELF, .75f);
                scale.setDuration(400);
                imageView.startAnimation(scale);
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

        private RecyclerView groupRecyclerView;
        public View view;


        private TextView groupNameTextView, taskCount, discussionCount, dueDate;
        private ImageView imageView, groupIcon;
        private MaterialLetterIcon imageViewIcon;
        private Group group;



        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            groupNameTextView = itemView.findViewById(R.id.group_text);
            taskCount = itemView.findViewById(R.id.unread_tasks_counter);
            discussionCount = itemView.findViewById(R.id.unread_discussion_counter);
            groupRecyclerView = itemView.findViewById(R.id.group_recycler_view);
            imageView =(CircleImageView) itemView.findViewById(R.id.image_view_group);
            imageViewIcon = itemView.findViewById(R.id.image_view_group_icon);
            groupIcon = itemView.findViewById(R.id.group_chat_icon);
            dueDate = itemView.findViewById(R.id.date);
            if(imageView.getDrawable() == null)
            {
//                MaterialLetterIcon icon = new MaterialLetterIcon.Builder(context) //
//                        .shapeColor(context.getResources().getColor(R.color.colorAccent))
//                        .shapeType(CIRCLE)
//                        .letter("S")
//                        .letterColor(context.getResources().getColor(R.color.white))
//                        .letterSize(26)
//                        .lettersNumber(1)
//
//                        .initials(false)
//                        .initialsNumber(2)
//                        .create();
                imageViewIcon
                        .setShapeColor(context.getResources().getColor(R.color.colorAccent));
                imageViewIcon.setShapeType(CIRCLE);
                imageViewIcon.setLetter("G");
                imageViewIcon.setLetterColor(context.getResources().getColor(R.color.white));
                imageViewIcon.setLetterSize(26);
                imageViewIcon.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                Drawable defaultImage= itemView.getResources().getDrawable(R.drawable.user_group_man_woman3x);
            }


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
