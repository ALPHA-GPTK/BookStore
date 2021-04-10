package com.example.bookstore


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment() {

    private lateinit var username: String
    private lateinit var password: String

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

        val bookInfo = handler.getBookData(username, password)
        txv_totalAmount.text = bookInfo.sumBy { it.price }.toString()
        adapter = RecyclerAdapter(
            activity!!,
            bookInfo,
            username,
            password,
            navController,
            R.layout.card_delete
        )
        recycler_cart_view.adapter = adapter
        recycler_cart_view.layoutManager = LinearLayoutManager(activity!!)
        recycler_cart_view.setHasFixedSize(true)
    }
}
