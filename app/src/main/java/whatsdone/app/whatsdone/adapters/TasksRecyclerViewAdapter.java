package whatsdone.app.whatsdone.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import whatsdone.app.whatsdone.R;
import whatsdone.app.whatsdone.model.Task;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.RecyclerViewHolderTask> {

    private List<Task> tasks;

    public TasksRecyclerViewAdapter(List<Task> tasks)
    {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public RecyclerViewHolderTask onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.task_recycler_view_layout, viewGroup, false);


        return new RecyclerViewHolderTask(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderTask recyclerViewHolderTask, int i)
    {
        recyclerViewHolderTask.textView.setText(tasks.get(i).getTaskName());

    }



    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //public class
    public class RecyclerViewHolderTask extends RecyclerView.ViewHolder
    {
        public TextView textView;

        public RecyclerViewHolderTask(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.task_text);
        }
    }

}
