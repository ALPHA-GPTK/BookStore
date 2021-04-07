package com.example.bookstore

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE  TABLE user(user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_name TEXT, user_email TEXT, user_username TEXT, user_password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertUserData(name: String, email: String, username: String, password: String) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put("user_name", name)
        values.put("user_email", email)
        values.put("user_username", username)
        values.put("user_password", password)

        db.insert("user", null, values)
        db.close()
    }

    fun isUserExists(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM user " +
                "WHERE user_username = '$username' AND user_password = '$password'"
        val cursor = db.rawQuery(query, null)

        if (cursor.count <= 0) {
            cursor.close()
            return false
        }

        cursor.close()
        return true
    }

    companion object {
        internal const val DATABASE_NAME = "user"
        internal const val DATABASE_VERSION = 1
    }
}