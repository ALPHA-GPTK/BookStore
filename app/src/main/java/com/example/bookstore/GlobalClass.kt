package com.example.bookstore

data class BookInfo(
    val id: Int = 0,
    var title: String,
    var author: String,
    var numPages: String,
    var imageUrl: String,
    var price: Int,
)