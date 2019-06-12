package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public class RecyclerViewHolderTask extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public TextView groupTextView;


        public RecyclerViewHolderTask(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_text);
            groupTextView = itemView.findViewById(R.id.group_name_text);
        }
    }

    public interface OnMyTaskFragmentInteractionListener {

        void onTaskClicked();

    }

}
