package app.whatsdone.android.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.presenter.AddGroupPresenter;
import app.whatsdone.android.ui.presenter.AddGroupPresenterImpl;
import app.whatsdone.android.ui.view.AddGroupFragmentView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AddGroupFragment extends Fragment implements AddGroupFragmentView {

    private static final int RESULT_LOAD_IMAGE = 0;
    private OnAddFragmentInteractionListener mListener;
    private Toolbar toolbar;
    private TextView teamPhoto, contactListTextView;
    private Button addMembers;
    private CircleImageView circleImageView;
    private Uri selectedImage;
    private List<String> contacts = new ArrayList<String>();
    private final static int RQS_PICK_CONTACT = 1;
    private ListView contactListView;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Cursor cursor;
    private ArrayAdapter arrayAdapter;
    private AddGroupPresenter presenter;
    private EditText teamName;
    private GroupServiceImpl groupService = new GroupServiceImpl();
    private Group group;
    private ServiceListener serviceListener;
    private TextView memberListTextView;
    private ConstraintLayout constraintLayout;
    private List<String> admins = new ArrayList<String>();


    public AddGroupFragment() {
        // Required empty public constructor
    }


    public static GroupContainerFragment newInstance(String param1, String param2) {
        GroupContainerFragment fragment = new GroupContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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


        circleImageView = (CircleImageView) view.findViewById(R.id.group_photo_image_view);
        teamPhoto = (TextView) view.findViewById(R.id.teamPhoto_text_view);
        contactListView = (ListView) view.findViewById(R.id.add_members_list_view);
        addMembers = (Button) view.findViewById(R.id.add_members_button);
        teamName = (EditText) view.findViewById(R.id.group_name_edit_text);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout3);




        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);

            }
        });

//        circleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("image view");
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent,RESULT_LOAD_IMAGE);
//
//            }
//        });

        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method


                }

                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contacts);
                contactListView.setAdapter(arrayAdapter);


            }
        });


        presenter = new AddGroupPresenterImpl();
        this.presenter.init(this,getActivity());

       ((AddGroupPresenterImpl) presenter).setContext(getActivity());
       group = new Group();

       //memberListTextView = view.findViewById(R.id.text1)
        view.findViewById(R.id.save_group_fab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admins.add(AuthServiceImpl.user.getDocumentID());
                group.setTeamImage(getImageData(circleImageView));
                group.setGroupName(teamName.getText().toString());
                group.setMembers(contacts);
                group.setCreatedBy(AuthServiceImpl.user.getDocumentID());
                group.setAdmins(admins);


                contacts.add(AuthServiceImpl.user.getDocumentID());
              //  AuthServiceImpl.user.getDocumentID();

                System.out.println("User doc Id" + AuthServiceImpl.user.getDocumentID());
                presenter.create(group);
            }
        });


//        toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().onBackPressed();
//            }
//        });
        return view;

    }

    private Bitmap getImageData(ImageView imageView){
        //Get the data from an ImageView as bytes
        if (imageView == null) return null;
        Drawable drawable = imageView.getDrawable();

        if(drawable == null) return null;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return  bitmap;
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
        getActivity().onBackPressed();
    }

    @Override
    public void onGroupError(String errorMessage) {
        Log.d("failed creating a group",errorMessage);

    }

    public interface OnAddFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSave();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

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
                          try {
                              //Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                              Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                              while (numbers.moveToNext()) {

                                  arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contacts);
                                  contactListView.setAdapter(arrayAdapter);

                                  String name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                  contacts.add(name);
                                 // contacts.add(AuthServiceImpl.user.getDocumentID());
                                  // contactListTextView.setText(num);
                                  // Toast.makeText(AddGroupFragment.this, "Number="+num, Toast.LENGTH_LONG).show();

                              }numbers.close();
                          } catch(Exception exception){
                              Log.d("test ", exception.getMessage());
                          }
                        }
                    }
                    break;
                }
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

//    private static final int REQUEST_CAPTURE_IMAGE = 100;
//
//    private void openCameraIntent() {
//        Intent pictureIntent = new Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE
//        );
//
//        if(pictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
//            startActivityForResult(pictureIntent,
//                    REQUEST_CAPTURE_IMAGE);
//        }
//    }



}
