package app.whatsdone.android.services;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import app.whatsdone.android.model.User;

public class AuthServiceImpl implements AuthService {
    final static String TAG = AuthServiceImpl.class.getSimpleName();
    public static User user = new User();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Activity context;

    public void setContext(Activity context) {
        this.context = context;
    }

    @Override
    public User getCurrentUser() {
        if((user.getDocumentID() == null || user.getDocumentID() == "")
                && firebaseAuth.getCurrentUser() != null){
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            user.setDocumentID(firebaseUser.getPhoneNumber());

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
                        Log.e(TAG, "onVerificationCompleted");
                        signInWithPhoneAuthCredential(phoneAuthCredential, listener);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        listener.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        Log.d(TAG, "onCodeSent:" + verificationId);

                        // Save verification ID and resending token so we can use them later
                        String mVerificationId = verificationId;
                        PhoneAuthProvider.ForceResendingToken mResendToken = token;
                        Map<String, Object> data = new HashMap<>();
                        data.put("verificationID", mVerificationId);
                        data.put("token", mResendToken);
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
                .addOnCompleteListener(context, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        AuthServiceImpl.user.setDocumentID(user.getPhoneNumber());
                        user.getIdToken(false).addOnCompleteListener(context, new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                System.out.println(task.getResult().getToken());
                                listener.onSuccess();
                            }
                        });

                        // ...
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }

                        listener.onError(task.getException().getLocalizedMessage());
                    }
                });
    }
}
