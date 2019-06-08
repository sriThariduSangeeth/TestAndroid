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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.services.GroupServiceImpl;
import app.whatsdone.android.services.ServiceListener;
import app.whatsdone.android.ui.presenter.AddEditGroupPresenter;
import app.whatsdone.android.ui.presenter.AddEditGroupPresenterImpl;
import app.whatsdone.android.ui.view.BaseGroupFragmentView;
import de.hdodenhof.circleimageview.CircleImageView;
import app.whatsdone.android.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.media.MediaRecorder.VideoSource.CAMERA;

public abstract class BaseFragment extends Fragment implements BaseGroupFragmentView {

    private static final int RESULT_LOAD_IMAGE = 0;
    private OnAddFragmentInteractionListener mListener;
    private Toolbar toolbar;
    private TextView teamPhoto, contactListTextView;
    private Button addMembers;
    protected CircleImageView circleImageView;
    private Uri selectedImage;
    protected List<String> contactNumber = new ArrayList<String>();
    protected List<String> contactName = new ArrayList<String>();


    private final static int RQS_PICK_CONTACT = 1;
    private ListView contactListView;
    private final int REQUEST_CODE = 99;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Cursor cursor;
    private ArrayAdapter arrayAdapter;
    protected AddEditGroupPresenter presenter;
    protected EditText teamName;
    private GroupServiceImpl groupService = new GroupServiceImpl();
    protected Group group;
    private ServiceListener serviceListener;
    private TextView memberListTextView;
    private ConstraintLayout constraintLayout;
    private List<String> admins = new ArrayList<String>();


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

        contactName = new ArrayList<String>();
        circleImageView = (CircleImageView) view.findViewById(R.id.group_photo_image_view);
        teamPhoto = (TextView) view.findViewById(R.id.teamPhoto_text_view);
        contactListView = (ListView) view.findViewById(R.id.add_members_list_view);
        addMembers = (Button) view.findViewById(R.id.add_members_button);
        teamName = (EditText) view.findViewById(R.id.group_name_edit_text);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout3);
       // arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contactName);
        //contactListView.setAdapter(arrayAdapter);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contactName);
        contactListView.setAdapter(arrayAdapter);


        contactName.addAll(group.getMembers());
        teamName.setText(group.getGroupName());
        circleImageView.setImageBitmap(group.getTeamImage());


        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent,RESULT_LOAD_IMAGE);

            }
        });


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


                //arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contacts);



            }
        });


        presenter = new AddEditGroupPresenterImpl();
        this.presenter.init(this,getActivity());

       ((AddEditGroupPresenterImpl) presenter).setContext(getActivity());
       //group = new Group();

       //memberListTextView = view.findViewById(R.id.text1)
        view.findViewById(R.id.save_group_fab_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admins.add(AuthServiceImpl.user.getDocumentID());
                group.setTeamImage(getImageData(circleImageView));
                group.setGroupName(teamName.getText().toString());
                group.setMembers(contactNumber);

                group.setCreatedBy(AuthServiceImpl.user.getDocumentID());
                group.setAdmins(admins);
                contactNumber.add(AuthServiceImpl.user.getDocumentID());
              //  AuthServiceImpl.user.getDocumentID();

                System.out.println("User doc Id" + AuthServiceImpl.user.getDocumentID());

               // if()
                save();
//                if(AuthServiceImpl.getCurrentUser().getPhoneNo()== AuthServiceImpl.user.getDocumentID())
//                {
//                     presenter.updateTeam(group);
//                }
            }
        });


        return view;

    }

    public abstract void save();

    public Bitmap getImageData(ImageView imageView){
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
        void onSave();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        //gallery
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
    //camera
        else if (requestCode == CAMERA) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(bmp);
          //  saveImage(bmp);
            //Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
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
                        if (Integer.valueOf(hasNumber) == 1) {
                            System.out.println("Select Contact");
                          try {
                              //Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                              Cursor numbers = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                              while (numbers.moveToNext()) {

                                 // arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contacts);
                                  contactListView.setAdapter(arrayAdapter);
                                  String number = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                  String name = numbers.getString(numbers.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                  contactName.add(name);
                                  contactNumber.add(number);
                                 // contacts.add(AuthServiceImpl.user.getDocumentID());
                                  // contactListTextView.setText(num);
                                  // Toast.makeText(BaseFragment.this, "Number="+num, Toast.LENGTH_LONG).show();

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


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
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
}
