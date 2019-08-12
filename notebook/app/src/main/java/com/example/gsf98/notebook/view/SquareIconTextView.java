package com.example.gsf98.notebook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SquareIconTextView extends IconTextView
{
    public SquareIconTextView(Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    public SquareIconTextView(Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public SquareIconTextView( Context context )
    {
        super( context );
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec )
    {
        setMeasuredDimension( getDefaultSize( 0, widthMeasureSpec ), getDefaultSize( 0, heightMeasureSpec ) );

        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        // int childHeightSize = getMeasuredHeight();

        // 高度和宽度一样
        heightMeasureSpec = widthMeasureSpec = View.MeasureSpec.makeMeasureSpec( childWidthSize, View.MeasureSpec.EXACTLY );
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );

    }
}
