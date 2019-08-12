package com.example.gsf98.notebook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 可以嵌套到ListView
 * 
 * @author liuzb
 */
public class IconGridView extends GridView
{
    public IconGridView(Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public IconGridView( Context context )
    {
        super( context );
    }

    public IconGridView(Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    public void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
    {

        int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST );
        super.onMeasure( widthMeasureSpec, expandSpec );
    }
}