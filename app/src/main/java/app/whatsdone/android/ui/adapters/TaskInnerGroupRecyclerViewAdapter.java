package app.whatsdone.android.ui.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import app.whatsdone.android.R;
import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.model.TaskInnerGroup;
import app.whatsdone.android.tasks.DownloadImageFromInternet;
import app.whatsdone.android.utils.GetCurrentDetails;

public class TaskInnerGroupRecyclerViewAdapter extends RecyclerView.Adapter<TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder>{
    public List<BaseEntity> taskList;
    private Context context;
    public TaskInnerGroupRecyclerViewAdapter(List<BaseEntity> tasks, Context context) {

        this.taskList = tasks;
        this.context = context;
    }


    @NonNull
    @Override
    public TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.task_inner_recycler_view_layout, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "show task", Toast.LENGTH_SHORT).show();
            }
        });
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskInnerGroupRecyclerViewAdapter.MyRecyclerViewHolder myRecyclerViewHolder, int position) {

        Task task = (Task) taskList.get(position);
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        myRecyclerViewHolder.groupTaskText.setText(task.getTitle());
        myRecyclerViewHolder.status.setText(task.getStatus().toString());
        if(task.getDueDate()== null){
            myRecyclerViewHolder.date.setText(df.format(new GetCurrentDetails().getCurrentDateTime()));
        }else {
            myRecyclerViewHolder.date.setText(df.format(task.getDueDate()));
        }


        if(task.getAssignedUserImage() != null && !task.getAssignedUserImage().isEmpty() && URLUtil.isValidUrl(task.getAssignedUserImage())){
            Picasso.get().load(task.getAssignedUserImage()).into(myRecyclerViewHolder.imag);
        }else {
            myRecyclerViewHolder.imag.setImageResource(R.drawable.ic_account_circle);
        }



//        myRecyclerViewHolder.groupTaskText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "show task", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView groupTaskText , date , status;
        private ImageView imag ;


        public MyRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            groupTaskText = itemView.findViewById(R.id.task_inner_text);
            imag = itemView.findViewById(R.id.image_view_task_inner_group);
            date = itemView.findViewById(R.id.task_inner_date);
            status = itemView.findViewById(R.id.status_inner_task);
        }
    }
}
