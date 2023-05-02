package com.leafbank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.ConcurrentHashMap;

public class NotificationHandler {
    private static final String CHANNEL_ID = "leafbank_notification_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager manager;
    private Context context;


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

    public void Send(String message){
        //TODO: History activity csere
        Intent intent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("LeafBank")
                .setContentText(message)
                .setSmallIcon(R.drawable.history)
                .setContentIntent(pendingIntent);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.manager.cancel(NOTIFICATION_ID);
    }
}
