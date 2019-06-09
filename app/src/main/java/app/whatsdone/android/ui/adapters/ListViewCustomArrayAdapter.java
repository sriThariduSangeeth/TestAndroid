package app.whatsdone.android.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.whatsdone.android.R;

public class ListViewCustomArrayAdapter extends ArrayAdapter<String> {
    protected LayoutInflater inflater;
    protected int layout;
    private List<String> contacts;

    public ListViewCustomArrayAdapter( @NonNull Context context, int resourceId, @NonNull List<String> contacts) {
        super(context, resourceId, contacts);
        this.contacts = contacts;
        layout = resourceId;
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
//    public ListViewCustomArrayAdapter(@NonNull Activity activity, int resourceId, @NonNull List<String> objects) {
//        super(activity, resourceId, objects);
//
//    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(layout,parent,false);
        TextView textView = (TextView)view.findViewById((R.id.item_label));
        textView.setText(contacts.get(position));

        return view;

    }
}
