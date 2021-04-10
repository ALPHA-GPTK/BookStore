package com.example.bookstore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_book_store.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BookStoreFragment : Fragment() {
    lateinit var navController: NavController
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>

    lateinit var username: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments!!.getString("username").toString()
        password = arguments!!.getString("password").toString()
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cart -> {
                navController.navigate(R.id.action_BookStoreFragment_to_CartFragment)
            }
        }
        super.onOptionsItemSelected(item)
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_book_store, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = Navigation.findNavController(view)

        val title = mutableListOf<String>()
        val author = mutableListOf<String>()
        val numPages = mutableListOf<String>()
        val imageUrl = mutableListOf<String>()
        val price = mutableListOf<Int>()

        val tvBookTitle = view.findViewById<TextView>(R.id.item_title)

        val queue = Volley.newRequestQueue(activity!!)
        val url = "http://10.0.2.2/PHP_acad/FinalProject/"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val arrItems: JSONArray = jsonObject.getJSONArray("book")

                    for (i in 0 until arrItems.length()) {
                        val item: JSONObject = arrItems.getJSONObject(i)
                        title.add(item.getString("title"))
                        val getAuthors = item.getJSONArray("authors")
                        author.add(getAuthors[0] as String)
                        numPages.add(item.getString("pageCount"))
                        imageUrl.add(item.getString("thumbnailUrl"))
                        price.add((100..999).random())
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { tvBookTitle.text = "That didn't work!" })

        queue.add(stringRequest)

        layoutManager = LinearLayoutManager(activity!!)

        book_store_rv.layoutManager = layoutManager

        adapter = RecyclerAdapter(
            activity!!,
            title,
            author,
            numPages,
            imageUrl,
            price,
            username,
            password,
            navController,
            R.layout.card_item
        )
        book_store_rv.adapter = adapter
    }
}
