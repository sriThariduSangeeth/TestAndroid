package app.whatsdone.android.services;

import android.app.Activity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import app.whatsdone.android.model.BaseEntity;

public class UserServiceImpl implements UserService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = UserServiceImpl.class.getSimpleName();
    private ListenerRegistration listener;
    private Activity context;

    @Override
    public void getById(String id, ServiceListener listener) {

    }

    @Override
    public void create(BaseEntity entity, ServiceListener listener) {

    }

    @Override
    public void update(BaseEntity entity, ServiceListener listener) {

    }

    @Override
    public void delete(String documentID, ServiceListener listener) {

    }

    @Override
    public void setContext(Activity activity) {
     this.context = context;
    }
}
