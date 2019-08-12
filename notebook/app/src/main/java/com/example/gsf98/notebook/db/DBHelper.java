package com.example.gsf98.notebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gsf98.notebook.db.upgrade.DBScheme;
import com.example.gsf98.notebook.db.upgrade.DBSchemeFactory;
import com.example.gsf98.notebook.util.Constant;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_FILE = "mood.db";
    private static final int DB_VERSION = 1;

    public DBHelper( Context context )
    {
        super( context, DB_FILE, null, DB_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
        db.execSQL( Constant.SQL_TABLE_DIARY );
        db.execSQL( Constant.SQL_TABLE_ONE );
        db.execSQL( Constant.SQL_TABLE_DIARY_ONE_REL );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion )
    {
        DBScheme scheme;
        while( oldVersion < newVersion )
        {
            scheme = DBSchemeFactory.getScheme( oldVersion );
            if( scheme != null )
            {
                scheme.toNextVerion( this, db );
            }
            oldVersion++;
        }
    }
}
