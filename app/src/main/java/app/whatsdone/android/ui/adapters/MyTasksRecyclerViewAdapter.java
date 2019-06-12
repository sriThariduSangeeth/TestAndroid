package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;

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
            Picasso.get().load(task.getAssignedUserImage()).into(recyclerViewHolderTask.imageView);

        recyclerViewHolderTask.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener !=null)
                {
                    System.out.println("mytask");
                    listener.onTaskClicked();
                }
            }
        });

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
        ImageView imageView;


        RecyclerViewHolderTask(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_text);
            groupTextView = itemView.findViewById(R.id.group_name_text);
            imageView = itemView.findViewById(R.id.image_view_my_task);
        }
    }

    public interface OnMyTaskFragmentInteractionListener {

        void onTaskClicked();

    }

}
