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

import com.squareup.picasso.Callback;
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
import app.whatsdone.android.utils.ColorGenerator;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.DateUtil;
import app.whatsdone.android.utils.TextDrawable;

import static app.whatsdone.android.model.Task.TaskStatus.DONE;
import static app.whatsdone.android.model.Task.TaskStatus.ON_HOLD;
import static app.whatsdone.android.model.Task.TaskStatus.TODO;

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
///

//



        Task task = (Task) taskList.get(position);
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        myRecyclerViewHolder.groupTaskText.setText(task.getTitle());
       // myRecyclerViewHolder.status.setText(task.getStatus().toString());


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT


        int colorGen = generator.getColor(task.getTitle());
        System.out.println(myRecyclerViewHolder.image.getLayoutParams().height);
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .width(myRecyclerViewHolder.image.getLayoutParams().width)
                .height(myRecyclerViewHolder.image.getLayoutParams().height)
                .endConfig()
                .rect();
        TextDrawable ic1 = builder.build(task.getTitle().substring(0,1), colorGen);

        if(task.getStatus().toString().equals("TODO"))
        myRecyclerViewHolder.status.setText("Not Started");

        if(task.getStatus().toString().equals("ON_HOLD"))
            myRecyclerViewHolder.status.setText("On Hold");

        if(task.getStatus().toString().equals("DONE"))
            myRecyclerViewHolder.status.setText("Done");

        if(task.getStatus().toString().equals("IN_PROGRESS"))
            myRecyclerViewHolder.status.setText("In Progress");


        if (task.getDueDate() == null) {
            myRecyclerViewHolder.date.setText(df.format(new Date()));
        } else {
            myRecyclerViewHolder.date.setText(df.format(task.getDueDate()));
        }

        if (task.getAssignedUserImage() != null && !task.getAssignedUserImage().isEmpty() && URLUtil.isValidUrl(task.getAssignedUserImage())) {
            //myRecyclerViewHolder.image.setImageDrawable(ic1);
            Picasso.get().load(task.getAssignedUserImage()).placeholder(ic1).into(myRecyclerViewHolder.image, getCallBack(myRecyclerViewHolder.image));
        } else {
            myRecyclerViewHolder.image.setImageDrawable(ic1);
          //  Picasso.get().load(R.mipmap.ic_user_default).into(myRecyclerViewHolder.image);
        }

        myRecyclerViewHolder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Fragment myFragment = EditTaskFragment.newInstance(group, task, true);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.task_container, myFragment).addToBackStack(null).commit();
        });
        if(task.getStatus() == Task.TaskStatus.DONE){
            myRecyclerViewHolder.statusIndicator.setVisibility(View.GONE);
        }else {
            myRecyclerViewHolder.statusIndicator.setVisibility(View.VISIBLE);
            myRecyclerViewHolder.statusIndicator.setText(getStatusIndicatorText(task));
            myRecyclerViewHolder.statusIndicator.setBackgroundColor(context.getResources().getColor(getStatusIndicatorColor(task)));
        }


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
}
