package com.example.gsf98.notebook.adapter;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.gsf98.notebook.R;
import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.view.IconColorUtil;
import com.example.gsf98.notebook.view.IconTextView;

import java.util.List;

public class EmojiAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    private List<IconTag> names;
    private Context context;
    private int textSize;
    private int type;

    public EmojiAdapter(Context context, List<IconTag> names, int type )
    {
        this.context = context;
        this.names = names;
        this.type = type;
        inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount()
    {
        return names.size();
    }

    @Override
    public Object getItem(int arg0 )
    {
        return names.get( arg0 );
    }

    @Override
    public long getItemId( int arg0 )
    {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent )
    {
        ViewHolder holder = null;
        if( convertView == null )
        {
            holder = new ViewHolder();
            convertView = inflater.inflate( R.layout.menu_left_item, null );
            holder.emojiText = (IconTextView)convertView.findViewById( R.id.menu_left_item_text );
            convertView.setTag( holder );
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        IconTag emojiBean = names.get( position );
        holder.emojiText.setTextSize( textSize );
        holder.emojiText.setIcon( emojiBean.getText() );

        if( emojiBean.isSelect() )
        {
            holder.emojiText.setTextColor( context.getResources().getColor( R.color.clr_tag_text_focus ) );
            holder.emojiText.setBackgroundDrawable( addStateDrawable( emojiBean, true ) );
        }
        else
        {
            holder.emojiText.setTextColor( context.getResources().getColorStateList( R.color.clr_text ) );
            holder.emojiText.setBackgroundDrawable( addStateDrawable( emojiBean, false ) );
        }

        return convertView;
    }

    private StateListDrawable addStateDrawable(IconTag emojiBean, boolean isSelect )
    {
        StateListDrawable sd = new StateListDrawable();

        ShapeDrawable normal = new ShapeDrawable( new OvalShape() );
        if( isSelect )
        {
            normal.getPaint().setColor( context.getResources().getColor( IconColorUtil.getFaMap().get( emojiBean.getText() )[0] ) );
        }
        else
        {
            if( type == 0 )
            {
                normal.getPaint().setColor( context.getResources().getColor( R.color.clr_tag_bg ) );
            }
            else
            {
                normal.getPaint().setColor( context.getResources().getColor( R.color.clr_tag_bg2 ) );
            }
        }

        ShapeDrawable focus = new ShapeDrawable( new OvalShape() );
        focus.getPaint().setColor( context.getResources().getColor( IconColorUtil.getFaMap().get( emojiBean.getText() )[0] ) );

        sd.addState( new int[]{ android.R.attr.state_pressed }, focus );
        sd.addState( new int[]{ android.R.attr.state_focused }, focus );
        sd.addState( new int[]{}, normal );
        return sd;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled( int position )
    {
        return true;
    }

    public void setTextSize( int size )
    {
        textSize = size;
    }

    public final class ViewHolder
    {
        public IconTextView emojiText;
    }
}
