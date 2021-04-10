package com.example.bookstore

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

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
                    "book_pages TEXT, book_price INTEGER, user_id INTEGER NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE ON UPDATE CASCADE)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
        db.execSQL("DROP TABLE IF EXISTS book")
        onCreate(db)
    }

    @SuppressLint("ShowToast")
    fun insertUserData(
        context: Context,
        name: String,
        email: String,
        username: String,
        password: String
    ): Toast {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()

        return if (!isExists("user", username, password)) {
            values.put("user_name", name)
            values.put("user_email", email)
            values.put("user_username", username)
            values.put("user_password", password)
            db.insert("user", null, values)
            db.close()
            Toast.makeText(context, "Successfully Logged In", Toast.LENGTH_SHORT)
        } else {
            db.close()
            Toast.makeText(context, "User Already Exists", Toast.LENGTH_SHORT)
        }
    }

    @SuppressLint("Recycle")
    fun insertBookData(
        bookInfo: BookInfo,
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
        val (title, author, image, pages, price) = bookInfo
        values.put("book_name", title)
        values.put("book_author", author)
        values.put("book_image", image)
        values.put("book_pages", pages)
        values.put("book_price", price)
        values.put("user_id", userId)
        db.insert("book", null, values)

        cursor.close()
        db.close()
        return true
    }

    @SuppressLint("Recycle")
    fun getBookData(
        username: String,
        password: String
    ): BookDetails {
        val title: MutableList<String> = arrayListOf()
        val author: MutableList<String> = arrayListOf()
        val imageUrl: MutableList<String> = arrayListOf()
        val numPages: MutableList<String> = arrayListOf()
        val price: MutableList<Int> = arrayListOf()

        val db: SQLiteDatabase = readableDatabase

        val userQuery = "SELECT * FROM user " +
                "WHERE user_username = '$username' AND user_password = '$password'"
        val userCursor = db.rawQuery(userQuery, null)
        val userId = if (userCursor.moveToFirst()) {
            userCursor.getInt(userCursor.getColumnIndex("user_id"))
        } else {
            0
        }
        val bookQuery = "SELECT * FROM book WHERE user_id = '$userId'"
        val bookCursor = db.rawQuery(bookQuery, null)
        while (bookCursor.moveToNext()) {
            title.add(bookCursor.getString(bookCursor.getColumnIndex("book_name")))
            author.add(bookCursor.getString(bookCursor.getColumnIndex("book_author")))
            imageUrl.add(bookCursor.getString(bookCursor.getColumnIndex("book_image")))
            numPages.add(bookCursor.getString(bookCursor.getColumnIndex("book_pages")))
            price.add(bookCursor.getInt(bookCursor.getColumnIndex("book_price")))
        }

        userCursor.close()
        bookCursor.close()
        db.close()

        return BookDetails(title, author, numPages, imageUrl, price)
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

    @SuppressLint("Recycle")
    fun deleteBookData(
        title: String,
        author: String,
        username: String,
        password: String
    ): Boolean {
        val db: SQLiteDatabase = writableDatabase

        val userQuery = "SELECT * FROM user " +
                "WHERE user_username = '$username' AND user_password = '$password'"
        val userCursor = db.rawQuery(userQuery, null)
        val userId =
            if (userCursor.moveToFirst()) {
                userCursor.getInt(userCursor.getColumnIndex("user_id"))
            } else {
                0
            }

        return db.delete(
            "book",
            "book_name = '$title' AND book_author = '$author' AND user_id = '$userId' ",
            null
        ) > 0
    }

    companion object {
        internal const val DATABASE_NAME = "BookStore"
        internal const val DATABASE_VERSION = 1
    }
}