package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.Group;

public class GroupServiceImpl implements GroupService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = GroupServiceImpl.class.getCanonicalName();

    @Override
    public void getAllGroups(String userId, GroupServiceListener groupServiceListener) {
        List<Group> groups = new ArrayList<>();

        db.collection("groups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            groupServiceListener.onGroupsUpdated(groups);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    @Override
    public void create(Group group, GroupServiceListener groupServiceListener) {

    }

    @Override
    public void update(Group group, GroupServiceListener groupServiceListener) {

    }

    @Override
    public void delete(String id, GroupServiceListener groupServiceListener) {

    }

    @Override
    public void subscribe(String userId, GroupServiceListener groupServiceListener) {

    }

    @Override
    public void unSubscribe() {

    }
}
