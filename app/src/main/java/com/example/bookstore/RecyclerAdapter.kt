package com.example.bookstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    val context: Context,
    var title: MutableList<String>,
    var author: MutableList<String>,
    var page: MutableList<String>,
    var image: MutableList<String>,
    val username: String,
    val password: String
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemAuthor.text = author[position]
        holder.itemPage.text = page[position]
        Picasso.get().load(image[position]).resize(50, 50).centerCrop().into(holder.itemImage)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemAuthor: TextView = itemView.findViewById(R.id.item_author)
        var itemPage: TextView = itemView.findViewById(R.id.item_page)
        var itemButton: Button = itemView.findViewById(R.id.btn_add)

        init {
            val db = DatabaseHelper(context)

            itemButton.setOnClickListener {
                val position: Int = adapterPosition
                val insertedBook = db.insertBookData(
                    title[position],
                    author[position],
                    image[position],
                    page[position],
                    username,
                    password
                )
                if (insertedBook) {
                    Toast.makeText(
                        itemView.context,
                        "${title[position]} has been added to cart.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        itemView.context,
                        "${title[position]} NOT ADDED.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}