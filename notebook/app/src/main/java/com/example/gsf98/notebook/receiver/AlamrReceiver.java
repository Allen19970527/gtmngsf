package com.example.gsf98.notebook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.gsf98.notebook.util.NotifyUtils;

public class AlamrReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent )
    {
        NotifyUtils.setNotification( context );
    }
}
