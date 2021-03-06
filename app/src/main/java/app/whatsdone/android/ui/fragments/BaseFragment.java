package app.whatsdone.android.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.adapters.ListViewCustomArrayAdapter;
import app.whatsdone.android.ui.presenter.AddEditGroupPresenter;
import app.whatsdone.android.ui.presenter.AddEditGroupPresenterImpl;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;
import app.whatsdone.android.utils.AlertUtil;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.UIUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public abstract class BaseFragment extends Fragment implements BaseGroupFragmentView {

    private OnAddFragmentInteractionListener mListener;
    protected CircleImageView circleImageView;
    protected List<String> contactNumbers = new ArrayList<>();
    protected List<String> contactName = new ArrayList<>();
    private final int REQUEST_CODE = 99;
    protected AddEditGroupPresenter presenter;
    protected EditText teamName;
    protected Group group;
    protected TextView toolbarTitle;
    private List<Contact> members = new ArrayList<Contact>();
    protected SwipeMenuListView swipeListView;
    ListViewCustomArrayAdapter adapter;
    HashSet contactSet = new HashSet<>();
    protected Button addMembers;
    protected ConstraintLayout constraintLayout;
    private boolean saveButtonClickedOnce = false;
    protected FloatingActionButton saveFab;
    protected Toolbar toolbar;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_group, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            UIUtil.hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();
        });
        contactName = new ArrayList<>();

        circleImageView = view.findViewById(R.id.group_photo_image_view);
        addMembers = view.findViewById(R.id.add_members_button);
        teamName = view.findViewById(R.id.group_name_edit_text);
        constraintLayout = view.findViewById(R.id.constraintLayout3);
        swipeListView = view.findViewById(R.id.add_members_list_view);
        saveFab = view.findViewById(R.id.save_group_fab_button);
        toolbarTitle = getActivity().findViewById(R.id.toolbar_task_title);
        contactSet = new HashSet();

        adapter = new ListViewCustomArrayAdapter(getActivity().getApplicationContext(), R.layout.member_list_layout, members);
        swipeListView.setAdapter(adapter);

        contactNumbers.addAll(group.getMembers());

        members.addAll(ContactUtil.getInstance().resolveContacts(group.getMembers(), group.getMemberDetails()));
        adapter.notifyDataSetChanged();
        teamName.setText(group.getGroupName());
        teamName.setHintTextColor(getResources().getColor(R.color.gray));

        checkUserForName();
        checkUserToAddMembers();

        if (group.getAvatar() != null && !group.getAvatar().isEmpty()) {
            Picasso.get().load(group.getAvatar()).into(circleImageView);
        }

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserForTeamImage();
            }
        });

        SwipeList();

        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestContactPermissions();

            }
        });


        presenter = new AddEditGroupPresenterImpl();
        this.presenter.init(this, getActivity());

        ((AddEditGroupPresenterImpl) presenter).setContext(getActivity());


        saveFab.setOnClickListener(v -> {

            if (teamName.getText().toString().isEmpty()) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Alert");
                alert.setMessage("Team Name should not be empty");

                alert.setPositiveButton("OK", (dialog, which) -> {

                });


                alert.setNegativeButton(android.R.string.no, null);
                alert.setIcon(android.R.drawable.ic_dialog_alert);
                alert.show();


            } else {

                group.setTeamImage(getImageData(circleImageView));
                group.setGroupName(teamName.getText().toString());
                group.setMembers(contactNumbers);
                String current = AuthServiceImpl.getCurrentUser().getDocumentID();
                if (!group.getMembers().contains(current)) {
                    group.getMembers().add(current);
                }
                save();
                saveFab.setEnabled(false);
                adapter.notifyDataSetChanged();
            }
        });


        return view;

    }

    public abstract void save();

    public abstract void checkUserForName();

    public abstract void checkUserForTeamImage();

    public abstract void checkUserToAddMembers();


    public Bitmap getImageData(ImageView imageView) {
        //Get the data from an ImageView as bytes
        if (imageView == null) return null;
        Drawable drawable = imageView.getDrawable();

        if (drawable == null) return null;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSave();
        }
    }

    public void setListener(OnAddFragmentInteractionListener handler) {
        mListener = handler;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGroupSaved() {
        adapter.notifyDataSetChanged();
        UIUtil.hideSoftKeyboard(getActivity());
        getActivity().onBackPressed();
    }

    @Override
    public void onGroupError(String errorMessage) {
        Timber.d(errorMessage);

    }

    public interface OnAddFragmentInteractionListener {
        void onSave();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bmp = null;
                try {
                    bmp = getBitmapFromUri(resultUri);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                circleImageView.setImageBitmap(bmp);
                group.setImageChanged(true);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Timber.e(error);
            }
        }

        //contacts
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
                            System.out.println("Select Contact");

                            Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            Contact contactItem = new Contact();

                            try {
                                while (numbers.moveToNext()) {

                                    String name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    contactItem.setDisplayName(name);
                                    String number = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    String num1 = number.replaceAll("\\s+", "");
                                    oneContact.add(num1);

                                }
                                numbers.close();

                                selectOneContact(oneContact, contact -> {
                                    contact = ContactUtil.getInstance().cleanNo(contact);
                                    if (contactNumbers.contains(contact)) {
                                        AlertUtil.showAlert(getActivity(), contactItem.getDisplayName() + " is already a member");
                                    } else {
                                        //contactName.add(name);
                                        if (contact != null && !contact.isEmpty()) {

                                            contactNumbers.add(contact);
                                            List<String> contacts = new ArrayList<>();
                                            contacts.add(contact);
                                            members.addAll(ContactUtil.getInstance().resolveContacts(contacts, group.getMemberDetails()));
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                });


                            } catch (Exception exception) {
                                Timber.d(exception);
                            }
                        }
                    }
                    break;
                }
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    protected void showPictureDialog() {
        requestMultiplePermissions();
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            CropImage.activity()
                                    .start(getContext(), BaseFragment.this);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(), "permissions are not granted by the user!", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    public void SwipeList() {


        SwipeMenuCreator creator = menu -> {
            swipeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            SwipeMenuItem item1 = new SwipeMenuItem(
                    getContext());
            item1.setBackground(new ColorDrawable(Color.RED));

            item1.setWidth(200);
            item1.setTitle("DELETE ");
            item1.setTitleSize(18);
            item1.setTitleColor(Color.WHITE);
            menu.addMenuItem(item1);

        };
        if (AuthServiceImpl.getCurrentUser().getPhoneNo().equals(group.getCreatedBy())) {
            swipeListView.setMenuCreator(creator);
        }
        swipeListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {


                System.out.println(" swipe start");
            }

            @Override
            public void onSwipeEnd(int position) {

                System.out.println("swipe");
            }
        });

        swipeListView.setOnMenuItemClickListener((position, menu, index) -> {

            String value = adapter.getItem(position).getPhoneNumber();

            if (group.getCreatedBy().equals(value)) {
                Toast.makeText(getContext(), "This number can not be deleted" + contactNumbers.get(position), Toast.LENGTH_SHORT).show();
            } else {
                contactNumbers.remove(value);
                members.remove(adapter.getItem(position));
                if(group.getMemberDetails().get(position) != null
                        && group.getMemberDetails().get(position).getPhoneNumber().equals(value)) {
                    group.getMemberDetails().remove(position);
                }
                adapter.notifyDataSetChanged();
                // Toast.makeText(getContext(), "Deleted " + contactNumbers.get(position), Toast.LENGTH_SHORT).show()
            }
            return false;

        });
    }

    private void selectOneContact(Set<String> oneContact, OnContactSelectedListener listener) {
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

    interface OnContactSelectedListener {
        void onSelected(String contact);
    }

    public boolean isSaveButtonClickedOnce() {
        return saveButtonClickedOnce;
    }

    public void setSaveButtonClickedOnce(boolean saveButtonClickedOnce) {
        this.saveButtonClickedOnce = saveButtonClickedOnce;
    }


    private void requestContactPermissions() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                            startActivityForResult(intent, REQUEST_CODE);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(), "permissions are not granted by the user!", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
}
