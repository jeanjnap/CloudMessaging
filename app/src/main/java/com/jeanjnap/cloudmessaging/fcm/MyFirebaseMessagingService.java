package com.jeanjnap.cloudmessaging.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.List;

import com.jeanjnap.cloudmessaging.R;
import com.squareup.picasso.Picasso;

/**
 * @author ton1n8o - antoniocarlos.dev@gmail.com on 6/13/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private static final String TAG = "MyGcmListenerService";
    private static final String TAG = "RES";

    String image, title, text, sound;
    int id=0;
    private String currentActivity;

    @Override
    public void onMessageReceived(RemoteMessage message) {

        image = message.getNotification().getIcon();
        title = message.getNotification().getTitle();
        text = message.getNotification().getBody();
        sound = message.getNotification().getSound();

        Object obj = message.getData().get("id");
        if (obj != null) {
            id = Integer.valueOf(obj.toString());
        }

        this.sendNotification(new NotificationData(image, id, title, text, sound));
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notificationData GCM message received.
     */
    private void sendNotification(NotificationData notificationData) {

        String msg = String.format(
                "{\"title\": \"%s\", \"text\": \"%s\", \"image\": \"%s\", \"sound\": \"%s\", \"id\": %d}",
                title, text, image, sound, id
        );

        Log.i(TAG, msg);

        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        this.currentActivity = taskInfo.get(0).topActivity.getClassName();
        //Log.i(TAG, "Current Activity: " + currentActivity);

        String url = "https://www.thiengo.com.br/img/post/facebook/650-366/7go5uuk478u4m2us9shqoeg3k0a45ffac74a32604bf42ddc4307f928f0.png";
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with( this )
                    .load( url )
                    .get();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager mNotificationManager;

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(title);
        bigText.bigText(text);


        notification.setSmallIcon(R.drawable.ring);
        notification.setColor(getResources().getColor(R.color.colorAccent));
        notification.setPriority(Notification.PRIORITY_MAX);
        notification.setStyle(bigText);
        //notification.setAutoCancel(true);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);

            notification.setChannelId(channelId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                Log.i(TAG, "Notifications enabled: " + mNotificationManager.areNotificationsEnabled());
        }

        mNotificationManager.notify(id, notification.build());

        //mNotificationManager.cancel(id);

    }

    public boolean isActivityRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }
}