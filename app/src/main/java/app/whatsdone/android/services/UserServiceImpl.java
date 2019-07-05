package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.User;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import timber.log.Timber;

public class UserServiceImpl implements UserService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = UserServiceImpl.class.getSimpleName();

    @Override
    public void getById(String id, UserService.Listener serviceListener) {
        db.collection(Constants.REF_USERS)
                .document(id)
                .get().addOnCompleteListener((task) -> {
            if (!task.isSuccessful()) {
                Timber.tag(TAG).w("Task subscription failed");
                serviceListener.onError(task.getException().getLocalizedMessage());
                return;
            }

            DocumentSnapshot doc = task.getResult();
            User user = new User(doc.getData(), doc.getId());
            serviceListener.onUserRetrieved(user);
        });
    }

    @Override
    public void disableNotifications() {
        try {

            new Thread(() -> {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                    String token = SharedPreferencesUtil.getString(Constants.FIELD_USER_DEVICE_TOKENS);
                    if(!token.isEmpty()) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put(Constants.FIELD_USER_DEVICE_TOKENS, FieldValue.arrayRemove(token));
                        data.put(Constants.FIELD_USER_ENABLE_NOTIFICATIONS, false);
                        db.collection(Constants.REF_USERS)
                                .document(AuthServiceImpl.getCurrentUser().getDocumentID())
                                .update(data)
                                .addOnCompleteListener(command -> {
                                    if (command.isSuccessful())

                                        Timber.d("command is success: %s", command.isSuccessful());
                                });
                    }
                    SharedPreferencesUtil.save(Constants.DISABLE_NOTIFICATION, "true");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();



        } catch (Exception ex) {
            Timber.e(ex);
        }
    }

    @Override
    public void enableNotifications() {
        try {

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Timber.tag(TAG).w(task.getException(), "getInstanceId failed");
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put(Constants.FIELD_USER_DEVICE_TOKENS, FieldValue.arrayUnion(token));
                        data.put(Constants.FIELD_USER_ENABLE_NOTIFICATIONS, true);
                        db.collection(Constants.REF_USERS)
                                .document(AuthServiceImpl.getCurrentUser().getDocumentID())
                                .update(data)
                                .addOnCompleteListener(command -> {
                                    if (command.isSuccessful())
                                        SharedPreferencesUtil.save(Constants.FIELD_USER_DEVICE_TOKENS, token);
                                    Timber.d("command is success: %s", command.isSuccessful());
                                });
                        SharedPreferencesUtil.save(Constants.DISABLE_NOTIFICATION, "");
                    });


        } catch (Exception ex) {
            Timber.e(ex);
        }

    }

    @Override
    public void create(BaseEntity entity, ServiceListener serviceListener) {
        User user = (User) entity;
        DocumentReference document = db.collection(Constants.REF_USERS).document(user.getDocumentID());
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_USER_PHONE_NO, user.getPhoneNo());
        data.put(Constants.FIELD_USER_DISPLAY_NAME, user.getDisplayName());
        data.put(Constants.FIELD_USER_DEVICE_TOKENS, user.getDeviceTokens());
        data.put(Constants.FIELD_USER_ENABLE_NOTIFICATIONS, user.isEnableNotifications());
        data.put(Constants.FIELd_USER_STATUS, user.getStatus());

        document.set(data, SetOptions.merge()).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error updating document.", task.getException());

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(task.isSuccessful());
        });

    }

    @Override
    public void update(BaseEntity entity, ServiceListener serviceListener) {
        User user = (User) entity;
        DocumentReference document = db.collection(Constants.REF_USERS).document(user.getDocumentID());
        HashMap<String, Object> data = new HashMap<>();
        data.put(Constants.FIELD_USER_DISPLAY_NAME, user.getDisplayName());
        data.put(Constants.FIELD_USER_ENABLE_NOTIFICATIONS, user.isEnableNotifications());
        data.put(Constants.FIELd_USER_STATUS, user.getStatus());

        document.set(data, SetOptions.merge()).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Timber.tag(TAG).w(task.getException(), "Error updating document.");

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(task.isSuccessful());
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.tag(TAG).w(task.getException(), "getInstanceId failed");
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    document.update(Constants.FIELD_USER_DEVICE_TOKENS, FieldValue.arrayUnion(token));
                });
    }

    @Override
    public void delete(String documentID, ServiceListener serviceListener) {
        serviceListener.onError("Users are not allowed to delete");
    }

}
