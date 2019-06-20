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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import app.whatsdone.android.R;
import app.whatsdone.android.model.CheckListItem;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.ContactService;
import app.whatsdone.android.services.ContactServiceImpl;
import app.whatsdone.android.services.GroupService;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.adapters.AddItemsAdapter;
import app.whatsdone.android.utils.AlertUtil;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.GetCurrentDetails;
import app.whatsdone.android.utils.LocalState;
import app.whatsdone.android.utils.TextUtil;
import app.whatsdone.android.utils.UrlUtils;
import timber.log.Timber;

public abstract class TaskFragmentBase extends Fragment implements ContactPickerListDialogFragment.Listener {
    protected boolean isFromMyTasks;
    protected boolean isPersonalTask;
    private DatePickerDialog datePickerDialog;
    private TextView setDueDate, assignFromContacts, assignedBy;
    private Toolbar toolbar;
    private int mYear, mMonth, mDay;
    private AddItemsAdapter itemsAdapter;
    private ListView listView;
    private EditText gettitle, getDescript;
    private Button addChecklistBtn;
    private ConstraintLayout lay;
    protected Group group;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private DateFormat dateFormat;
    protected String title = "Add Task";
    //data
    protected TaskService service = new TaskServiceImpl();
    protected GroupService groupService = new GroupServiceImpl();
    protected ContactService contactService = new ContactServiceImpl();


    Task task = new Task();
    protected TextView toolbarTitle;

