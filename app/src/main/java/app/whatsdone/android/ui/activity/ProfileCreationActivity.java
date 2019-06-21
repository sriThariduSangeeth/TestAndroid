package app.whatsdone.android.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.ActivityProfileCreationBinding;
import app.whatsdone.android.ui.presenter.ProfilePresenter;
import app.whatsdone.android.ui.presenter.ProfilePresenterImpl;
import app.whatsdone.android.ui.view.ProfileView;
import app.whatsdone.android.ui.viewmodel.ProfileViewModel;
import app.whatsdone.android.utils.AlertUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class ProfileCreationActivity extends AppCompatActivity implements ProfileView{
    private CircleImageView profilePic;
    ProfilePresenter presenter = new ProfilePresenterImpl(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileCreationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_creation);
        binding.setPresenter(presenter);
        binding.setViewModel(new ProfileViewModel(""));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profilePic = findViewById(R.id.profilePic);
        profilePic.setOnClickListener(v -> requestMultiplePermissions());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
                    presenter.setProfileImage(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                    Timber.e(e);
                }

                profilePic.setImageBitmap(bmp);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Timber.e(error);
            }
        }
    }


    private void  requestMultiplePermissions(){
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
                            CropImage.activity()
                                    .start(ProfileCreationActivity.this);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                            Toast.makeText(getApplicationContext(), "All permissions are denied by user!", Toast.LENGTH_SHORT).show();
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
    public void showValidationError() {
        AlertUtil.showAlert(this, "Name cannot be empty");
    }
}

