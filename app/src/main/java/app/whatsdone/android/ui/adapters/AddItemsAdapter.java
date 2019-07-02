package app.whatsdone.android.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.CheckListItem;


public class AddItemsAdapter extends RecyclerView.Adapter<AddItemsAdapter.ViewHolder> {

    private Context context;
    private List<CheckListItem> itemList;

    public AddItemsAdapter(Context context, List<CheckListItem> itemListArray)
    {
        this.context = context;
        this.itemList = itemListArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.list_view_task_layout, viewGroup, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        CheckListItem myTask = itemList.get(position);
        viewHolder.textView.setText(myTask.getTitle());
        changedColors(viewHolder, myTask);

        viewHolder.textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myTask.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.toggleComplete.setOnClickListener((buttonView) -> {
            myTask.setCompleted(!myTask.isCompleted());
            changedColors(viewHolder, myTask);
        });


        viewHolder.delete.setOnClickListener(v -> {
            itemList.remove(position);
            notifyDataSetChanged();
        });
    }

    private void changedColors(@NonNull ViewHolder viewHolder, CheckListItem myTask) {
        if(myTask.isCompleted()) {
            viewHolder.first_status.setBackgroundColor(viewHolder.first_status.getContext().getResources().getColor(R.color.LimeGreen));
            viewHolder.textView.setPaintFlags(viewHolder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            viewHolder.first_status.setBackgroundColor(viewHolder.first_status.getContext().getResources().getColor(R.color.CornflowerBlue));
            viewHolder.textView.setPaintFlags(viewHolder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        EditText textView;
        View delete;
        View toggleComplete;
        TextView first_status;
        ViewHolder(@NonNull View convertView) {
            super(convertView);
            delete = convertView.findViewById(R.id.delete_layout);
            toggleComplete = convertView.findViewById(R.id.status_layout);
            textView = convertView.findViewById(R.id.checklist__item);
            first_status = convertView.findViewById(R.id.first_status);
        }
    }
}
