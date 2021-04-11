package com.example.bookstore


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(), RecyclerAdapter.OnItemClickListener, View.OnClickListener {
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var bookInfo: MutableList<BookInfo>

    private lateinit var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments!!.getString("username").toString()
        password = arguments!!.getString("password").toString()
        bundle = bundleOf("username" to username, "password" to password)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        db = DatabaseHelper(activity!!)

        bookInfo = db.getBookData(username, password)

        // Calculate Total Price
        txv_totalAmount.text = bookInfo.sumBy { it.price }.toString()

        adapter = RecyclerAdapter(bookInfo, this, R.layout.card_delete)
        recycler_cart_view.adapter = adapter
        recycler_cart_view.layoutManager = LinearLayoutManager(activity!!)
        recycler_cart_view.setHasFixedSize(true)

        view.findViewById<Button>(R.id.btn_pay).setOnClickListener(this)
    }

    override fun onItemClick(position: Int) {
        val currentItem = bookInfo[position]
        val (id, title, author, _, _, _) = currentItem

        val numDeleted = db.deleteBookData(id, title, author, username, password)
        if (numDeleted > 0) {
            bookInfo.removeAt(position).apply {
                // Update Total Price
                txv_totalAmount.text =
                    (Integer.parseInt(txv_totalAmount.text.toString()) - this.price).toString()
            }
            adapter.notifyItemRemoved(position)

            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "$title has been deleted to cart.",
                Snackbar.LENGTH_SHORT
            ).apply { this.setAction("Undo") { this.dismiss() }.show() }

        } else {
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "$title not deleted",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_pay -> {
                val removedCount = db.checkOut(username, password)
                bookInfo.clear()
                adapter.notifyItemRangeRemoved(0, removedCount)
                txv_totalAmount.text = "0"
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Checkout success",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    this.setAction("View Checkout") {
                        navController.navigate(
                            R.id.action_CartFragment_to_CheckoutFragment,
                            bundle
                        )
                        this.dismiss()
                    }.show()
                }
            }
        }
    }
}
