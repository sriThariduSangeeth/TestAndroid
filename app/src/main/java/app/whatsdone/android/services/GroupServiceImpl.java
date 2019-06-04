package app.whatsdone.android.services;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Group;

public class GroupServiceImpl implements GroupService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = GroupServiceImpl.class.getCanonicalName();

    @Override
    public void getAllGroups(String userId, ServiceListener serviceListener) {
        List<BaseEntity> groups = new ArrayList<>();

        db.collection("groups")
                .whereArrayContains("members", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Group group = new Group();
                            group.setGroupName(document.getString("title"));
                            groups.add(group);
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
        db.collection("groups")
                .whereArrayContains("members", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<BaseEntity> groups = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("title") != null) {
                                Group group = new Group();
                                group.setGroupName(doc.getString("title"));
                                groups.add(group);
                            }
                        }
                        Log.d(TAG, "Current cites in CA: " + groups);

                        serviceListener.onDataReceived(groups);
                    }
                });
    }

    @Override
    public void unSubscribe() {

    }
}
