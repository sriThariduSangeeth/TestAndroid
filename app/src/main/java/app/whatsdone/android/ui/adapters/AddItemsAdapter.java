package app.whatsdone.android.ui.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Task;


public class AddItemsAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Task> itemList;

    public AddItemsAdapter(Context context, ArrayList<Task> itemListArray)
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
        convertView = null;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_task_layout, null);

            TextView textView = (TextView) convertView.findViewById(R.id.checklist__item);
            ImageView imageRemove = (ImageView) convertView.findViewById(R.id.remove_image_view);

            Task myTask = itemList.get(position);
            textView.setText(myTask.getTitle());


            imageRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;


    }
}
