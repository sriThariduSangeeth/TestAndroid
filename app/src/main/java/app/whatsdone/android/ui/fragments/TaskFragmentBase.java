package app.whatsdone.android.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import app.whatsdone.android.services.LogService;
import app.whatsdone.android.services.LogServiceImpl;
import app.whatsdone.android.services.TaskService;
import app.whatsdone.android.services.TaskServiceImpl;
import app.whatsdone.android.ui.adapters.AddItemsAdapter;
import app.whatsdone.android.utils.AlertUtil;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactReaderUtil;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.InviteAssigneeUtil;
import app.whatsdone.android.utils.LocalState;
import app.whatsdone.android.utils.SortUtil;
import app.whatsdone.android.utils.TextUtil;
import app.whatsdone.android.utils.UIUtil;
import app.whatsdone.android.utils.UrlUtils;
import timber.log.Timber;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static app.whatsdone.android.utils.SortUtil.clean;

public abstract class TaskFragmentBase extends Fragment implements ContactPickerListDialogFragment.Listener{
    boolean isFromMyTasks;
    boolean isPersonalTask;
    private DatePickerDialog datePickerDialog;
    private TextView setDueDate;
    protected TextView assignFromContacts;
    protected TextView assignedBy;
    private AddItemsAdapter itemsAdapter;
    private EditText getTitle, getDescription;
    private Button acknowledgeButton;
    private View assignedByLayout, assignedToLayout;

    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private DateFormat dateFormat;
    protected String title = "Add Task";
    //data
    protected TaskService service = new TaskServiceImpl();
    GroupService groupService = GroupServiceImpl.getInstance();
    private ContactService contactService = new ContactServiceImpl();
    LogService logService = new LogServiceImpl();

    protected Group group = new Group();
    Task task = new Task();
    Task original = new Task();

