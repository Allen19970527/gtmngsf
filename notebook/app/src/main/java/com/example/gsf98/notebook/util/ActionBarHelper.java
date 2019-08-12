package com.example.gsf98.notebook.util;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;

import com.example.gsf98.notebook.R;

public class ActionBarHelper
{
    public static Drawable setColor(Context context, ActionBar actionBar, int color, Drawable.Callback callback )
    {
        Drawable colorDrawable = new ColorDrawable( color );
        Drawable bottomDrawable = context.getResources().getDrawable( R.drawable.actionbar_bottom );
        LayerDrawable ld = new LayerDrawable( new Drawable[]{ colorDrawable, bottomDrawable } );

        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 )
        {
            actionBar.setBackgroundDrawable( ld );
            ld.setCallback( callback );
        }
        else
        {
            actionBar.setBackgroundDrawable( ld );
        }

        return ld;
    }

    public static Drawable setColor(Context context, ActionBar actionBar, Drawable old, int newcolor, Drawable.Callback callback )
    {
        if( old == null ) return setColor( context, actionBar, newcolor, callback );

        Drawable colorDrawable = new ColorDrawable( newcolor );
        Drawable bottomDrawable = context.getResources().getDrawable( R.drawable.actionbar_bottom );
        LayerDrawable ld = new LayerDrawable( new Drawable[]{ colorDrawable, bottomDrawable } );
        TransitionDrawable td = new TransitionDrawable( new Drawable[]{ old, ld } );

        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 )
        {
            actionBar.setBackgroundDrawable( td );
            td.setCallback( callback );
        }
        else
        {
            actionBar.setBackgroundDrawable( td );
        }

        td.startTransition( 200 );
        return td;
    }
}
