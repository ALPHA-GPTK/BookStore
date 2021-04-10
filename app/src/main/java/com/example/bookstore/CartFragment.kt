package com.example.bookstore


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var bookInfo: MutableList<BookInfo>

    private lateinit var handler: DatabaseHelper
    private lateinit var navController: NavController
    private lateinit var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments!!.getString("username").toString()
        password = arguments!!.getString("password").toString()
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
        handler = DatabaseHelper(activity!!)

        bookInfo = handler.getBookData(username, password)
        txv_totalAmount.text = bookInfo.sumBy { it.price }.toString()
        adapter = RecyclerAdapter(
            bookInfo,
            this,
            R.layout.card_delete,
        )
        recycler_cart_view.adapter = adapter
        recycler_cart_view.layoutManager = LinearLayoutManager(activity!!)
        recycler_cart_view.setHasFixedSize(true)

        btn_pay.setOnClickListener {
            val snackBar = Snackbar.make(
                it,
                "Checkout success",
                Snackbar.LENGTH_LONG
            )

            val bundle = bundleOf("username" to username, "password" to password)

            snackBar.setAction("View Checkout") {
                navController.navigate(
                    R.id.action_CartFragment_to_CheckoutFragment,
                    bundle
                )
                snackBar.dismiss()
            }.show()
        }
    }

    override fun onItemClick(position: Int) {
        val currentItem = bookInfo[position]
        val db = DatabaseHelper(activity!!)
        val (title, author, _, _, _) = currentItem

        if (db.deleteBookData(title, author, username, password)) {
            adapter.notifyItemRemoved(position)
            val snackBar = Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "$title has been deleted to cart.",
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction("Undo") { snackBar.dismiss() }.show()
        } else {
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "$title not deleted",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}
