package com.example.gsf98.notebook.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ListView;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utility
{

    private Utility()
    {
        // Forbidden being instantiated.
    }

    public static String encodeUrl(Map<String, String> param )
    {
        if( param == null )
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> keys = param.keySet();
        boolean first = true;

        for( String key : keys )
        {
            String value = param.get( key );
            // pain...EditMyProfileDao params' values can be empty
            if( !TextUtils.isEmpty( value ) || key.equals( "description" ) || key.equals( "url" ) )
            {
                if( first )
                {
                    first = false;
                }
                else
                {
                    sb.append( "&" );
                }
                try
                {
                    sb.append( URLEncoder.encode( key, "UTF-8" ) ).append( "=" ).append( URLEncoder.encode( param.get( key ), "UTF-8" ) );
                }
                catch( UnsupportedEncodingException e )
                {

                }
            }

        }

        return sb.toString();
    }

    public static Bundle decodeUrl(String s )
    {
        Bundle params = new Bundle();
        if( s != null )
        {
            String array[] = s.split( "&" );
            for( String parameter : array )
            {
                String v[] = parameter.split( "=" );
                try
                {
                    params.putString( URLDecoder.decode( v[0], "UTF-8" ), URLDecoder.decode( v[1], "UTF-8" ) );
                }
                catch( UnsupportedEncodingException e )
                {
                    e.printStackTrace();

                }
            }
        }
        return params;
    }

    public static void closeSilently( Closeable closeable )
    {
        if( closeable != null )
        {
            try
            {
                closeable.close();
            }
            catch( IOException ignored )
            {

            }
        }
    }

    /**
     * Parse a URL query and fragment parameters into a key-value bundle.
     */
    public static Bundle parseUrl(String url )
    {
        // hack to prevent MalformedURLException
        url = url.replace( "weiboconnect", "http" );
        try
        {
            URL u = new URL( url );
            Bundle b = decodeUrl( u.getQuery() );
            b.putAll( decodeUrl( u.getRef() ) );
            return b;
        }
        catch( MalformedURLException e )
        {
            return new Bundle();
        }
    }

    public static void cancelTasks( AsyncTask... tasks )
    {
        for( AsyncTask task : tasks )
        {
            if( task != null )
            {
                task.cancel( true );
            }
        }
    }

    public static boolean isTaskStopped( AsyncTask task )
    {
        return task == null || task.getStatus() == AsyncTask.Status.FINISHED;
    }

    public static int length( String paramString )
    {
        int i = 0;
        for( int j = 0; j < paramString.length(); j++ )
        {
            if( paramString.substring( j, j + 1 ).matches( "[Α-￥]" ) )
            {
                i += 2;
            }
            else
            {
                i++;
            }
        }

        if( i % 2 > 0 )
        {
            i = 1 + i / 2;
        }
        else
        {
            i = i / 2;
        }

        return i;
    }

    public static boolean isConnected( Context context )
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifi( Context context )
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if( networkInfo != null && networkInfo.isConnected() )
        {
            if( networkInfo.getType() == ConnectivityManager.TYPE_WIFI )
            {
                return true;
            }
        }
        return false;
    }

    public static int getNetType( Context context )
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if( networkInfo != null && networkInfo.isConnected() )
        {
            return networkInfo.getType();
        }
        return -1;
    }

    public static boolean isGprs( Context context )
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if( networkInfo != null && networkInfo.isConnected() )
        {
            if( networkInfo.getType() != ConnectivityManager.TYPE_WIFI )
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isSystemRinger( Context context )
    {
        AudioManager manager = (AudioManager)context.getSystemService( Context.AUDIO_SERVICE );
        return manager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public static String getPicPathFromUri(Uri uri, Activity activity )
    {
        String value = uri.getPath();

        if( value.startsWith( "/external" ) )
        {
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.managedQuery( uri, proj, null, null, null );
            int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            cursor.moveToFirst();
            return cursor.getString( column_index );
        }
        else
        {
            return value;
        }
    }

    public static boolean isAllNotNull( Object... obs )
    {
        for( int i = 0; i < obs.length; i++ )
        {
            if( obs[i] == null )
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isIntentSafe(Context activity, Uri uri )
    {
        Intent mapCall = new Intent( Intent.ACTION_VIEW, uri );
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities( mapCall, 0 );
        return activities.size() > 0;
    }

    public static boolean isIntentSafe(Context activity, Intent intent )
    {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities( intent, 0 );
        return activities.size() > 0;
    }

    public static boolean isGooglePlaySafe( Activity activity )
    {
        Uri uri = Uri.parse( "http://play.google.com/store/apps/details?id=com.google.android.gms" );
        Intent mapCall = new Intent( Intent.ACTION_VIEW, uri );
        mapCall.addFlags( Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
        mapCall.setPackage( "com.android.vending" );
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities( mapCall, 0 );
        return activities.size() > 0;
    }

    public static Rect locateView(View v )
    {
        int[] location = new int[2];
        if( v == null )
        {
            return null;
        }
        try
        {
            v.getLocationOnScreen( location );
        }
        catch( NullPointerException npe )
        {
            // Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect locationRect = new Rect();
        locationRect.left = location[0];
        locationRect.top = location[1];
        locationRect.right = locationRect.left + v.getWidth();
        locationRect.bottom = locationRect.top + v.getHeight();
        return locationRect;
    }

    public static int countWord(String content, String word, int preCount )
    {
        int count = preCount;
        int index = content.indexOf( word );
        if( index == -1 )
        {
            return count;
        }
        else
        {
            count++;
            return countWord( content.substring( index + word.length() ), word, count );
        }
    }

    public static void vibrate(Context context, View view )
    {
        // Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // vibrator.vibrate(30);
        view.performHapticFeedback( HapticFeedbackConstants.LONG_PRESS );
    }

    public static void playClickSound( View view )
    {
        view.playSoundEffect( SoundEffectConstants.CLICK );
    }

    public static View getListViewItemViewFromPosition(ListView listView, int position )
    {
        return listView.getChildAt( position - listView.getFirstVisiblePosition() );
    }
}
