package com.example.gsf98.notebook.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeListener implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor sensor;
    long lastUpdate, lastShakeTime = 0;
    float x, y, last_x = 0, last_y = 0;
    static final int SHAKE_THRESHOLD = 1000;
    private Context mContext;
    private ShakeItem shakeItem;

    public ShakeListener( Context context )
    {
        mContext = context;
        // 在构造函数里面注册Sensor服务
        enableSensor();
    }

    public void setShakeItem( ShakeItem shakeItem )
    {
        this.shakeItem = shakeItem;
    }

    // 注册传感器服务，在本java和Activity里面都要注册，但是取消注册的时候，只在activity里面取消注册即可。
    public void enableSensor()
    {
        // 在这里真正注册Service服务
        mSensorManager = (SensorManager)mContext.getSystemService( Context.SENSOR_SERVICE );
        sensor = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );

        if( mSensorManager == null )
        {
            Log.v( "sensor..", "Sensors not supported" );
        }

        mSensorManager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_NORMAL );
    }

    // 取消注册传感器服务
    public void disableSensor()
    {
        if( mSensorManager != null )
        {
            mSensorManager.unregisterListener( this );
            mSensorManager = null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged( SensorEvent e )
    {
        long curTime = System.currentTimeMillis();
        // detect per 100 Millis
        if( ( curTime - lastUpdate ) > 100 )
        {
            long diffTime = ( curTime - lastUpdate );
            lastUpdate = curTime;
            // 这里做了简化，没有用z的数据
            x = e.values[SensorManager.DATA_X];
            y = e.values[SensorManager.DATA_Y];
            // z = Math.abs(values[SensorManager.DATA_Z]);
            float acceChangeRate = 0;// = Math.abs(x+y - last_x - last_y) / diffTime * 1000;
            if( last_x != 0 ) acceChangeRate = Math.abs( x + y - last_x - last_y ) / diffTime * 10000;
            // 这里设定2个阀值，一个是加速度的，一个是shake的间隔时间的
            if( acceChangeRate > SHAKE_THRESHOLD && curTime - lastShakeTime > 2000 )
            {

                lastShakeTime = curTime;
                if( shakeItem != null )
                {
                    shakeItem.onShake();
                }
            }
            last_x = x;
            last_y = y;
        }
    }

}