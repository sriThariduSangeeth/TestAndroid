package app.whatsdone.android.services;

import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import app.whatsdone.android.model.User;

public class AuthServiceImpl implements AuthService {
    final static String TAG = AuthServiceImpl.class.getSimpleName();
    static User user = new User();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public void Login(String verificationId, String verificationCode, ServiceListener listener) {

    }

    @Override
    public void register(String phoneNo, ServiceListener listener) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                (Executor) this,               // Activity (for callback binding)
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
                        listener.onCompleted(data);
                        // ...
                    }
                });        // OnVerificationStateChangedCallbacks
    }

    @Override
    public void logout() {

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, ServiceListener listener) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        AuthServiceImpl.user.setDocumentID(user.getPhoneNumber());
                        listener.onSuccess();
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
