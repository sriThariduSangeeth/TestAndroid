package app.whatsdone.android.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import app.whatsdone.android.R;
import app.whatsdone.android.ui.activity.GroupsActivity;
import app.whatsdone.android.ui.activity.SplashActivity;
import app.whatsdone.android.utils.AlertUtil;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import timber.log.Timber;

public class WhatsDoneFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = WhatsDoneFirebaseMessagingService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Timber.tag(TAG).d("From: %s", remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData()!=null){
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Timber.d(TAG, "key, " + key + " value " + value);
                }}
            Timber.tag(TAG).d("Message data payload: %s", remoteMessage.getData());

            handleNow(remoteMessage);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Timber.tag(TAG).d("Message Notification Body: %s", remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String token) {
        Timber.tag(TAG).d("Refreshed token: %s", token);

        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        //sendNotification("Local notification");
        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     * @param remoteMessage
     */
    private void handleNow(RemoteMessage remoteMessage) {
        Timber.d("Short lived task is done.");
        if(remoteMessage.getData().get("type").equals("task")){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String clickAction = remoteMessage.getNotification().getClickAction();

            Timber.d("title: %s, body: %s, action: %s", title, body,clickAction);
            sendNotification(title, body);
        }
    }

   /*
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        try {
            if(token != null && !token.isEmpty() && !AuthServiceImpl.getCurrentUser().getDocumentID().isEmpty()) {
                if (SharedPreferencesUtil.getString(Constants.DISABLE_NOTIFICATION).isEmpty()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put(Constants.FIELD_USER_DEVICE_TOKENS, FieldValue.arrayUnion(token));
                    db.collection(Constants.REF_USERS)
                            .document(AuthServiceImpl.getCurrentUser().getDocumentID())
                            .update(data)
                            .addOnCompleteListener(command -> {
                                if (command.isSuccessful())
                                    SharedPreferencesUtil.save(Constants.FIELD_USER_DEVICE_TOKENS, token);
                                Timber.d("command is success: %s", command.isSuccessful());
                            });
                }
            }
        }catch (Exception ex){
            Timber.e(ex);
        }

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, GroupsActivity.class);
        if(title.indexOf(Constants.NOTIFICATION_TO_ME) > 0)
            intent.putExtra(Constants.ARG_ACTION, Constants.ACTION_VIEW_TASK);
        intent.putExtra(Constants.ARG_CLICK_ACTION, Constants.ACTION_GROUP_ACTIVITY);

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
