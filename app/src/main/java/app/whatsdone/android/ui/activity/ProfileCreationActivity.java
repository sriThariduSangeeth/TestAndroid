package app.whatsdone.android.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.ActivityProfileCreationBinding;
import app.whatsdone.android.ui.fragments.BottomSheetFragment;
import app.whatsdone.android.ui.presenter.ProfilePresenter;
import app.whatsdone.android.ui.presenter.ProfilePresenterImpl;
import app.whatsdone.android.ui.view.ProfileView;
import app.whatsdone.android.ui.viewmodel.ProfileViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaRecorder.VideoSource.CAMERA;


public class ProfileCreationActivity extends AppCompatActivity implements ProfileView, BottomSheetFragment.Listener {
    private static final int RESULT_LOAD_IMAGE = 0;
    private CircleImageView profilePic;
    ProfilePresenter presenter = new ProfilePresenterImpl(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileCreationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_creation);
        binding.setPresenter(presenter);
        binding.setViewModel(new ProfileViewModel(""));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profilePic = findViewById(R.id.profilePic);
        profilePic.setOnClickListener(v -> showBottomSheetDialogFragment());
    }

    BottomSheetFragment bottomSheetFragment;
    public void showBottomSheetDialogFragment()
    {
        bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.setListener(this);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }
    public void exitBottomSheetDialogFragment()
    {
        bottomSheetFragment.dismiss();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                presenter.setProfileImage(bmp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            profilePic.setImageBitmap(bmp);
        }else if(requestCode == CAMERA)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(thumbnail);
        }
    }


    private void  requestMultiplePermissions(boolean isCamera){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            if(!isCamera){
                                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                            }else {
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA);
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }


                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }



    private Bitmap getBitmapFromUri(Uri uri) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onProfileUpdated() {
        Intent intent = new Intent(ProfileCreationActivity.this,GroupsActivity.class);

        startActivity(intent);
    }

    @Override
    public void dismiss() {
        finish();
    }

    @Override
    public void onGallerySelected() {
        requestMultiplePermissions(false);
    }
    public void onCameraSelected()
    {
        requestMultiplePermissions(true);
    }
}

