package com.example.gsf98.notebook.db.upgrade;

import android.database.sqlite.SQLiteDatabase;

import com.example.gsf98.notebook.db.DBHelper;

public interface DBScheme
{
    void toNextVerion(DBHelper dbHelper, SQLiteDatabase db);
}
