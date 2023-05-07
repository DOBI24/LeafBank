package com.leafbank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.leafbank.history.HistoryInActivity;

public class NotificationHandler {
    private static final String CHANNEL_ID = "leafbank_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private final NotificationManager manager;
    private final Context context;


    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "LeafBank notification",
                NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.YELLOW);
        channel.setDescription("History");
        this.manager.createNotificationChannel(channel);
    }

    public void SendTransferNotification(String message) {
        Intent intent = new Intent(context, HistoryInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("LeafBank")
                .setContentText(message)
                .setSmallIcon(R.drawable.outmoney)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }

    public void SendEmailNotification(String message) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);

        //Intent intent = new Intent(context, HistoryInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("LeafBank")
                .setContentText(message)
                .setSmallIcon(R.drawable.outline_mark_email_read_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }
}
