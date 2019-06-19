package app.whatsdone.android.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.Task;


public class AddItemsAdapter extends BaseAdapter {

    private Context context;
    List<CheckListItem> itemList;

    public AddItemsAdapter(Context context, List<CheckListItem> itemListArray)
    {
        this.context = context;
        this.itemList = itemListArray;
    }
    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_task_layout, null);

            EditText textView = convertView.findViewById(R.id.checklist__item);
            ImageView imageRemove = convertView.findViewById(R.id.remove_image_view);
            CheckBox checkBox = convertView.findViewById(R.id.checkbox_task);

            CheckListItem myTask = itemList.get(position);
            textView.setText(myTask.getTitle());
            checkBox.setChecked(myTask.isCompleted());

            textView.addTextChangedListener(new TextWatcher() {
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

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> myTask.setCompleted(isChecked));


            imageRemove.setOnClickListener(v -> {
                itemList.remove(position);
                notifyDataSetChanged();
            });
        }

        return convertView;


    }
}
