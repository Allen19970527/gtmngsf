package com.example.gsf98.notebook.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.gsf98.notebook.FragChangeActivity;
import com.example.gsf98.notebook.R;

public class NotifyUtils
{
    // 添加常驻通知
    public static void setNotification( Context context )
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
        Notification notification = new Notification( R.drawable.ic_launcher, context.getString( R.string.app_name ), System.currentTimeMillis() );
        Intent intent = new Intent( context, FragChangeActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        Bundle bundle = new Bundle();
        bundle.putBoolean( "isNotify", true );
        intent.putExtras( bundle );
        notification.flags = Notification.FLAG_AUTO_CANCEL; // 设置常驻 Flag
        PendingIntent contextIntent = PendingIntent.getActivity( context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        notification.setLatestEventInfo( context, context.getString( R.string.app_name ), context.getString( R.string.notify_content ), contextIntent );
        notificationManager.notify( R.string.app_name, notification );
    }

    // 取消通知
    public static void cancelNotification( Context context )
    {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.cancel( R.string.app_name );
    }
}
