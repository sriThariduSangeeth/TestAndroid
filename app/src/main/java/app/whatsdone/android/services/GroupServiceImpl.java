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
    public List<Group> loadGroup() {
        List<Group> groups = new ArrayList<>();

        db.collection("groups")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        return groups;
    }
}
