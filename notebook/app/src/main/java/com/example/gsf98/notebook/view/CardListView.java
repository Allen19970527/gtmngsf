package com.example.gsf98.notebook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.gsf98.notebook.R;

public class CardListView extends QuickReturnListView
{
    public CardListView( Context context )
    {
        super( context );
        init( null );
    }

    public CardListView(Context context, AttributeSet attrs )
    {
        super( context, attrs );
        init( attrs );
    }

    public CardListView(Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
        init( attrs );
    }

    private void init( AttributeSet attrs )
    {
        setDivider( null );
        setDividerHeight( 0 );
        setSelector( android.R.color.transparent );

        if( attrs != null )
        {
            TypedArray a = getContext().obtainStyledAttributes( attrs, new int[]{ android.R.attr.background } );
            if( a.length() > 0 )
            {
                int color = a.getColor( 0, 0 );
                if( color == 0 )
                {
                    setDefaultBackground();
                }
                else
                {
                    setBackgroundColor( color );
                    setCacheColorHint( color );
                }
            }
            else
                setDefaultBackground();
        }
        else
            setDefaultBackground();
    }

    private void setDefaultBackground()
    {
        int gray = getResources().getColor( R.color.card_gray );
        setBackgroundColor( gray );
        setCacheColorHint( gray );
    }
}
