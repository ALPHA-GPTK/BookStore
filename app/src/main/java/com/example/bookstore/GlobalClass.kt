package com.example.bookstore

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class BookInfo(
    val id: Int = 0,
    var title: String,
    var author: String,
    var numPages: String,
    var imageUrl: String,
    var price: Int,
)

data class UserData(
    var name: String,
    var username: String,
    var email: String
)

fun initRecycler(
    activity: FragmentActivity,
    bookList: MutableList<BookInfo>,
    listener: RecyclerAdapter.OnItemClickListener,
    recyclerView: RecyclerView,
    layout: Int
): RecyclerAdapter {
    val adapter = RecyclerAdapter(bookList, listener, layout)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.setHasFixedSize(true)
    return adapter
}