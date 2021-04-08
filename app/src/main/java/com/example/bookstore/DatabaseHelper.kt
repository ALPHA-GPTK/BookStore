package com.example.bookstore

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
                    "book_name TEXT NOT NULL, book_author NOT NULL, book_image NOT NULL, " +
                    "book_pages INTEGER, user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE ON UPDATE CASCADE)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertUserData(
//        context: Context,
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
//            return Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
        }

        db.close()
//        return Toast.makeText(context, "User Exist", Toast.LENGTH_SHORT).show()
    }

    fun insertBookData(
//        context: Context,
        title: String,
        author: String,
        image: String,
        pages: Int,
        user_id: Int
    ) {
        val db: SQLiteDatabase = writableDatabase

        val values = ContentValues()
        if (!isExists(title, author, "book")) {
            values.put("book_name", title)
            values.put("book_author", author)
            values.put("book_image", image)
            values.put("book_pages", pages)
            values.put("user_id", user_id)
            db.insert("book", null, values)
            db.close()
//            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
//            return
        }

        db.close()
//        Toast.makeText(context, "User Exist", Toast.LENGTH_SHORT).show()
        return
    }

    fun isExists(dbName: String, username: String, password: String): Boolean {
        val db: SQLiteDatabase = readableDatabase
        val query =
            if (dbName == "user") {
            "SELECT * FROM $dbName " +
                    "WHERE user_username = '$username' AND user_password = '$password'" }
            else {
                "SELECT * FROM $dbName " +
                        "WHERE book_name = '$username' AND book_author = '$password'" }

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