package app.whatsdone.android.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.IOException;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.FragmentSettingsBinding;
import app.whatsdone.android.model.User;
import app.whatsdone.android.model.UserStatus;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.activity.LoginActivity;
import app.whatsdone.android.ui.presenter.SettingsPresenter;
import app.whatsdone.android.ui.presenter.SettingsPresenterImpl;
import app.whatsdone.android.ui.view.SettingsView;
import app.whatsdone.android.ui.viewmodel.SettingsViewModel;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment implements SettingsView {

    FragmentSettingsBinding binding;
    private SettingsViewModel model;
    private SettingsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        User current =  AuthServiceImpl.getCurrentUser();

        this.model = new SettingsViewModel(current.getDisplayName(), true, UserStatus.Available, current.getAvatar());
        this.loadProfileImage(current.getAvatar());
        this.presenter = new SettingsPresenterImpl(this, model);
        this.binding.setModel(model);
        this.binding.setPresenter(presenter);
        presenter.initUser();

        return binding.getRoot();

    }

    public void onImageEdit() {
        CropImage.activity()
                .start(getContext(), this);
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onProfileLoaded(User user) {
        //loadProfileImage(user.getAvatar());
        model.setAvatar(user.getAvatar());
        model.setDisplayName(user.getDisplayName());
        model.setEnableNotifications(user.isEnableNotifications());
        model.status.set(user.getStatus().getValue());
    }

    private void loadProfileImage(String avatar){
        if(avatar != null && !avatar.isEmpty()){
            Picasso.get().load(avatar).placeholder(R.mipmap.ic_user_default).into(binding.profilePic);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //presenter.save(model);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

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
                binding.profilePic.setImageBitmap(bmp);
                presenter.uploadUserImage(bmp);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}



