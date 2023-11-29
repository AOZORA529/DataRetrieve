package com.example.dataretrieve.bean

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(val context: Context, name: String, version: Int):
    SQLiteOpenHelper(context, name, null, version) {


    override fun onCreate(p0: SQLiteDatabase?) {

        val sqlCreateTable = "CREATE TABLE app_usage_info(" +
                "id integer primary key autoincrement," +
                "app text," +
                "time_stamp text," +
                "time_duration integer" +
                ")"

        p0?.execSQL(sqlCreateTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}