package app.whatsdone.android.services;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import app.whatsdone.android.utils.Constants;

public class StorageServiceImpl implements StorageService {
    private static final String TAG = StorageServiceImpl.class.getSimpleName();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    @Override
    public void uploadUserImage(Bitmap bitmap, Listener listener) {
        String path = String.format("images/u/%s/avatar.jpg", AuthServiceImpl.getCurrentUser().getDocumentID());
        Log.d(TAG, path);
        uploadMedia(bitmap, listener, path);

    }

    @Override
    public void uploadGroupImage(Bitmap bitmap, String groupId, Listener listener) {
        String path = String.format("images/g/%s/avatar.jpg", groupId);
        Log.d(TAG, path);
        uploadMedia(bitmap, listener, path);
    }

    private void uploadMedia(Bitmap bitmap, Listener listener, String path) {
        StorageReference storageReference = storageRef.child(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT, false);
        resized.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] data = baos.toByteArray();
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        UploadTask uploadTask = storageReference.putBytes(data, metadata);

        uploadTask.addOnFailureListener(exception -> listener.onError(exception.getLocalizedMessage()))
                .addOnSuccessListener(taskSnapshot -> listener.onSuccess(""));
    }
}
