package com.example.bookstore

data class BookDetails(
    val title: MutableList<String>,
    val author: MutableList<String>,
    val numPages: MutableList<String>,
    val imageUrl: MutableList<String>,
    val price: MutableList<Int>,
) {
    val size: Int = title.count()
}

data class BookInfo(
    val title: String,
    val author: String,
    val numPages: String,
    val imageUrl: String,
    val price: Int,
)