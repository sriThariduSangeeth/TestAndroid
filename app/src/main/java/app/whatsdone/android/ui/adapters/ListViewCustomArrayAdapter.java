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
import app.whatsdone.android.utils.ContactUtil;

public class ListViewCustomArrayAdapter extends ArrayAdapter<String> {
    protected LayoutInflater inflater;
    protected int layout;
    private List<String> numbers;
    private List<Contact> contacts;

    public ListViewCustomArrayAdapter( @NonNull Context context, int resourceId,List<String> numbers,  @NonNull List<Contact> contacts) {
        super(context, resourceId, numbers);
        this.numbers = numbers;
        this.contacts = contacts;
        layout = resourceId;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(layout,parent,false);
        TextView textView = (TextView)view.findViewById((R.id.item_label));
        String phoneNumber = this.numbers.get(position);
        textView.setText(ContactUtil.getDisplayNameOrNumber(contacts, phoneNumber));

        return view;

    }
}
