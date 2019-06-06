package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.User;
import app.whatsdone.android.utils.Constants;

public class UserServiceImpl implements UserService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = UserServiceImpl.class.getSimpleName();

    @Override
    public void getById(String id, UserService.Listener serviceListener) {
        db.collection(Constants.REF_USERS)
                .document(id)
                .get().addOnCompleteListener ((task) -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Task subscription failed");
                        serviceListener.onError(task.getException().getLocalizedMessage());
                        return;
                    }

                    DocumentSnapshot doc = task.getResult();
                    User user = new User(doc.getData(), doc.getId());
                    serviceListener.onUserRetrieved(user);
                });
    }

    @Override
    public void create(BaseEntity entity, ServiceListener serviceListener) {
        User user = (User)entity;
        DocumentReference document = db.collection(Constants.REF_TEAMS).document(user.getDocumentID());
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_USER_PHONE_NO, user.getPhoneNo());
        data.put(Constants.FIELD_USER_DISPLAY_NAME, user.getDisplayName());
        data.put(Constants.FIELD_USER_DEVICE_TOKENS, user.getDeviceTokens());
        data.put(Constants.FIELd_USER_ENABLE_NOTIFICATIONS, user.isEnableNotifications());
        data.put(Constants.FIELd_USER_STATUS, user.getStatus());

        document.set(data, SetOptions.merge()).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error updating document.", task.getException());

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(null);
        });

    }

    @Override
    public void update(BaseEntity entity, ServiceListener serviceListener) {
        User user = (User)entity;
        DocumentReference document = db.collection(Constants.REF_TEAMS).document(user.getDocumentID());
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_USER_PHONE_NO, user.getPhoneNo());
        data.put(Constants.FIELD_USER_DISPLAY_NAME, user.getDisplayName());
        data.put(Constants.FIELD_USER_DEVICE_TOKENS, user.getDeviceTokens());
        data.put(Constants.FIELd_USER_ENABLE_NOTIFICATIONS, user.isEnableNotifications());
        data.put(Constants.FIELd_USER_STATUS, user.getStatus());

        document.set(data, SetOptions.merge()).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error updating document.", task.getException());

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(null);
        });
    }

    @Override
    public void delete(String documentID, ServiceListener serviceListener) {
        serviceListener.onError("Users are not allowed to delete");
    }

}
