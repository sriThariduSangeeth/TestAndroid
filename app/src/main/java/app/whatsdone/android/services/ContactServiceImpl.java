package app.whatsdone.android.services;

import org.jetbrains.annotations.NotNull;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactServiceImpl implements ContactService {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.URL_FIREBASE)
            .build();
    CloudService service = retrofit.create(CloudService.class);

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
            public void onResponse(@NotNull Call<ContactSyncResponse> call, @NotNull Response<ContactSyncResponse> response) {
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
    public void existsInPlatform(List<Contact> contacts, Listener serviceListener) {
        ExistInPlatformRequest request = new ExistInPlatformRequest();
        List<ContactRequestItem> items = new ArrayList<>();
        for (Contact contact:contacts) {
            ContactRequestItem item = new ContactRequestItem();
            item.setContactName(contact.getDisplayName());
            item.setContactNo(contact.getPhoneNumber());
        }
        request.setContacts(items);

        Call<ExistInPlatformResponse> call = service.existInPlatform(request);

        call.enqueue(new Callback<ExistInPlatformResponse>() {
            @Override
            public void onResponse(@NotNull Call<ExistInPlatformResponse> call, @NotNull Response<ExistInPlatformResponse> response) {
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
            public void onResponse(@NotNull Call<InviteAssigneeResponse> call, @NotNull Response<InviteAssigneeResponse> response) {
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
            public void onResponse(@NotNull Call<InviteMembersResponse> call, @NotNull Response<InviteMembersResponse> response) {
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