    public TaskFragmentBase() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_task, container, false);

        dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());


        Spinner spinner = view.findViewById(R.id.user_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(Task.TaskStatus.getIndex(task.getStatus()), true);

        gettitle = view.findViewById(R.id.title_edit_text);
        gettitle.setText(task.getTitle());
        gettitle.setHintTextColor(getResources().getColor(R.color.gray));

        assignedBy = view.findViewById(R.id.assigned_by_text);
        assignedBy.setText(ContactUtil.getInstance().resolveContact(task.getAssignedBy()).getDisplayName());

        getDescript = view.findViewById(R.id.description_edit_text);
        getDescript.setText(task.getDescription());
        getDescript.setHintTextColor(getResources().getColor(R.color.gray));
        lay = view.findViewById(R.id.select_group_view);
        listView = view.findViewById(R.id.list_view_checklist);

        lay.setVisibility(LinearLayout.GONE);
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
                        String dateValue = String.format(Locale.getDefault(), "%02d/%02d/%d", monthOfYear + 1, dayOfMonth, year);
                        setDueDate.setText(dateValue);
                        try {
                            Date date = dateFormat.parse(dateValue);
                            task.setDueDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        addChecklistBtn = view.findViewById(R.id.add_check_list);


        itemsAdapter = new AddItemsAdapter(getContext().getApplicationContext(), task.getCheckList());
        listView.setAdapter(itemsAdapter);

        addChecklistBtn.setOnClickListener(this::addValue);

        assignFromContacts = view.findViewById(R.id.assign_from_contacts_text_view);
        if (!task.getAssignedUserName().isEmpty())
            assignFromContacts.setText(ContactUtil.getInstance().resolveContact(task.getAssignedUser()).getDisplayName());

        if (!isPersonalTask) {
            assignFromContacts.setOnClickListener(v -> {
                ArrayList<ExistUser> users = (ArrayList<ExistUser>) ContactUtil.getInstance().resolveContacts(group.getMemberDetails());
                ContactPickerListDialogFragment fragment = ContactPickerListDialogFragment.newInstance(users);

                fragment.show(getChildFragmentManager(), "Contacts");


            });
        } else {
            assignFromContacts.setText(AuthServiceImpl.getCurrentUser().getDisplayName());
        }

        view.findViewById(R.id.save_task_button_mmm).setOnClickListener(v -> {
            String title = gettitle.getText().toString();
            if (title.isEmpty()) {
                AlertUtil.showAlert(getActivity(), getString(R.string.error_task_title));
                return;
            }
            List<CheckListItem> nonEmpty = new ArrayList<>();
            for (CheckListItem checkListItem : task.getCheckList()) {
                if(!TextUtil.isNullOrEmpty(checkListItem.getTitle())){
                    nonEmpty.add(checkListItem);
                }
            }
            task.setCheckList(nonEmpty);
            v.setEnabled(false);
            task.setTitle(title);
            task.setDescription(getDescript.getText().toString());
            task.setStatus(Task.TaskStatus.valueOf(returnStatus(spinner.getSelectedItem().toString())));
            task.setUpdatedDate(new Date());
            task.setAssignedUserImage(UrlUtils.getUserImage(task.getAssignedUser()));
            save();
            LocalState.getInstance().setTaskRead(this.task);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

            getActivity().onBackPressed();


        });

        return view;
    }

    private void setupToolbar() {
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbarTitle = getActivity().findViewById(R.id.toolbar_task_title);
        if (title == null || title.isEmpty()) {
            toolbarTitle.setText(R.string.add_task);
        } else {
            toolbarTitle.setText(title);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbarTitle.setClickable(false);
    }

    protected abstract void save();

    public String returnStatus(String out) {

        if (getString(R.string.todo).equals(out)) {
            return Task.TaskStatus.TODO.name();
        } else if (getString(R.string.in_progress).equals(out)) {
            return Task.TaskStatus.IN_PROGRESS.name();
        } else if (getString(R.string.on_hold).equals(out)) {
            return Task.TaskStatus.ON_HOLD.name();
        } else if (getString(R.string.done).equals(out)) {
            return Task.TaskStatus.DONE.name();
        }

        return Task.TaskStatus.TODO.name();
    }

    public void addValue(View v) {
        String name = "";
        CheckListItem checkListItem = new CheckListItem();
        checkListItem.setTitle(name);
        checkListItem.setCompleted(false);
        task.getCheckList().add(checkListItem);
        itemsAdapter.notifyDataSetChanged();

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
                        Set<String> oneContact = new HashSet<>();

                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {

                                String assignee = numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String assignee_name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                assignFromContacts.setText(assignee_name);
                                task.setAssignedUserName(assignee_name);
                                task.setAssignedUser(ContactUtil.getInstance().cleanNo(assignee));
                                String num1 = assignee.replaceAll("\\s+", "");
                                oneContact.add(num1);
                                System.out.println(" AAAAAAAAAA   " + num1);


                            }

                            numbers.close();
                            selectOneContact(oneContact, new ContactSelectedListener() {
                                @Override
                                public void onSelected(String number) {
                                    //contactName.add(name);
                                    number = ContactUtil.getInstance().cleanNo(number);
                                    if (number != null && !number.isEmpty()) {

                                        task.setAssignedUserName(number);

                                    }

                                }

                            });
                        }
                    }
                    break;
                }
        }

    }

    protected void inviteAssignee() {
        List<String> members = new ArrayList<>();
        members.add(task.getAssignedUser());
        contactService.existsInPlatform(members, new ContactService.Listener() {
            @Override
            public void onCompleteSearch(List<ExistUser> users, List<String> isExisting) {
                if (users.size() == 1) {
                    ExistUser user = users.get(0);
                    task.setAssignedUserName(user.getDisplayName());
                    service.update(task, new ServiceListener() {
                        @Override
                        public void onSuccess() {
                            Timber.d("user updated");
                        }
                    });
                } else {
                    contactService.inviteAssignee(task.getAssignedUser(), group, task, new ContactService.Listener() {
                        @Override
                        public void onInvited() {
                            Timber.d("user invited");
                        }
                    });
                }
            }
        });
    }


    private void selectOneContact(Set<String> oneContact, ContactSelectedListener listener) {
        String[] numbers = oneContact.toArray(new String[oneContact.size()]);

        if (numbers.length == 0)
            return;

        if (numbers.length == 1) {
            listener.onSelected(numbers[0]);
            return;
        }
        AlertDialog.Builder contactDialog = new AlertDialog.Builder(getContext());
        contactDialog.setTitle("Select one contact to add");
        contactDialog.setItems(numbers, (dialog, which) -> listener.onSelected(numbers[which]));
        contactDialog.show();

    }

    interface ContactSelectedListener {

        void onSelected(String number);
    }

    @Override
    public void onContactPickerClicked(int position) {
        Timber.d(group.getMembers().get(position));
        ExistUser user = group.getMemberDetails().get(position);
        assignFromContacts.setText(user.getDisplayName());
        task.setAssignedUserName(user.getDisplayName());
        task.setAssignedUser(user.getPhoneNumber());
    }

    @Override
    public void onContactButtonClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        } else {

            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
