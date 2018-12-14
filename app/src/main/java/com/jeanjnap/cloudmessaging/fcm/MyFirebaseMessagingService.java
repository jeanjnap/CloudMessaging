package com.jeanjnap.cloudmessaging.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.List;

import com.jeanjnap.cloudmessaging.Activitys.ResultActivity;
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

        Log.i("RES", "Tem mensagem pra vc");

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

        // Create an Intent for the activity you want to start
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("text", msg);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntentWithParentStack(intent);
        }
        // Get the PendingIntent containing the entire back stack
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_"+id)
                .setSmallIcon(R.drawable.ring)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());

        /*
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "channel_"+id)
                .setSmallIcon(R.drawable.ring)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        createNotificationChannel();

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, mBuilder.build());
        */
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_"+id;
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_"+id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager2 = getSystemService(NotificationManager.class);
            notificationManager2.createNotificationChannel(channel);
        }
    }
}