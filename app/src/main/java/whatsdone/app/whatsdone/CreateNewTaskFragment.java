package whatsdone.app.whatsdone;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateNewTaskFragment extends Fragment {
    List<String> spinnerArray = new ArrayList<String>();
    private DatePickerDialog datePickerDialog;
    private TextView setDueDate;
    private DatePicker datePicker;
    private int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_new_task, container, false);


        Spinner spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        setDueDate = (TextView) view.findViewById(R.id.due_date_text_view);
        datePicker = (DatePicker) view.findViewById(R.id.due_date_picker);

        return view;

    }

}


/*        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        setDueDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                };
                datePickerDialog.show();
            }
                });


*/
