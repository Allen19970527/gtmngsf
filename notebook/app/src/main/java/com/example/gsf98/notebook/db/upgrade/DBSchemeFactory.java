package com.example.gsf98.notebook.db.upgrade;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gsf98.notebook.db.DBHelper;

import java.util.HashMap;
import java.util.Map;

public final class DBSchemeFactory
{
    private static final Map<Integer, DBScheme> _schemes = new HashMap<Integer, DBScheme>();
    static
    {
        loadSchemes();
    }

    private static void loadSchemes()
    {
        // key : old version code
        _schemes.put( 1, new DBScheme()
        {
            @Override
            public void toNextVerion( DBHelper dbHelper, SQLiteDatabase db )
            {
                Log.v( "kunai update scheme", "success" );
            }
        } );
    }

    public static DBScheme getScheme( int vserionCode )
    {
        return _schemes.get( vserionCode );
    }
}
