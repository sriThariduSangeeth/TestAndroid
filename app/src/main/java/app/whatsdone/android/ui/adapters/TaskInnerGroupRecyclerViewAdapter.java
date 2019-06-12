package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.fragments.EditTaskFragment;
import app.whatsdone.android.utils.DateUtil;
import app.whatsdone.android.utils.GetCurrentDetails;

public class TaskInnerGroupRecyclerViewAdapter extends RecyclerView.Adapter<TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder> {
    public List<BaseEntity> taskList;
    private Context context;
    private Group group;

    public TaskInnerGroupRecyclerViewAdapter(List<BaseEntity> tasks, Context context, Group group) {

        this.taskList = tasks;
        this.context = context;
        this.group = group;
    }

    @NonNull
    @Override
    public TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.task_inner_recycler_view_layout, viewGroup, false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder myRecyclerViewHolder, int position) {

        Task task = (Task) taskList.get(position);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        myRecyclerViewHolder.groupTaskText.setText(task.getTitle());
        myRecyclerViewHolder.status.setText(task.getStatus().toString());
        if (task.getDueDate() == null) {
            myRecyclerViewHolder.date.setText(df.format(new GetCurrentDetails().getCurrentDateTime()));
        } else {
            myRecyclerViewHolder.date.setText(df.format(task.getDueDate()));
        }

        if (task.getAssignedUserImage() != null && !task.getAssignedUserImage().isEmpty() && URLUtil.isValidUrl(task.getAssignedUserImage())) {
            Picasso.get().load(task.getAssignedUserImage()).placeholder(R.mipmap.ic_user_default).into(myRecyclerViewHolder.image);
        } else {
            Picasso.get().load(R.mipmap.ic_user_default).into(myRecyclerViewHolder.image);
        }

        myRecyclerViewHolder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment myFragment = EditTaskFragment.newInstance(group, task, true);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();
        });
        myRecyclerViewHolder.statusIndicator.setText(getStatusIndicatorText(task));
        myRecyclerViewHolder.statusIndicator.setBackgroundColor(context.getResources().getColor(getStatusIndicatorColor(task)));
    }

    private int getStatusIndicatorColor(Task task) {
        Date today = DateUtil.getLastMinuteDate(new Date());

        if (DateUtil.isEqual(today, task.getDueDate()))
            return R.color.LightSalmonGold;
        else if ((today).after(task.getDueDate()))
            return R.color.lightRed;
        return R.color.LimeGreen;
    }

    private int getStatusIndicatorText(Task task) {
        Date today = DateUtil.getLastMinuteDate(new Date());

        if (DateUtil.isEqual(today, task.getDueDate()))
            return R.string.task_due_soon;
        else if ((today).after(task.getDueDate()))
            return R.string.task_overdue;
        return R.string.task_ontrack;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView groupTaskText, date, status;
        private ImageView image;
        TextView statusIndicator;

        MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTaskText = itemView.findViewById(R.id.task_inner_text);
            image = itemView.findViewById(R.id.image_view_task_inner_group);
            date = itemView.findViewById(R.id.task_inner_date);
            status = itemView.findViewById(R.id.status_inner_task);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
        }
    }
}
