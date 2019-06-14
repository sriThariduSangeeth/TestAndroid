package app.whatsdone.android.services;

import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.LeaveGroupRequest;
import app.whatsdone.android.model.LeaveGroupResponse;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ContactUtil;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

public class GroupServiceImpl implements GroupService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = GroupServiceImpl.class.getSimpleName();
    public static Group personalGroup = new Group();
    private ListenerRegistration listener;
    CloudService service;

    public GroupServiceImpl() {
        AuthServiceImpl.refreshToken();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + SharedPreferencesUtil.getString(Constants.SHARED_TOKEN))
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(Constants.URL_FIREBASE)
                .client(client)
                .build();

        service = retrofit.create(CloudService.class);
    }

    public static Group getPersonalGroup() {
        personalGroup.setDocumentID(AuthServiceImpl.getCurrentUser().getDocumentID());
        personalGroup.setGroupName(Constants.GROUP_PERSONAL);
        return personalGroup;
    }

    @Override
    public void getAllGroups(String userId, ServiceListener serviceListener) {
        List<BaseEntity> groups = new ArrayList<>();

        db.collection(Constants.REF_TEAMS)
                .whereArrayContains(Constants.FIELD_GROUP_MEMBERS, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Group group = getGroup(doc);
                            groups.add(group);
                            serviceListener.onDataReceived(groups);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        serviceListener.onError(task.getException().getLocalizedMessage());
                    }
                    serviceListener.onCompleted(task.isSuccessful());
                });
    }

    @NonNull
    private Group getGroup(DocumentSnapshot doc) {
        Group group = new Group();
        group.setDocumentID(doc.getId());
        if (doc.get(Constants.FIELD_GROUP_TITLE) != null)
            group.setGroupName(doc.getString(Constants.FIELD_GROUP_TITLE));
        if (doc.get(Constants.FIELD_GROUP_AVATAR) != null)
            group.setAvatar(doc.getString(Constants.FIELD_GROUP_AVATAR));
        if (doc.get(Constants.FIELD_GROUP_CREATED_BY) != null)
            group.setCreatedBy(doc.getString(Constants.FIELD_GROUP_CREATED_BY));
        if (doc.get(Constants.FIELD_GROUP_DISCUSSION_COUNT) != null)
            group.setDiscussionCount(doc.getLong(Constants.FIELD_GROUP_DISCUSSION_COUNT).intValue());
        if (doc.get(Constants.FIELD_GROUP_TASKS_COUNT) != null)
            group.setTaskCount(doc.getLong(Constants.FIELD_GROUP_TASKS_COUNT).intValue());
        if (doc.get(Constants.FIELD_GROUP_UPDATED_AT) != null)
            group.setUpdatedDate(doc.getDate(Constants.FIELD_GROUP_UPDATED_AT));
        if (doc.get(Constants.FIELD_GROUP_MEMBERS) != null) {
            List<String> members = (List<String>) doc.get(Constants.FIELD_GROUP_MEMBERS);
            group.setMembers(members);
            group.setOriginalMembers(members);
        }
        if (doc.get(Constants.FIELD_GROUP_ADMINS) != null)
            group.setAdmins((List<String>) doc.get(Constants.FIELD_GROUP_ADMINS));
        List<ExistUser> users = new ArrayList<>();
        if (doc.get(Constants.FIELD_GROUP_MEMBERS_DETAILS) != null) {
            List<HashMap> details = (List<HashMap>) doc.get(Constants.FIELD_GROUP_MEMBERS_DETAILS);

            for (HashMap map :
                    details) {
                String phone = (String)map.get(Constants.FIELD_GROUP_MEMBERS_DETAILS_PHONE);
                String isInvited = (String)map.get(Constants.FIELD_GROUP_MEMBERS_DETAILS_INVITED);
                String displayName = (String)map.get(Constants.FIELD_GROUP_MEMBERS_DETAILS_NAME);

                ExistUser user = new ExistUser();
                user.setDisplayName(displayName);
                user.setIsInvited(isInvited);
                user.setPhoneNumber(phone);
                users.add(user);
            }
        }
        group.setMemberDetails(users);
        return group;
    }


    @Override
    public void create(Group group, ServiceListener serviceListener) {

        DocumentReference document = db.collection(Constants.REF_TEAMS).document(group.getDocumentID());
        HashMap<String, Object>  data = new HashMap<>();

        ContactUtil.getInstance().cleanNo(group.getMembers());
        ContactUtil.getInstance().cleanNo(group.getAdmins());

        data.put(Constants.FIELD_GROUP_TITLE, group.getGroupName());
        data.put(Constants.FIELD_GROUP_CREATED_BY, ContactUtil.getInstance().cleanNo(group.getCreatedBy()));
        data.put(Constants.FIELD_GROUP_TASKS_COUNT, 0);
        data.put(Constants.FIELD_GROUP_DISCUSSION_COUNT, 0);
        data.put(Constants.FIELD_GROUP_MANAGED_BY_ADMIN, true);
        data.put(Constants.FIELD_GROUP_ENABLE_USER_TASKS, true);
        data.put(Constants.FIELD_GROUP_MEMBERS,  group.getMembers());
        data.put(Constants.FIELD_GROUP_ADMINS, group.getAdmins());
        data.put(Constants.FIELD_GROUP_AVATAR, group.getAvatar());
        data.put(Constants.FIELD_GROUP_UPDATED_AT, new Date());

        document.set(data).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Timber.w(task.getException(), "Error creating document.");
                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(task.isSuccessful());
        });
    }

    @Override
    public void update(Group group, ServiceListener serviceListener) {
        ContactUtil.getInstance().cleanNo(group.getMembers());

        DocumentReference document = db.collection(Constants.REF_TEAMS).document(group.getDocumentID());
        HashMap<String, Object>  data = new HashMap<>();
        data.put(Constants.FIELD_GROUP_TITLE, group.getGroupName());
        data.put(Constants.FIELD_GROUP_MEMBERS, group.getMembers());
        data.put(Constants.FIELD_GROUP_AVATAR, group.getAvatar());
        data.put(Constants.FIELD_GROUP_UPDATED_AT, new Date());

        document.update(data).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error updating document.", task.getException());

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(task.isSuccessful());
        });

    }

    @Override
    public void delete(String id, ServiceListener serviceListener) {
        db.collection(Constants.REF_TEAMS)
                .document(id)
                .delete()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                    serviceListener.onSuccess();
                   }else {
                       Log.w(TAG, "Error deleting document.", task.getException());
                       serviceListener.onError(task.getException().getLocalizedMessage());
                   }
                    serviceListener.onCompleted(task.isSuccessful());
                });
    }

    @Override
    public void leave(String groupId, ServiceListener serviceListener) {
        LeaveGroupRequest request = new LeaveGroupRequest();
        request.setGroupId(groupId);
        Call<LeaveGroupResponse> call = service.leaveGroup(request);
        call.enqueue(new Callback<LeaveGroupResponse>() {
            @Override
            public void onResponse(@NotNull Call<LeaveGroupResponse> call,@NotNull Response<LeaveGroupResponse> response) {
                LeaveGroupResponse leaveGroupResponse = response.body();
                if (leaveGroupResponse != null && leaveGroupResponse.isSuccess()) {
                    serviceListener.onSuccess();
                    return;
                }
                serviceListener.onError(null);
            }

            @Override
            public void onFailure(Call<LeaveGroupResponse> call, Throwable t) {
                serviceListener.onError(t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void subscribe(ServiceListener serviceListener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        listener = db.collection(Constants.REF_TEAMS)
                .whereArrayContains(Constants.FIELD_GROUP_MEMBERS, user.getPhoneNumber())
                .orderBy(Constants.FIELD_GROUP_UPDATED_AT, Query.Direction.DESCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e != null) {
                        Timber.tag(TAG).w(e, "Team subscription failed");
                        return;
                    }
                    Timber.tag(TAG).d(value.toString());

                    List<BaseEntity> groups = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {

                        try {
                            Group group = getGroup(doc);
                            groups.add(group);
                        }catch (Exception exception) {
                            Timber.tag(TAG).d(exception, "failed to parse group");
                        }

                    }

                    serviceListener.onDataReceived(groups);
                });
    }

    @Override
    public void unSubscribe() {
        if( listener != null) {
            listener.remove();
            listener = null;
        }
    }

    @Override
    public String add() {
        return db.collection(Constants.REF_TEAMS).document().getId();
    }

    @Override
    public void getGroupById(String groupId, ServiceListener serviceListener) {
        db.collection(Constants.REF_TEAMS)
                .document(groupId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            Group group = getGroup(task.getResult());
                            serviceListener.onDataReceived(group);
                        }else {
                            serviceListener.onError(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    @Override
    public void update(Group group, List<ExistUser> users, ServiceListener serviceListener) {
        ContactUtil.getInstance().cleanNo(group.getMembers());

        DocumentReference document = db.collection(Constants.REF_TEAMS).document(group.getDocumentID());
        HashMap<String, Object>  data = new HashMap<>();
        data.put(Constants.FIELD_GROUP_MEMBERS_DETAILS, users);
        data.put(Constants.FIELD_GROUP_UPDATED_AT, new Date());

        document.update(data).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                serviceListener.onSuccess();
            else {
                Log.w(TAG, "Error updating document.", task.getException());

                serviceListener.onError(task.getException().getLocalizedMessage());
            }
            serviceListener.onCompleted(task.isSuccessful());
        });
    }
}
