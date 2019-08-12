package com.example.gsf98.notebook;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity
{
    @Override
    protected void onResume()
    {
        super.onResume();
        MobclickAgent.onResume( this );
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MobclickAgent.onPause( this );
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }
}
