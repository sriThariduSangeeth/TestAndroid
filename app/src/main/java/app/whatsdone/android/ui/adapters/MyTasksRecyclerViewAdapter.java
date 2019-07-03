package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.ui.fragments.MyTaskFragmentListener;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.DateUtil;
import app.whatsdone.android.utils.IconFactory;
import app.whatsdone.android.utils.TextDrawable;
import ru.rambler.libs.swipe_layout.SwipeLayout;
import timber.log.Timber;

import static app.whatsdone.android.model.Task.TaskStatus.DONE;
import static app.whatsdone.android.model.Task.TaskStatus.IN_PROGRESS;
import static app.whatsdone.android.model.Task.TaskStatus.ON_HOLD;
import static app.whatsdone.android.model.Task.TaskStatus.TODO;

public class MyTasksRecyclerViewAdapter extends RecyclerView.Adapter<MyTasksRecyclerViewAdapter.RecyclerViewHolderTask> {

    public List<BaseEntity> tasks;
    private Context context;
    private MyTaskFragmentListener listener;

    public MyTasksRecyclerViewAdapter(List<BaseEntity> tasks , Context context)
    {
        this.tasks = tasks;
        this.context = context;
    }

    public void setListener(MyTaskFragmentListener listener) {
        this.listener = listener;
    }

    // TextView textView = findViewById(R.id.task_text);
    @NonNull
    @Override
    public RecyclerViewHolderTask onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.swipe_layout_my_tasks, viewGroup, false);

        return new RecyclerViewHolderTask(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderTask holder, int i)
    {
        Task task = (Task)tasks.get(i);
        holder.textView.setText(task.getTitle());
        Timber.d("%s is unread: %b", task.getTitle(), task.isUnreadTask());
        if(task.isUnreadTask()){
            holder.textView.setTypeface(holder.textView.getTypeface(), Typeface.BOLD);
        }else {
            holder.textView.setTypeface(holder.textView.getTypeface(), Typeface.NORMAL);
        }


        holder.groupTextView.setText(task.getGroupName());


        TextDrawable icon = IconFactory.getInstance().get(holder.imageView, task);

        if(!task.getAssignedUserImage().isEmpty())
            Picasso.get().load(task.getAssignedUserImage()).placeholder(icon).into(holder.imageView);

        holder.dueDateText.setText(DateUtil.formatted(task.getDueDate(), null));

        holder.frontLayout.setOnClickListener(v -> {
            holder.swipeLayout.animateReset();
            Intent intent = new Intent(context, InnerGroupTaskActivity.class);
            intent.putExtra(Constants.ARG_ACTION, Constants.ACTION_VIEW_TASK);
            intent.putExtra(Constants.ARG_TASK, task);
            context.startActivity(intent);
        });

        if(task.getStatus() == Task.TaskStatus.DONE){
            holder.statusIndicator.setVisibility(View.GONE);
        }else {
            holder.statusIndicator.setVisibility(View.VISIBLE);
            holder.statusIndicator.setText(getStatusIndicatorText(task));
            holder.statusIndicator.setBackgroundColor(context.getResources().getColor(getStatusIndicatorColor(task)));
        }

        setStatusIcons(holder, task);

    }

    private void setStatusIcons(@NonNull RecyclerViewHolderTask holder, Task task) {
        if (task.getStatus() == TODO) {
            holder.swipeLayout.setLeftSwipeEnabled(true);
            holder.swipeLayout.setRightSwipeEnabled(true);
        }

        if (task.getStatus() == ON_HOLD) {
            holder.swipeLayout.setLeftSwipeEnabled(true);
            holder.swipeLayout.setRightSwipeEnabled(true);
            holder.inprogressBtn.setVisibility(View.VISIBLE);
            holder.onholdBtn.setVisibility(View.GONE);
            holder.doneBtn.setVisibility(View.VISIBLE);
        }

        if (task.getStatus() == DONE) {
            holder.swipeLayout.setLeftSwipeEnabled(true);
            holder.swipeLayout.setRightSwipeEnabled(true);
            holder.inprogressBtn.setVisibility(View.GONE);
            holder.onholdBtn.setVisibility(View.GONE);
            holder.doneBtn.setVisibility(View.GONE);
        }

        if (task.getStatus() == Task.TaskStatus.IN_PROGRESS) {
            holder.swipeLayout.setLeftSwipeEnabled(true);
            holder.swipeLayout.setRightSwipeEnabled(true);
            holder.inprogressBtn.setVisibility(View.GONE);
            holder.onholdBtn.setVisibility(View.VISIBLE);
            holder.doneBtn.setVisibility(View.VISIBLE);
        }

        holder.deleteLayout.setOnClickListener(v -> {
            holder.swipeLayout.animateReset();
            if (listener != null) {
                listener.onDelete(task);
            }
        });

        holder.onholdBtn.setOnClickListener(click -> {
            holder.swipeLayout.animateReset();
            if(listener != null)
                listener.onChangeStatus(task, ON_HOLD);
        });

        holder.inprogressBtn.setOnClickListener(click -> {
            holder.swipeLayout.animateReset();
            if(listener != null)
                listener.onChangeStatus(task, IN_PROGRESS);
        });

        holder.doneBtn.setOnClickListener(click -> {
            holder.swipeLayout.animateReset();
            if(listener != null)
                listener.onChangeStatus(task, DONE);
        });
    }

    private int getStatusIndicatorColor(Task task) {
        Date today = DateUtil.getLastMinuteDate(new Date());

        if (DateUtil.isDateEqual(today, task.getDueDate()))
            return R.color.LightSalmonGold;
        else if ((today).after(task.getDueDate()))
            return R.color.lightRed;
        return R.color.LimeGreen;
    }

    private int getStatusIndicatorText(Task task) {
        Date today = DateUtil.getLastMinuteDate(new Date());

        if (DateUtil.isDateEqual(today, task.getDueDate()))
            return R.string.task_due_soon;
        else if ((today).after(task.getDueDate()))
            return R.string.task_overdue;
        return R.string.task_ontrack;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //public class
    class RecyclerViewHolderTask extends RecyclerView.ViewHolder
    {
        TextView textView;
        TextView groupTextView;
        TextView statusIndicator;
        TextView dueDateText;
        ImageView imageView;

        private final SwipeLayout swipeLayout;
        private final View frontLayout;
        private final View deleteLayout;
        private final TextView inprogressBtn, onholdBtn, doneBtn;


        RecyclerViewHolderTask(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_text_my);
            groupTextView = itemView.findViewById(R.id.group_name_text);
            imageView = itemView.findViewById(R.id.image_view_my_task);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            dueDateText = itemView.findViewById(R.id.task_inner_date);

            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            frontLayout = itemView.findViewById(R.id.front_layout);
            deleteLayout = itemView.findViewById(R.id.delete_layout);
            inprogressBtn = itemView.findViewById(R.id.first_status);
            onholdBtn = itemView.findViewById(R.id.second_status);
            doneBtn = itemView.findViewById(R.id.third_status);
        }
    }

}
