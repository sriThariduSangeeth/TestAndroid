package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.ui.model.TaskInnerGroup;

public class TaskInnerGroupRecyclerViewAdapter extends RecyclerView.Adapter<TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder>{
    private List<TaskInnerGroup> taskList;
    private Context context;

    public TaskInnerGroupRecyclerViewAdapter(List<TaskInnerGroup> tasks, Context context) {

        this.taskList = tasks;
        this.context = context;
    }


    @NonNull
    @Override
    public TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.task_inner_recycler_view_layout, viewGroup, false);

        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder myRecyclerViewHolder, int position) {

        myRecyclerViewHolder.groupTaskText.setText(taskList.get(position).getTaskName());

        myRecyclerViewHolder.groupTaskText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "show task", Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView groupTaskText;


        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            groupTaskText = itemView.findViewById(R.id.task_inner_text);
        }
    }
}
