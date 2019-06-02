package app.whatsdone.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CountyListAdapter extends ArrayAdapter <CountryJsonKey>{


    private final Activity context;
    private final List<CountryJsonKey> keyList;


    public CountyListAdapter(Activity context , List<CountryJsonKey> keyList) {
        super(context,R.layout.activity_listview, keyList);

        this.context = context;
        this.keyList = keyList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_listview, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(keyList.get(position).getName());
        subtitleText.setText(keyList.get(position).getDialCode());
        return rowView;
    }
}
