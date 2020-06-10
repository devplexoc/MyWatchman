package com.plexoc.mywatchman.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.plexoc.mywatchman.Activity.HomeActivity;
import com.plexoc.mywatchman.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static int NOTIFICATION_ID = 110;

    String channelId = "", channelName = "", channelDescription = "";
    private int notificationPriority;
    NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e("Notification", "From : " + remoteMessage.getFrom());
            Log.e("Notification", "Data : " + remoteMessage.getData().toString());

            sendNotification(remoteMessage);

            /*String type = remoteMessage.getData().get("type");
            if (type != null && type.equalsIgnoreCase("logout")) {
                //user logout
                //TODO uncomment above line in final production
            } else {
                try {
                    sendNotification(remoteMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(RemoteMessage remoteMessage) {
        String title = "", message = "", id = "", status = "";
        JSONObject objData = null;
        Intent intent = new Intent(this, HomeActivity.class);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channelId = getString(R.string.channel_id_default);
        channelName = getString(R.string.channel_name_default);
        channelDescription = getString(R.string.channel_description_default);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationPriority = NotificationManager.IMPORTANCE_DEFAULT;
        }
        try {

            title = getString(R.string.app_name);
            if (remoteMessage.getData().containsKey("message")) {
                if (remoteMessage.getData().containsKey("title"))
                    title = remoteMessage.getData().get("title");
                message = remoteMessage.getData().get("message");
                id = remoteMessage.getData().get("id");
                status = remoteMessage.getData().get("status");
            }

            intent.putExtra("id", id);
            intent.putExtra("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (!remoteMessage.getData().containsKey("Data")) {
            title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
            message = Objects.requireNonNull(remoteMessage.getNotification()).getBody();
        } else {
            try {
                objData = new JSONObject(remoteMessage.getData().get("Data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            title = getString(R.string.app_name);
            if (remoteMessage.getData().containsKey("message")) {
                if (remoteMessage.getData().containsKey("title"))
                    title = remoteMessage.getData().get("title");
                message = remoteMessage.getData().get("message");
                id = remoteMessage.getData().get("id");
            }

            intent.putExtra("id", id);

        }*/

        // You only need to create the channel on API 26+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assert notificationManager != null;
            createChannel(notificationManager, channelId, channelName, channelDescription, notificationPriority);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText(message);
        Bitmap remotePicture = null;

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title).setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);

        if (remoteMessage.getData().containsKey("image")) {
            try {
                remotePicture = BitmapFactory.decodeStream((InputStream) new URL(remoteMessage.getData().get("image")).getContent());
                notiStyle.bigPicture(remotePicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (remotePicture != null) notificationBuilder.setStyle(notiStyle);
        }
        if (notificationManager != null)
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private static void createChannel(NotificationManager mNotificationManager, String channelId, String channelName, String channelDescription, int importance) {

        NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);

        // Configure the notification channel.
        mChannel.setDescription(channelDescription);
        mChannel.setShowBadge(false);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }

    // [START refresh_token]
    @Override
    public void onNewToken(String refreshedToken) {
        Log.e("Refreshed token: ", refreshedToken);
        /*session = new SessionManager(getApplicationContext());
        if (session.getDataByKey(SessionManager.IS_LOGIN, false)) {
            sendRegistrationToServer(refreshedToken);
        }*/
    }

    /**
     * Persist token to third-party servers.
     * * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param refreshedToken The new token.
     */

    /*private void sendRegistrationToServer(String refreshedToken) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(session.getUserDetail().getId()));
        params.put("deviceToken", refreshedToken);
        params.put("deviceType", "A");

        try {
            Retrofit.with(this)
                    .setParameters(params)
                    .setAPI(APIs.API_REFRESH_TOKEN)
                    .setCallBackMarketYardListener(new JSONCallbackMarketYard(this) {
                        @Override
                        protected void onSuccess(int statusCode, String jsonObject) {
                            Logger.e(TAG, jsonObject.toString());
                        }

                        @Override
                        protected void onFailed(int statusCode, String message) {
                            Logger.e(TAG, message);
                        }

                        @Override
                        protected void onFailure(String failureMessage) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
