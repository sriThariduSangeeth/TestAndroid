package app.whatsdone.android.services;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import app.whatsdone.android.model.User;
import app.whatsdone.android.model.UserUpdateRequest;
import app.whatsdone.android.model.UserUpdateResponse;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.ServiceFactory;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class AuthServiceImpl implements AuthService {
    final static String TAG = AuthServiceImpl.class.getSimpleName();
    private static User user = new User();
    private final CloudService service;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Activity context;

    public AuthServiceImpl() {
        Retrofit retrofit = ServiceFactory.getRetrofitService();
        service = retrofit.create(CloudService.class);
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    @Override
    public void updateProfile(User user,@Nullable Listener listener) {
        FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates;
        if(user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getDisplayName())
                    .setPhotoUri(Uri.parse(user.getAvatar()))
                    .build();
        }else {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getDisplayName())
                    .build();
        }

        fireUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Timber.tag(TAG).d("User profile updated.");
                        AuthServiceImpl.user.setAvatar(user.getAvatar());
                        AuthServiceImpl.user.setDisplayName(user.getDisplayName());
                        if(listener !=null) listener.onSuccess();
                    }else {
                        if(listener != null )listener.onError(task.getException().getLocalizedMessage());
                    }
                });

        notifyProfileUpdated(user.getDisplayName(), user.getAvatar(), user.getDocumentID());
    }

    private void notifyProfileUpdated(String displayName, String avatar, String id) {
        UserUpdateRequest request = new UserUpdateRequest(displayName, avatar, id);

        Call<UserUpdateResponse> call = service.onUserUpdated(request);

        call.enqueue(new Callback<UserUpdateResponse>() {
            @Override
            public void onResponse(Call<UserUpdateResponse> call, Response<UserUpdateResponse> response) {
                UserUpdateResponse data = response.body();
                if(data != null && data.isSuccess()){
                    Timber.d("%d records updated for user %s", data.getCount(), id);
                }
            }

            @Override
            public void onFailure(Call<UserUpdateResponse> call, Throwable t) {
                Timber.e(t);
            }
        });
    }



    public static void refreshToken(){
        try {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        SharedPreferencesUtil.save(Constants.SHARED_TOKEN, task.getResult().getToken());
                    }
                });
            }
        }catch (Exception ex){
            Timber.e(ex);
        }
    }

    public static User getCurrentUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if(firebaseUser != null) {
                user.setDocumentID(firebaseUser.getPhoneNumber());
                user.setPhoneNo(firebaseUser.getPhoneNumber());
                if (firebaseUser.getPhotoUrl() != null)
                    user.setAvatar(Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString());
                if(firebaseUser.getDisplayName() != null) {
                    user.setDisplayName(firebaseUser.getDisplayName());
                }
            }
        }

        return user;
    }

    @Override
    public void Login(String verificationId, String verificationCode, AuthService.Listener listener) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        signInWithPhoneAuthCredential(credential, listener);
    }

    @Override
    public void register(String phoneNo, AuthService.Listener listener) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                context,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Timber.tag(TAG).e("onVerificationCompleted");
                        SharedPreferencesUtil.save(Constants.SHARED_PHONE, phoneNo);
                        signInWithPhoneAuthCredential(phoneAuthCredential, listener);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Timber.e(e);
                        listener.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Timber.tag(TAG).d("onCodeSent:%s", verificationId);
                        SharedPreferencesUtil.save(Constants.SHARED_PHONE, phoneNo);
                        // Save verification ID and resending token so we can use them later
                        String mVerificationId = verificationId;
                        PhoneAuthProvider.ForceResendingToken mResendToken = token;
                        listener.onCodeSent(mVerificationId);
                        // ...
                    }
                });        // OnVerificationStateChangedCallbacks
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, AuthService.Listener listener) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.tag(TAG).d("signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        AuthServiceImpl.user.setDocumentID(user.getPhoneNumber());
                        String phoneNo = SharedPreferencesUtil.getString(Constants.SHARED_PHONE);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> data = new HashMap<>();
                        data.put(Constants.FIELD_USER_PHONE_NO, phoneNo);
                        data.put(Constants.FIELD_USER_ACTIVE, 1);
                        db.collection(Constants.REF_USERS).document(phoneNo).set(data, SetOptions.merge());

                        user.getIdToken(false).addOnCompleteListener(context, new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if(task.isSuccessful()) {
                                    System.out.println(task.getResult().getToken());
                                    SharedPreferencesUtil.save(Constants.SHARED_TOKEN, task.getResult().getToken());
                                    listener.onSuccess();
                                }else {
                                    listener.onError(task.getException().getLocalizedMessage());
                                }
                            }
                        });

                        // ...
                    } else {
                        // Sign in failed, display a message and update the UI
                        Timber.tag(TAG).w(task.getException(), "signInWithCredential:failure");
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }

                        listener.onError(task.getException().getLocalizedMessage());
                    }
                });
    }
}
