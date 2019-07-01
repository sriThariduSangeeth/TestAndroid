package app.whatsdone.android.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

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
        viewHolder.checkBox.setChecked(myTask.isCompleted());

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

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> myTask.setCompleted(isChecked));


        viewHolder.imageRemove.setOnClickListener(v -> {
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
        CheckBox checkBox;
        ImageView imageRemove;
        ViewHolder(@NonNull View convertView) {
            super(convertView);

            textView = convertView.findViewById(R.id.checklist__item);
            imageRemove = convertView.findViewById(R.id.remove_image_view);
            checkBox = convertView.findViewById(R.id.checkbox_task);
        }
    }
}
