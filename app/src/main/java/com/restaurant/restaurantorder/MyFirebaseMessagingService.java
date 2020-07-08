package com.restaurant.restaurantorder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "message";
    public static Ringtone r;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
       //  Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String body = remoteMessage.getNotification().getBody();


        //  Intent startIntent = new Intent(this, RingtoneService.class);
        //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        /// startIntent.putExtra("ringtone-uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        // this.startService(startIntent);
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // notificationIntent.putExtra("channel", channel);
        //notificationIntent.putExtra("roomType", roomType);
        notificationIntent.putExtra("body", body);

        startActivity(notificationIntent);
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        Intent intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("body", body);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());


    }
}
