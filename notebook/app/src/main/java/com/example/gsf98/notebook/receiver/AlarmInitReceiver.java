package com.example.gsf98.notebook.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmInitReceiver extends BroadcastReceiver
{
    private Calendar calendar;
    public final String MYACTION = "android.intent.action.STARTMYAP";

    @Override
    public void onReceive(Context context, Intent intent1 )
    {
        String action = intent1.getAction();

        // 如果是开机广播的话就重新设计闹铃
        if( action.equals( Intent.ACTION_BOOT_COMPLETED ) )
        {
            setAlarmTime( context );
        }
    }

    private void setAlarmTime( Context context )
    {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis( System.currentTimeMillis() );
        calendar.set( Calendar.HOUR_OF_DAY, 20 );

        Intent intent = new Intent( MYACTION );
        PendingIntent pi = PendingIntent.getBroadcast( context, 0, intent, 0 );
        AlarmManager am = (AlarmManager)context.getSystemService( Activity.ALARM_SERVICE );
        am.set( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi );
        // 24 * 60 * 60 * 1000
        am.setRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10 * 1000, pi );
    }
}
