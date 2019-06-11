package app.whatsdone.android.services;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.whatsdone.android.model.Contact;
import app.whatsdone.android.model.ContactRequestItem;
import app.whatsdone.android.model.ContactSyncRequest;
import app.whatsdone.android.model.ContactSyncResponse;
import app.whatsdone.android.model.ExistInPlatformRequest;
import app.whatsdone.android.model.ExistInPlatformResponse;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.InviteAssigneeRequest;
import app.whatsdone.android.model.InviteAssigneeResponse;
import app.whatsdone.android.model.InviteMembersRequest;
import app.whatsdone.android.model.InviteMembersResponse;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ContactServiceImpl implements ContactService {
    CloudService service;
    public ContactServiceImpl() {
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



    @Override
    public void syncContacts(List<Contact> contacts, Listener serviceListener) {

        ContactSyncRequest request = new ContactSyncRequest();
        List<ContactRequestItem> items = new ArrayList<>();
        for (Contact contact:contacts) {
            ContactRequestItem item = new ContactRequestItem();
            item.setContactName(contact.getDisplayName());
            item.setContactNo(contact.getPhoneNumber());
        }
        request.setContacts(items);
        request.setByUser(AuthServiceImpl.getCurrentUser().getDocumentID());
        Call<ContactSyncResponse> call = service.syncContacts(request);
        call.enqueue(new Callback<ContactSyncResponse>() {
            @Override
            public void onResponse(@NotNull Call<ContactSyncResponse> call, @NotNull retrofit2.Response<ContactSyncResponse> response) {
                ContactSyncResponse data = response.body();
                if (data != null && data.isSuccess()) {
                    serviceListener.onContactsSynced(data.getAdded(), data.getDeleted());
                    return;
                }
                serviceListener.onError(null);
            }

            @Override
            public void onFailure(Call<ContactSyncResponse> call, Throwable t) {
                serviceListener.onError(t.getLocalizedMessage());
            }
        });

    }

    @Override
    public void existsInPlatform(List<String> contacts, Listener serviceListener) {
        ExistInPlatformRequest request = new ExistInPlatformRequest();

        request.setContacts(contacts);

        Call<ExistInPlatformResponse> call = service.existInPlatform(request);

        call.enqueue(new Callback<ExistInPlatformResponse>() {
            @Override
            public void onResponse(@NotNull Call<ExistInPlatformResponse> call, @NotNull retrofit2.Response<ExistInPlatformResponse> response) {
                ExistInPlatformResponse data = response.body();
                if (data != null) {
                    serviceListener.onCompleteSearch(data.getExistNumbers());
                    return;
                }
                serviceListener.onError(null);
            }

            @Override
            public void onFailure(Call<ExistInPlatformResponse> call, Throwable t) {
                serviceListener.onError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void inviteAssignee(Contact contact, Group group, Task task, Listener serviceListener) {
        InviteAssigneeRequest request = new InviteAssigneeRequest();

        request.setAssignee(contact.getPhoneNumber());
        request.setGroupTitle(group.getGroupName());
        request.setTaskId(task.getDocumentID());
        request.setTaskTitle(task.getTitle());

        Call<InviteAssigneeResponse> call = service.inviteAssignee(request);

        call.enqueue(new Callback<InviteAssigneeResponse>() {
            @Override
            public void onResponse(@NotNull Call<InviteAssigneeResponse> call, @NotNull retrofit2.Response<InviteAssigneeResponse> response) {
                InviteAssigneeResponse data = response.body();
                if (data != null && data.isSuccess()) {
                    serviceListener.onInvited();
                    return;
                }
                serviceListener.onError(null);
            }

            @Override
            public void onFailure(Call<InviteAssigneeResponse> call, Throwable t) {
                serviceListener.onError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void inviteMembers(List<String> members, Group group, Listener serviceListener) {
        InviteMembersRequest request = new InviteMembersRequest();
        request.setMembers(members);
        request.setGroupId(group.getDocumentID());
        request.setGroupTitle(group.getGroupName());

        Call<InviteMembersResponse> call = service.inviteMembers(request);

        call.enqueue(new Callback<InviteMembersResponse>() {
            @Override
            public void onResponse(@NotNull Call<InviteMembersResponse> call, @NotNull retrofit2.Response<InviteMembersResponse> response) {
                InviteMembersResponse data = response.body();
                if (data != null && data.isSuccess()) {
                    serviceListener.onInvited();
                    return;
                }
                serviceListener.onError(null);
            }

            @Override
            public void onFailure(Call<InviteMembersResponse> call, Throwable t) {
                serviceListener.onError(t.getLocalizedMessage());
            }
        });
    }
}
