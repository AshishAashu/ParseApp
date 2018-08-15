package com.ashish.parseapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;

public class ParseAppPushReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected NotificationCompat.Builder getNotification(Context context, Intent intent) {
        return super.getNotification(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        //TODO For app specific implementation comment the following line calling super version of this method
        // super.onPushOpen(context, intent);

        //TODO Add App Specific implementation below
        Intent lastIntent = new Intent(context,SecondActivity.class);
        lastIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(lastIntent);
    }
}
