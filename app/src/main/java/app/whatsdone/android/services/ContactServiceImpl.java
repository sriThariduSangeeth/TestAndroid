package app.whatsdone.android.services;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.whatsdone.android.model.ContactRequestItem;
import app.whatsdone.android.model.ContactSyncRequest;
import app.whatsdone.android.model.ContactSyncResponse;
import app.whatsdone.android.model.ExistInPlatformRequest;
import app.whatsdone.android.model.ExistInPlatformResponse;
import app.whatsdone.android.model.ExistUser;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.InviteAssigneeRequest;
import app.whatsdone.android.model.InviteAssigneeResponse;
import app.whatsdone.android.model.InviteMembersRequest;
import app.whatsdone.android.model.InviteMembersResponse;
import app.whatsdone.android.model.Task;
import app.whatsdone.android.utils.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ContactServiceImpl implements ContactService {
    private CloudService service;
    public ContactServiceImpl() {
        Retrofit retrofit = ServiceFactory.getRetrofitService();
        service = retrofit.create(CloudService.class);
    }

    @Override
    public void syncContacts(Map<String, String> contacts, Listener serviceListener) {

        ContactSyncRequest request = new ContactSyncRequest();
        List<ContactRequestItem> items = new ArrayList<>();
        for (String key:contacts.keySet()) {
            ContactRequestItem item = new ContactRequestItem();
            item.setContactName(contacts.get(key));
            item.setContactNo(key);
            items.add(item);
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
                    List<String> numbers = new ArrayList<>();
                    List<ExistUser> users = data.getUsers();
                    for (ExistUser user :
                            users) {
                        numbers.add(user.getPhoneNumber());
                    }
                    serviceListener.onCompleteSearch(users, numbers);
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
    public void notifyAssignee(String contact, Group group, Task task, Listener serviceListener) {
        InviteAssigneeRequest request = new InviteAssigneeRequest();

        request.setAssignee(contact);
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
    public void notifyMembers(List<String> members, Group group, Listener serviceListener) {
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
