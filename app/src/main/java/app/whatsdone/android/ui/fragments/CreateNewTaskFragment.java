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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.adapters.AddItemsAdapter;
import app.whatsdone.android.utils.GetCurrentDetails;

public class CreateNewTaskFragment extends Fragment {
    private List<String> spinnerArray = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private TextView setDueDate, assignFromContacts;
    private DatePicker datePicker;
    private Toolbar toolbar;
    private int mYear, mMonth, mDay;
    private ArrayList<Task> itemList;
    private AddItemsAdapter itemsAdapter;
    private ListView listView;
    private EditText addNewTask , gettitle , getDescript;
    private LinearLayout lay ;
    private Group group;
    private ImageView imageView;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Button saveButton;
    private Date date;
    private DateFormat dateFormat;

    //data
    private String assingnee = null;
    private String assingnee_name = null;
    private String creator = null;
    private Date due_date;
    private String status = null;
    private String title = null ;
    private String description = null;
    private ArrayList<CheckListItem> checkList;
    private TaskService service = new TaskServiceImpl();

    public static CreateNewTaskFragment newInstance(Group group){

        CreateNewTaskFragment instance = new CreateNewTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        Group group = arg.getParcelable("group");
        this.group = group;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_task, container, false);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        checkList = new ArrayList<>();
        Spinner spinner = view.findViewById(R.id.user_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        creator = new GetCurrentDetails().getCurrentUser().getDocumentID();
        gettitle = view.findViewById(R.id.title_edit_text);
        getDescript = view.findViewById(R.id.description_edit_text);
        lay = view.findViewById(R.id.select_group_view) ;
        listView =  view.findViewById(R.id.list_view_checklist);
        lay.setVisibility(LinearLayout.GONE);
        TextView emptyText = (TextView)view.findViewById(R.id.empty);
        listView.setEmptyView(emptyText);
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Task");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        setDueDate = (TextView) view.findViewById(R.id.due_date_text_view);

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
                                String dateval = dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year;
                                setDueDate.setText(dateval);
                                try {
                                    date = dateFormat.parse(dateval);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        addNewTask = view.findViewById(R.id.checklist_add_new_item_edit_text);
        imageView =  view.findViewById(R.id.checklist_add_image_view) ;


        itemsAdapter = new AddItemsAdapter(getContext().getApplicationContext(), checkList);
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

        view.findViewById(R.id.save_task_button_mmm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setTitle(String.valueOf(gettitle.getText().toString()));
                task.setDescription(getDescript.getText().toString());
                task.setDueDate(date);
                task.setGroupId(group.getDocumentID());
                task.setGroupName(group.getGroupName());
                task.setAssignedUserName(assingnee_name);
                task.setAssignedUser(assingnee);
                task.setAssignedBy(new GetCurrentDetails().getCurrentUser().getDocumentID());
                task.setCreatedBy(new GetCurrentDetails().getCurrentUser().getDocumentID());
                task.setStatus(Task.TaskStatus.valueOf(returnStatus(spinner.getSelectedItem().toString())));
                task.setUpdatedDate(new GetCurrentDetails().getCurrentDateTime());
                task.setCheckList(checkList);

                service.create(task, new ServiceListener() {
                    @Override
                    public void onSuccess() {
                        getActivity().onBackPressed();
                    }
                });

            }
        });


        return view;

    }

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
            checkList.add(checkListItem);
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

                            assingnee =  numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));


                            assingnee_name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            assignFromContacts.setText(assingnee_name);

                        }
                    }
                }
                break;
            }
    }

}
}

