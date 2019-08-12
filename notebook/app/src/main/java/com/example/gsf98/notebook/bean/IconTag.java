package com.example.gsf98.notebook.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.gsf98.notebook.entity.base.Entity;

public class IconTag extends Entity implements Parcelable
{
    private String text;
    private boolean select;

    public IconTag()
    {
    }

    public IconTag( Parcel in )
    {
        text = in.readString();
        select = ( in.readInt() == 0 ? false : true );
    }

    public boolean isSelect()
    {
        return select;
    }

    public void setSelect( boolean select )
    {
        this.select = select;
    }

    public IconTag( String text )
    {
        this.text = text;
        this.select = false;
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public void switchSelect()
    {
        select = !select;
    }

    public boolean equals( Object obj )
    {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;

        IconTag other = (IconTag)obj;
        if( this.getText().equals( other.getText() ) ) return true;
        return false;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags )
    {
        dest.writeString( text );
        dest.writeInt( select ? 1 : 0 );
    }

    public static final Parcelable.Creator<IconTag> CREATOR = new Parcelable.Creator<IconTag>()
    {
        public IconTag createFromParcel( Parcel in )
        {
            return new IconTag( in );
        }

        public IconTag[] newArray( int size )
        {
            return new IconTag[size];
        }
    };
}
