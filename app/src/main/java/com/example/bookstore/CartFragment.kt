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
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(), RecyclerAdapter.OnItemClickListener, View.OnClickListener {
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var bookList: MutableList<BookInfo>

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

        bottom_nav.setupWithNavController(navController)
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId, bundle)
            true
        }

        db = DatabaseHelper(activity!!)
        bookList = db.getBookData(username, password)

        txv_totalAmount.text = bookList.sumBy { it.price }.toString() // Calculate Total Price

        adapter = initRecycler(activity!!, bookList, this, recycler_cart_view, R.layout.card_delete)

        view.findViewById<Button>(R.id.btn_pay).setOnClickListener(this)
    }

    override fun onItemClick(position: Int) {
        val currentItem = bookList[position]
        val (id, title, author, _, _, _) = currentItem

        val numDeleted = db.deleteBookData(id, title, author, username, password)
        if (numDeleted > 0) {
            bookList.removeAt(position).apply {
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
                bookList.clear()
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
