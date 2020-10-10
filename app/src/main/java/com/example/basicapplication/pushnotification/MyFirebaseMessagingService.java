package com.example.basicapplication.pushnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.basicapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.basicapplication.pushnotification.App.FCM_CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static final String TAG = "MyTag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: called");

        Log.d(TAG, "onMessageReceived: Message received from: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

//            Uri uri = Uri.parse("https://nhacchuong123.com/nhac-chuong/nhac-tre/Nhac Chuong Hoa Hai Duong - Jack (J97).mp3");
            Notification notification = new NotificationCompat.Builder(this,FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_chat_black)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.BLUE)
                    .build();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002, notification);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG,"onMessageReceived: DataSize:"+remoteMessage.getData().size());
            for (String key: remoteMessage.getData().keySet()){
                Log.d(TAG, "onMessageReceived: Key: " + remoteMessage.getData().get(key));
            }
            Log.d(TAG, "onMessageReceived: Data: " + remoteMessage.getData().toString());
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG, "onDeletedMessages: called");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: called");
        //upload this token on the app server
    }
}
