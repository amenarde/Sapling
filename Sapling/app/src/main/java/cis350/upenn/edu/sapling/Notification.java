package cis350.upenn.edu.sapling;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by amenarde on 3/31/18.
 */

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //initChannels(context);

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, LoggingActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.ic_sapling_1)
                .setContentTitle("Sapling")
                .setContentText("Click to log your progress for the day!")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(1, mNotifyBuilder.build());
        // 1 means that each of these notifications is actually the same, and will hence
        // overwrite each other

    }

    // from https://stackoverflow.com/questions/44443690/notificationcompat-with-api-26
    // there is like no data on how to use channels on Android's websites
    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "sapling channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("to send log reminders");
        notificationManager.createNotificationChannel(channel);
    }
}