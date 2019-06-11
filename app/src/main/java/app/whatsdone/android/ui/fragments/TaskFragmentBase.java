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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.whatsdone.android.R;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.adapters.AddItemsAdapter;
import app.whatsdone.android.utils.GetCurrentDetails;

public abstract class TaskFragmentBase extends Fragment {
    private DatePickerDialog datePickerDialog;
    private TextView setDueDate, assignFromContacts;
    private Toolbar toolbar;
    private int mYear, mMonth, mDay;
    private AddItemsAdapter itemsAdapter;
    private ListView listView;
    private EditText addNewTask , gettitle , getDescript;
    private LinearLayout lay ;
    protected Group group;
    private ImageView imageView;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private DateFormat dateFormat;
    protected String title = "Add Task";
    //data
    protected TaskService service = new TaskServiceImpl();

    Task task = new Task();

    public TaskFragmentBase() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_task, container, false);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        Spinner spinner = view.findViewById(R.id.user_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(Task.TaskStatus.getIndex(task.getStatus()), true);

        gettitle = view.findViewById(R.id.title_edit_text);
        gettitle.setText(task.getTitle());

        getDescript = view.findViewById(R.id.description_edit_text);
        getDescript.setText(task.getDescription());
        lay = view.findViewById(R.id.select_group_view) ;
        listView =  view.findViewById(R.id.list_view_checklist);
        lay.setVisibility(LinearLayout.GONE);
        TextView emptyText = view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);
        setupToolbar();


        setDueDate = view.findViewById(R.id.due_date_text_view);
        setDueDate.setText(dateFormat.format(task.getDueDate()));

        setDueDate.setOnClickListener(v -> {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        // set day of month , month and year value in the edit text
                        String dateValue = dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year;
                        setDueDate.setText(dateValue);
                        try {
                            task.setDueDate(dateFormat.parse(dateValue));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        addNewTask = view.findViewById(R.id.checklist_add_new_item_edit_text);
        imageView =  view.findViewById(R.id.checklist_add_image_view) ;


        itemsAdapter = new AddItemsAdapter(getContext().getApplicationContext(), task.getCheckList());
        listView.setAdapter(itemsAdapter);

        imageView.setOnClickListener(this::addValue);

        assignFromContacts =  view.findViewById(R.id.assign_from_contacts_text_view);
        assignFromContacts.setText(task.getAssignedUserName());

        assignFromContacts.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method


            }
        });

        view.findViewById(R.id.save_task_button_mmm).setOnClickListener(v -> {

            task.setTitle(gettitle.getText().toString());
            task.setDescription(getDescript.getText().toString());
            task.setStatus(Task.TaskStatus.valueOf(returnStatus(spinner.getSelectedItem().toString())));
            task.setUpdatedDate(new Date());
            save();
            getActivity().onBackPressed();

        });
        return view;
    }

    private void setupToolbar() {
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }

    protected abstract void save();

    public String returnStatus(String out){

        switch (out){
            case "To Do":
                return "TODO";
            case "IN PROGRESS":
                return "IN_PROGRESS";
            case "ON HOLD":
                return "ON_HOLD";
            case "DONE":
                return "DONE";
        }

        return "TODO";
    }

    public void addValue(View v) {
        String name = addNewTask.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getContext().getApplicationContext(), " empty",
                    Toast.LENGTH_SHORT).show();
        } else {
            CheckListItem checkListItem = new CheckListItem();
            checkListItem.setTitle(name);
            checkListItem.setCompleted(false);
            task.getCheckList().add(checkListItem);
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
                            Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {

                                String assignee =  numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String assignee_name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                assignFromContacts.setText(assignee_name);
                                task.setAssignedUserName(assignee_name);
                                task.setAssignedUser(assignee);

                            }
                        }
                    }
                    break;
                }
        }

    }
}

