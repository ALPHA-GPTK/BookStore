package com.example.bookstore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    private val bookInfo: MutableList<BookInfo>,
    private val listener: OnItemClickListener,
    private var layout: Int

) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

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
            itemButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
