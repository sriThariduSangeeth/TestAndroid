package app.whatsdone.android.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.squareup.picasso.Picasso;

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

    private static int RESULT_LOAD_IMAGE = 1;
    FragmentSettingsBinding binding;
    private SettingsViewModel model;
    private SettingsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        System.out.println("settings fragment");

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        this.model = new SettingsViewModel(AuthServiceImpl.getCurrentUser().getDisplayName(), true, UserStatus.Available, "");
        this.loadProfileImage(AuthServiceImpl.getCurrentUser().getAvatar());
        this.presenter = new SettingsPresenterImpl(this, model);
        this.binding.setModel(model);
        this.binding.setPresenter(presenter);
        presenter.initUser();

        return binding.getRoot();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void onImageEdit() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMAGE);
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
        presenter.save(model);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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
            binding.profilePic.setImageBitmap(bmp);
            presenter.uploadUserImage(bmp);
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



