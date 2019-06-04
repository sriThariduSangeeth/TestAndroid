package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;

public class GroupServiceImpl implements GroupService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = GroupServiceImpl.class.getCanonicalName();

    @Override
    public void getAllGroups(String userId, ServiceListener serviceListener) {
        List<BaseEntity> groups = new ArrayList<>();

        db.collection("groups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            serviceListener.onDataReceived(groups);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void create(Group group, ServiceListener serviceListener) {

    }

    @Override
    public void update(Group group, ServiceListener serviceListener) {

    }

    @Override
    public void delete(String id, ServiceListener serviceListener) {

    }

    @Override
    public void subscribe(String userId, ServiceListener serviceListener) {

    }

    @Override
    public void unSubscribe() {

    }
}
