package app.whatsdone.android.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        viewHolder.toggleComplete.setChecked(myTask.isCompleted());

        viewHolder.textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        viewHolder.textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextView tmp=(TextView) view;
                myTask.setTitle((tmp.getText().toString()));
                itemList.set(position,myTask);
            }
        });

        viewHolder.toggleComplete.setOnClickListener((buttonView) -> {
            myTask.setCompleted(!myTask.isCompleted());
            itemList.set(position,myTask);

        });



//        viewHolder.toggleComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                myTask.setCompleted(b);
//                itemList.set(position,myTask);
//            }
//        });


        viewHolder.delete.setOnClickListener(v -> {
            itemList.remove(position);
            notifyDataSetChanged();
        });
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
        CheckBox toggleComplete;
        ViewHolder(@NonNull View convertView) {
            super(convertView);
            delete = convertView.findViewById(R.id.remove_image_view);
            toggleComplete = convertView.findViewById(R.id.checkbox_task);
            textView = convertView.findViewById(R.id.checklist__item);
        }
    }
}