    public TaskFragmentBase() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_task, container, false);

        dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        assignedToLayout = view.findViewById(R.id.constraintLayout8);
        assignedByLayout = view.findViewById(R.id.constraintLayout9);
        acknowledgeButton = view.findViewById(R.id.acknowledge_button);
        acknowledgeButton.setEnabled(false);
        acknowledgeButton.setTextColor(getResources().getColor(R.color.textViewColor));

        this.original = this.task.getClone();

        Spinner spinner = view.findViewById(R.id.user_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(Task.TaskStatus.getIndex(task.getStatus()), true);

        getTitle = view.findViewById(R.id.title_edit_text);


        if(!task.getAssignedBy().equals(AuthServiceImpl.getCurrentUser().getPhoneNo()) && task.getAssignedUser().equals(AuthServiceImpl.getCurrentUser().getPhoneNo()))
        {
            if(task.isAcknowledged()) {
                acknowledgeButton.setEnabled(false);
                acknowledgeButton.setTextColor(GREEN);
                acknowledgeButton.setText(getResources().getString(R.string.acknowledged));

            } else  {
                acknowledgeButton.setEnabled(true);
                acknowledgeButton.setClickable(true);
                acknowledgeButton.setTextColor(BLUE);

                acknowledgeButton.setOnClickListener(v -> {
                    task.setAcknowledged(true);
                    acknowledgeButton.setEnabled(false);
                    acknowledgeButton.setTextColor(GREEN);
                    acknowledgeButton.setText(getResources().getString(R.string.acknowledged));
                });
            }
        }


        else if(task.getAssignedBy().equals(AuthServiceImpl.getCurrentUser().getPhoneNo()) && !task.getAssignedUser().equals(AuthServiceImpl.getCurrentUser().getPhoneNo()) && task.getAssignedUser()!=null && !task.getAssignedUser().isEmpty())
        {
            if(task.isAcknowledged())
            {
                acknowledgeButton.setTextColor(GREEN);
                acknowledgeButton.setText(getResources().getString(R.string.acknowledged));
                acknowledgeButton.setEnabled(false);
            } else {
                acknowledgeButton.setText(getResources().getString(R.string.acknowledge_pending));
                acknowledgeButton.setEnabled(false);
                acknowledgeButton.setTextColor(getResources().getColor(R.color.textViewColor));
            }
        }

        else
            acknowledgeButton.setVisibility(View.GONE);




        getTitle.setText(task.getTitle());
        getTitle.setHintTextColor(getResources().getColor(R.color.gray));

        assignedBy = view.findViewById(R.id.assigned_by_text);
        if(group != null)
            assignedBy.setText(ContactUtil.getInstance().resolveContact(task.getAssignedBy(), group.getMemberDetails()).getDisplayName());

        getDescription = view.findViewById(R.id.description_edit_text);
        getDescription.setText(task.getDescription());
        getDescription.setHintTextColor(getResources().getColor(R.color.gray));
        ConstraintLayout lay = view.findViewById(R.id.select_group_view);
        RecyclerView listView = view.findViewById(R.id.list_view_checklist);

        lay.setVisibility(LinearLayout.GONE);
        setupToolbar();

        setDueDate = view.findViewById(R.id.due_date_text_view);
        setDueDate.setText(dateFormat.format(task.getDueDate()));
        final Calendar calendar = Calendar.getInstance();

        setDueDate.setOnClickListener(v -> {
            calendar.setTime(task.getDueDate());
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
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });


        Button addChecklistBtn = view.findViewById(R.id.add_check_list);
        itemsAdapter = new AddItemsAdapter(getContext().getApplicationContext(), task.getCheckList());
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(itemsAdapter);

        addChecklistBtn.setOnClickListener(this::addValue);

        assignFromContacts = view.findViewById(R.id.assign_from_contacts_text_view);

        if (group != null && !TextUtil.isNullOrEmpty(task.getAssignedUser())) {
            assignFromContacts.setText(ContactUtil.getInstance().resolveContact(task.getAssignedUser(), group.getMemberDetails()).getDisplayName());

        }

        if (!isPersonalTask) {
            assignedByLayout.setVisibility(View.VISIBLE);
            assignedToLayout.setVisibility(View.VISIBLE);
            assignFromContacts.setOnClickListener(v -> {
                List<ExistUser> users = ContactUtil.getInstance().resolveContacts(group.getMemberDetails());
                ArrayList<ExistUser> userCleaned = (ArrayList<ExistUser>) clean(group, users);
                ContactPickerListDialogFragment fragment = ContactPickerListDialogFragment.newInstance(userCleaned);
                fragment.show(getChildFragmentManager(), "Contacts");
                task.setAcknowledged(false);
            });
        } else {
            assignedByLayout.setVisibility(View.GONE);
            assignedToLayout.setVisibility(View.GONE);
            assignFromContacts.setText(AuthServiceImpl.getCurrentUser().getDisplayName());

        }

        view.findViewById(R.id.save_task_button_mmm).setOnClickListener(v -> {
            String title = getTitle.getText().toString();
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
            task.setDescription(getDescription.getText().toString());
            task.setStatus(Task.TaskStatus.valueOf(returnStatus(spinner.getSelectedItem().toString())));
            task.setUpdatedDate(new Date());
            task.setAssignedUserImage(UrlUtils.getUserImage(task.getAssignedUser()));
             save();
            LocalState.getInstance().setTaskRead(this.task);
            UIUtil.hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();
        });

        return view;
    }

    private void setupToolbar() {
        this.setHasOptionsMenu(true);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_task_title);
        if (title == null || title.isEmpty()) {
            toolbarTitle.setText(R.string.add_task);
        } else {
            toolbarTitle.setText(title);
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            UIUtil.hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();
        });
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
                    new ContactReaderUtil(data, getContext(), task).selectContact(task -> {
                        assignFromContacts.setText(task.getAssignedUserName());
                    });

                    break;
                }
        }//

    }

    protected void inviteAssignee() {
        new InviteAssigneeUtil(task, contactService, service, group, groupService).invite();
    }


    @Override
    public void onContactPickerClicked(int position) {
        Timber.d(group.getMembers().get(position));
        ExistUser user = group.getMemberDetails().get(position);
        assignFromContacts.setText(user.getDisplayName());
        task.setAssignedUserName(user.getDisplayName());
        task.setAssignedUser(user.getPhoneNumber());
        assignedBy.setText(AuthServiceImpl.getCurrentUser().getDisplayName());
        task.setAssignedBy(AuthServiceImpl.getCurrentUser().getPhoneNo());
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
