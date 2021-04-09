package com.example.bookstore

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE user(user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_name TEXT, user_email TEXT, user_username TEXT, user_password TEXT)"
        )
        db.execSQL(
            "CREATE TABLE book(book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "book_name TEXT NOT NULL, book_author TEXT NOT NULL, book_image TEXT NULL, " +
                    "book_pages TEXT, user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE ON UPDATE CASCADE)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS book")
        onCreate(db)
    }

    fun insertUserData(
        name: String,
        email: String,
        username: String,
        password: String
    ) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()

        if (isExists("user", username, password)) {
            values.put("user_name", name)
            values.put("user_email", email)
            values.put("user_username", username)
            values.put("user_password", password)
            db.insert("user", null, values)
            db.close()
        }

        db.close()
    }

    @SuppressLint("Recycle")
    fun insertBookData(
        title: String,
        author: String,
        image: String,
        pages: String,
        username: String,
        password: String
    ): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val query = "SELECT * FROM user " +
                "WHERE user_username = '$username' AND user_password = '$password'"
        val cursor = db.rawQuery(query, null)
        val userId = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex("user_id"))
        } else {
            0
        }
        val values = ContentValues()
        values.put("book_name", title)
        values.put("book_author", author)
        values.put("book_image", image)
        values.put("book_pages", pages)
        values.put("user_id", userId)
        db.insert("book", null, values)
        db.close()
        return true
    }

    fun isExists(dbName: String, username: String, password: String, userId: Int = 0): Boolean {
        val db: SQLiteDatabase = readableDatabase
        val query =
            if (dbName == "user") {
                "SELECT * FROM $dbName " +
                        "WHERE user_username = '$username' AND user_password = '$password'"
            } else {
                "SELECT * FROM $dbName " +
                        "WHERE book_name = '$username' AND book_author = '$password'"
            }

        val cursor = db.rawQuery(query, null)

        if (cursor.count <= 0) {
            cursor.close()
            return false
        }

        cursor.close()
        return true
    }

    companion object {
        internal const val DATABASE_NAME = "BookStore"
        internal const val DATABASE_VERSION = 1
    }
}