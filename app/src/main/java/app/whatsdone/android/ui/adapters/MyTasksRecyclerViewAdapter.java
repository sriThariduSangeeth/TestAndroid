package app.whatsdone.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.ui.activity.InnerGroupTaskActivity;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.DateUtil;

public class MyTasksRecyclerViewAdapter extends RecyclerView.Adapter<MyTasksRecyclerViewAdapter.RecyclerViewHolderTask> {

    public List<BaseEntity> tasks;
    private Context context;
    private OnMyTaskFragmentInteractionListener listener;


    public MyTasksRecyclerViewAdapter(List<BaseEntity> tasks , Context context)
    {
        this.tasks = tasks;
        this.context = context;
    }

    public void setListener(OnMyTaskFragmentInteractionListener listener) {
        this.listener = listener;
    }

    // TextView textView = findViewById(R.id.task_text);
    @NonNull
    @Override
    public RecyclerViewHolderTask onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.my_task_recycler_view_layout, viewGroup, false);


/*

        view.findViewById(R.id.task_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("mytask");
                if(listener !=null)
                {
                    System.out.println("mytask");
                    listener.onTaskClicked();
                }
            }
        });
*/
        return new RecyclerViewHolderTask(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderTask recyclerViewHolderTask, int i)
    {
        Task task = (Task)tasks.get(i);
        recyclerViewHolderTask.textView.setText(task.getTitle());
        recyclerViewHolderTask.groupTextView.setText(task.getGroupName());
        if(!task.getAssignedUserImage().isEmpty())
            Picasso.get().load(task.getAssignedUserImage()).placeholder(R.mipmap.ic_user_default).into(recyclerViewHolderTask.imageView);

        recyclerViewHolderTask.dueDateText.setText(DateUtil.formatted(task.getDueDate()));

        recyclerViewHolderTask.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, InnerGroupTaskActivity.class);
            intent.putExtra(Constants.ARG_ACTION, Constants.ACTION_VIEW_TASK);
            intent.putExtra(Constants.ARG_TASK, task);
            context.startActivity(intent);
        });

        recyclerViewHolderTask.statusIndicator.setText(getStatusIndicatorText(task));
        recyclerViewHolderTask.statusIndicator.setBackgroundColor(context.getResources().getColor(getStatusIndicatorColor(task)));

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


        RecyclerViewHolderTask(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_text);
            groupTextView = itemView.findViewById(R.id.group_name_text);
            imageView = itemView.findViewById(R.id.image_view_my_task);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
            dueDateText = itemView.findViewById(R.id.task_inner_date);
        }
    }

    public interface OnMyTaskFragmentInteractionListener {

        void onTaskClicked(Task task);

    }


}
