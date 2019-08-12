package com.example.gsf98.notebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gsf98.notebook.R;
import com.example.gsf98.notebook.entity.Tag;
import com.example.gsf98.notebook.view.IconTextView;

import java.util.List;

public class TagAdapter extends BaseAdapter
{
    private final LayoutInflater inflater;
    private List<Tag> tags;

    public TagAdapter(Context context, List<Tag> ones )
    {
        inflater = LayoutInflater.from( context );
        this.tags = ones;
    }

    @Override
    public int getCount()
    {
        return tags.size();
    }

    @Override
    public Object getItem(int position )
    {
        return tags.get( position );
    }

    @Override
    public long getItemId( int position )
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent )
    {
        ViewHolder holder = null;
        if( convertView == null )
        {
            holder = new ViewHolder();
            convertView = inflater.inflate( R.layout.tag_item, null );
            holder.txtName = (TextView)convertView.findViewById( R.id.tag_name );
            holder.icnCheck = (IconTextView)convertView.findViewById( R.id.tag_check );
            convertView.setTag( holder );
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        Tag tag = tags.get( position );
        holder.txtName.setText( tag.getName() );
        holder.icnCheck.setVisibility( tag.isSelect() ? View.VISIBLE : View.INVISIBLE );
        return convertView;
    }

    public final class ViewHolder
    {
        public TextView txtName;
        public IconTextView icnCheck;
    }
}
