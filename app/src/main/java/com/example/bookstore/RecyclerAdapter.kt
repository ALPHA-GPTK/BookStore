package com.example.bookstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    val context: Context,
    var title: MutableList<String>,
    var author: MutableList<String>,
    var page: MutableList<String>,
    var image: MutableList<String>,
    val username: String,
    val password: String,
    var navController: NavController,
    var layout: Int

) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        navController = Navigation.findNavController(parent)

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
                    val snackBar = Snackbar.make(
                        it,
                        "${title[position]} has been added to cart.",
                        Snackbar.LENGTH_LONG
                    )
                    val bundle = bundleOf("username" to username, "password" to password)
                    snackBar.setAction("View Cart") {
                        navController.navigate(R.id.action_APiFragment_to_CartFragment, bundle)
                        snackBar.dismiss()
                    }.show()
                } else {
                    Snackbar.make(it, "${title[position]} not added", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}
