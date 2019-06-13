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
import app.whatsdone.android.model.Contact;

public class ListViewCustomArrayAdapter extends ArrayAdapter<Contact> {
    protected LayoutInflater inflater;
    protected int layout;
    private List<Contact> contacts;

    public ListViewCustomArrayAdapter( @NonNull Context context, int resourceId, @NonNull List<Contact> contacts) {
        super(context, resourceId, contacts);
        this.contacts = contacts;
        layout = resourceId;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_list_layout, parent, false);
        }

        TextView textView = convertView.findViewById((R.id.item_label));
        textView.setText(contacts.get(position).getDisplayName());
        TextView numberView = convertView.findViewById(R.id.item_number);
        numberView.setText(contacts.get(position).getPhoneNumber());

        return convertView;

    }
}