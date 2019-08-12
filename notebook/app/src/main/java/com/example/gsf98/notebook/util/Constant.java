package com.example.gsf98.notebook.util;

public class Constant
{
    public static final int PAGE_NUM = 10;

    public static final String SD_PATH = "moodiary";
    public static final String PREF_NAME = "com.liuzb.moodary";

    public static final String TRANS_KEY_DIARY = "TRANS_KEY_DIARY";
    public static final String TRANS_KEY_PASSWORD = "TRANS_KEY_PASSWORD";
    public static final String TRANS_KEY_ONE = "TRANS_KEY_ONE";

    public static final String KEY_PASSWORD = "KEY_PASSWORD";

    public static final String TABLE_DIARY = "t_diary";
    public static final String TABLE_ATTACHMENT = "t_attachment";
    public static final String TABLE_TAG = "t_tag";
    public static final String TABLE_DIARY_TAG_REL = "t_diary_tag_rel";

    public static final String SQL_TABLE_DIARY = "CREATE TABLE IF NOT EXISTS t_diary (id INTEGER PRIMARY KEY AUTOINCREMENT, c_datetime INTEGER NOT NULL,c_mood INTEGER DEFAULT 0,c_content TEXT);";
    public static final String SQL_TABLE_ONE = "CREATE TABLE IF NOT EXISTS t_tag (id INTEGER PRIMARY KEY AUTOINCREMENT, c_name VARCHAR(100) NOT NULL);";
    public static final String SQL_TABLE_DIARY_ONE_REL = "CREATE TABLE IF NOT EXISTS t_diary_one_rel (id INTEGER PRIMARY KEY AUTOINCREMENT, c_diary_id INTEGER NOT NULL,c_tag_id INTEGER NOT NULL);";
}