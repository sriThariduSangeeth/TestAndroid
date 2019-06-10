package app.whatsdone.android.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

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
import app.whatsdone.android.utils.ContactUtil;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static android.widget.Toast.LENGTH_LONG;

public abstract class BaseFragment extends Fragment implements BaseGroupFragmentView, TextWatcher {

    private static final int RESULT_LOAD_IMAGE = 0;
    private OnAddFragmentInteractionListener mListener;
    private Button addMembers;
    protected CircleImageView circleImageView;
    private Uri selectedImage;
    protected List<String> contactNumbers = new ArrayList<String>();
    protected List<String> contactName = new ArrayList<String>();
    private final static int RQS_PICK_CONTACT = 1;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    protected AddEditGroupPresenter presenter;
    protected EditText teamName;
    protected Group group;
    private ConstraintLayout constraintLayout;
    private List<Contact> members = new ArrayList<Contact>();
    private SwipeMenuListView swipeListView;
    ListViewCustomArrayAdapter adapter;
    HashSet contactSet = new HashSet<>();


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


        contactName = new ArrayList<>();
        circleImageView = view.findViewById(R.id.group_photo_image_view);
        addMembers = view.findViewById(R.id.add_members_button);
        teamName = view.findViewById(R.id.group_name_edit_text);
        constraintLayout = view.findViewById(R.id.constraintLayout3);
        swipeListView = view.findViewById(R.id.add_members_list_view);

        contactSet = new HashSet();

        adapter = new ListViewCustomArrayAdapter(getActivity().getApplicationContext(), R.layout.member_list_layout, contactNumbers, members);
        swipeListView.setAdapter(adapter);

        //arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.member_list_layout, contactName);
        //swipeListView.setAdapter(arrayAdapter);
        contactNumbers.addAll(group.getMembers());
        members.addAll(ContactUtil.resolveContacts(group.getMembers()));
        adapter.notifyDataSetChanged();
        teamName.setText(group.getGroupName());


        teamName.addTextChangedListener(this);

        //team name
//        if(teamName.length() >  25)
//        {
//            AlertDialog.Builder teamNameLengthAlert = new AlertDialog.Builder(getContext());
//            teamNameLengthAlert.setTitle("Alert");
//            teamNameLengthAlert.setMessage("Number of characters should not exceed than 25");
//
//
//            teamNameLengthAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//
//
//            teamNameLengthAlert.setNegativeButton(android.R.string.no, null);
//            teamNameLengthAlert.setIcon(android.R.drawable.ic_dialog_alert);
//            teamNameLengthAlert.show();
//
//        }


        if (group.getAvatar() != null && !group.getAvatar().isEmpty()) {
            Picasso.get().load(group.getAvatar()).into(circleImageView);
        }

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();

            }
        });
        SwipeList();

        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                //intent.setDataAndType(ContactsContract.Contacts.CONTENT_URI,ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);


                }


            }
        });


        presenter = new AddEditGroupPresenterImpl();
        this.presenter.init(this, getActivity());

        ((AddEditGroupPresenterImpl) presenter).setContext(getActivity());


        view.findViewById(R.id.save_group_fab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (teamName.getText().toString().isEmpty()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Alert");
                    alert.setMessage("Team Name should not be empty");


                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    alert.setNegativeButton(android.R.string.no, null);
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();


                } else {


                    group.setTeamImage(getImageData(circleImageView));
                    group.setGroupName(teamName.getText().toString());
                    group.setMembers(contactNumbers);

                    System.out.println("User doc Id" + AuthServiceImpl.getCurrentUser().getDocumentID());


                    save();
                }

            }


        });


        return view;

    }

    public abstract void save();

    public Bitmap getImageData(ImageView imageView) {
        //Get the data from an ImageView as bytes
        if (imageView == null) return null;
        Drawable drawable = imageView.getDrawable();

        if (drawable == null) return null;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return bitmap;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        //goes back to the group fragment, list of groups
        adapter.notifyDataSetChanged();
        getActivity().onBackPressed();
    }

    @Override
    public void onGroupError(String errorMessage) {
        Log.d("failed creating a group", errorMessage);

    }

    public interface OnAddFragmentInteractionListener {
        void onSave();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        //gallery
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {

                e.printStackTrace();
            }
            circleImageView.setImageBitmap(bmp);
        }
        //camera
        else if (requestCode == CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(bmp);

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
                                    System.out.println(" AAAAAAAAAA   " + num1);

                                }
                                numbers.close();

                                selectOneContact(oneContact, new OnContactSelectedListener() {
                                    @Override
                                    public void onSelected(String contact) {

                                        if (contactNumbers.contains(contact)) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                            alert.setTitle("Alert");
                                            alert.setMessage("" + contactItem.getDisplayName() + " is already a member");

                                            System.out.println("" + contactNumbers);

                                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });


                                            alert.setNegativeButton(android.R.string.no, null);
                                            alert.setIcon(android.R.drawable.ic_dialog_alert);
                                            alert.show();
                                        } else {
                                            //contactName.add(name);
                                            if (contact != null && !contact.isEmpty()) {
                                                contact = ContactUtil.cleanNo(contact);
                                                contactNumbers.add(contact);
                                                List<String> contacts = new ArrayList<>();
                                                contacts.add(contact);
                                                members.addAll(ContactUtil.resolveContacts(contacts));
                                                adapter.notifyDataSetChanged();
                                            }

                                        }
                                    }
                                });


                            } catch (Exception exception) {
                                Log.d("test ", exception.getMessage());
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


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        // requestMultiplePermissions();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void takePhotoFromCamera() {
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
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //swipeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        return true;
    }


    public void SwipeList() {


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                swipeListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getContext());
                item1.setBackground(new ColorDrawable(Color.RED));

                item1.setWidth(200);
                item1.setTitle("DELETE ");
                item1.setTitleSize(18);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

            }
        };

        swipeListView.setMenuCreator(creator);
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

        swipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                String value = adapter.getItem(position);

                contactNumbers.remove(value);
                adapter.notifyDataSetChanged();
                // Toast.makeText(getContext(), "Deleted " + contactNumbers.get(position), Toast.LENGTH_SHORT).show()
                return false;

            }


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

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
