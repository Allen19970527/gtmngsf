package com.example.gsf98.notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gsf98.notebook.R;
import com.example.gsf98.notebook.bean.MonthBean;
import com.example.gsf98.notebook.entity.Tag;

import java.util.List;

public class MenuRightAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    private List<Tag> tags;
    private List<MonthBean> months;
    private int type;

    public MenuRightAdapter(Context context, List<Tag> tags, List<MonthBean> months, int type )
    {
        inflater = LayoutInflater.from( context );
        this.tags = tags;
        this.months = months;
        this.type = type;
    }

    public void setType( int type )
    {
        this.type = type;
    }

    @Override
    public int getCount()
    {
        return type == 0 ? tags.size() : months.size();
    }

    @Override
    public Object getItem(int arg0 )
    {
        return type == 0 ? tags.get( arg0 ) : months.get( arg0 );
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
            convertView = inflater.inflate( R.layout.menu_right_item, null );
            holder.title = (TextView)convertView.findViewById( R.id.menu_right_item_name );
            convertView.setTag( holder );
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        if( type == 1 )
        {
            MonthBean month = months.get( position );
            holder.title.setText( month.getContent() );
        }
        else
        {
            Tag tag = tags.get( position );
            holder.title.setText( tag.getName() );
        }

        return convertView;
    }

    public final class ViewHolder
    {
        public TextView title;
    }
}
