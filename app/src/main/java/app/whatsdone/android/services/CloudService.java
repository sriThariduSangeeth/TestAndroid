package app.whatsdone.android.services;

import java.util.List;

import app.whatsdone.android.model.ContactSyncRequest;
import app.whatsdone.android.model.ContactSyncResponse;
import app.whatsdone.android.model.ExistInPlatformRequest;
import app.whatsdone.android.model.InviteAssigneeRequest;
import app.whatsdone.android.model.InviteAssigneeResponse;
import app.whatsdone.android.model.InviteMembersRequest;
import app.whatsdone.android.model.InviteMembersResponse;
import app.whatsdone.android.model.LeaveGroupRequest;
import app.whatsdone.android.model.LeaveGroupResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CloudService {
    @POST("syncContacts")
    Call<ContactSyncResponse> syncContacts(@Body ContactSyncRequest request);

    @POST("inviteAssignee")
    Call<InviteAssigneeResponse> inviteAssignee(@Body InviteAssigneeRequest request);

    @POST("inviteMembers")
    Call<InviteMembersResponse> inviteMembers(@Body InviteMembersRequest request);

    @POST("existInPlatform")
    Call<List<String>> existInPlatform(@Body ExistInPlatformRequest request);

    @POST("leaveGroup")
    Call<LeaveGroupResponse> leaveGroup(@Body LeaveGroupRequest request);
}
