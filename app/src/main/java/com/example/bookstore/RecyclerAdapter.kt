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
    val bookInfo: MutableList<BookInfo>,
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

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val currentItem = bookInfo[position]
        holder.itemTitle.text = currentItem.title
        holder.itemAuthor.text = currentItem.author
        holder.itemPage.text = currentItem.numPages
        Picasso.get().load(currentItem.imageUrl).resize(50, 50).centerCrop().into(holder.itemImage)
        holder.itemPrice.text = currentItem.price.toString()
    }

    override fun getItemCount() = bookInfo.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemAuthor: TextView = itemView.findViewById(R.id.item_author)
        var itemPage: TextView = itemView.findViewById(R.id.item_page)
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)

        // 2131492894 for card_item layout
        // 2131492893 for card_delete layout
        var itemPrice: TextView =
            itemView.findViewById(if (layout.toString() == "2131492894") R.id.item_price_item else R.id.item_price_delete)
        var itemButton: Button =
            itemView.findViewById(if (layout.toString() == "2131492894") R.id.btn_add else R.id.btn_delete_item)

        init {
            val db = DatabaseHelper(context)
            if (layout.toString() == "2131492894") {
                itemButton.setOnClickListener {
                    val position = adapterPosition
                    val currentItem = bookInfo[position]
                    val (title, author, page, image, price) = currentItem
                    val insertedBook =
                        db.insertBookData(
                            BookInfo(title, author, page, image, price), username, password
                        )
                    if (insertedBook) {
                        val snackBar = Snackbar.make(
                            it,
                            "$title has been added to cart.",
                            Snackbar.LENGTH_LONG
                        )
                        val bundle = bundleOf("username" to username, "password" to password)
                        snackBar.setAction("View Cart") {
                            navController.navigate(
                                R.id.action_BookStoreFragment_to_CartFragment,
                                bundle
                            )
                            snackBar.dismiss()
                        }.show()

                    } else {
                        Snackbar.make(it, "$title not added", Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

            } else if (layout.toString() == "2131492893") {
                itemButton.setOnClickListener {
                    val position = adapterPosition
                    val currentItem = bookInfo[position]
                    val (title, author, _, _, _) = currentItem
                    val deletedBook = db.deleteBookData(title, author, username, password)
                    if (deletedBook) {
                        val snackBar = Snackbar.make(
                            it,
                            "$title has been deleted to cart.",
                            Snackbar.LENGTH_LONG
                        )
                        snackBar.setAction("Undo") { snackBar.dismiss() }.show()
                    } else {
                        Snackbar.make(it, "$title not deleted", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
