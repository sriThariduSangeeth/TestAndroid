package app.whatsdone.android.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.MyTask;
import app.whatsdone.android.ui.adapters.AddItemsAdapter;

public class CreateNewTaskFragment extends Fragment {
    private List<String> spinnerArray = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private TextView setDueDate, assignFromContacts;
    private DatePicker datePicker;
    private int mYear, mMonth, mDay;
    private ArrayList<MyTask> itemList;
    private AddItemsAdapter itemsAdapter;
    private ListView listView;
    private EditText addNewTask;
    private ImageView imageView;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Button saveButton;


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
       // datePicker = (DatePicker) view.findViewById(R.id.due_date_picker);

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                setDueDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        listView =  view.findViewById(R.id.list_view_checklist);
        addNewTask = view.findViewById(R.id.checklist_add_new_item_edit_text);
        imageView =  view.findViewById(R.id.checklist_add_image_view) ;

        itemList = new ArrayList<>();

        itemsAdapter = new AddItemsAdapter(getContext().getApplicationContext(), itemList);
        //listView.setEmptyView(view.findViewById(R.id.empty));
        listView.setAdapter(itemsAdapter);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValue(v);
            }
        });

        assignFromContacts =  view.findViewById(R.id.assign_from_contacts_text_view);

        assignFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method


                }
            }
        });
//        saveButton = view.findViewById(R.id.save_task_button);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



        return view;

    }

    public void addValue(View v) {
        String name = addNewTask.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getContext().getApplicationContext(), " empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            MyTask md = new MyTask(name);
            itemList.add(md);
            itemsAdapter.notifyDataSetChanged();
            addNewTask.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      switch (requestCode) {
        case (REQUEST_CODE):
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContext().getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String num = "";
                    if (Integer.valueOf(hasNumber) == 1) {
                        System.out.println("Select Contact");
                        Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (numbers.moveToNext()) {

                            //    String name =  numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            String name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            assignFromContacts.setText(name);
                            //String name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            // contact.setText(name);
                            // contactListTextView.setText(num);
                            // Toast.makeText(AddGroupFragment.this, "Number="+num, Toast.LENGTH_LONG).show();

                        }
                    }
                }
                break;
            }
    }

}
}

