package com.example.gsf98.notebook.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PasswordHelper
{
    public static void save(Context context, String password )
    {
        SharedPreferences setting = context.getSharedPreferences( Constant.PREF_NAME, Activity.MODE_PRIVATE );
        setting.edit().putString( Constant.KEY_PASSWORD, password ).commit();
    }

    public static String read(Context context )
    {
        SharedPreferences setting = context.getSharedPreferences( Constant.PREF_NAME, Activity.MODE_PRIVATE );
        return setting.getString( Constant.KEY_PASSWORD, "" );
    }
}
