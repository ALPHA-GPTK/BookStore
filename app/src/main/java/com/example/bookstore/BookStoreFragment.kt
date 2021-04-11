package com.example.bookstore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_book_store.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class BookStoreFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    private lateinit var username: String
    private lateinit var password: String
    private var bookInfo = mutableListOf<BookInfo>()

    private lateinit var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments!!.getString("username").toString()
        password = arguments!!.getString("password").toString()
        bundle = bundleOf("username" to username, "password" to password)
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_cart, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bundle = bundleOf("username" to username, "password" to password)
        when (item.itemId) {
            R.id.cart -> navController.navigate(
                R.id.action_BookStoreFragment_to_CartFragment,
                bundle
            )
            R.id.acb_btnLogout -> navController.navigate(R.id.action_BookStoreFragment_to_LoginFragment)
            R.id.acb_profile -> navController.navigate(R.id.action_BookStoreFragment_to_profileFragment)
        }
        return super.onOptionsItemSelected(item)
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
//        val container = activity!!.findViewById<FrameLayout>(R.id.container)
//        val rootView = LayoutInflater.from(activity!!).inflate(R.layout.card_item, container, false)
//        val btn = rootView.findViewById<Button>(R.id.btn_add)
//        btn.setBackgroundResource(R.color.red)

        val queue = Volley.newRequestQueue(activity!!)
        val url = "http://10.0.2.2/PHP_acad/FinalProject/"

        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    Log.i("details", "here")
                    val arrItems: JSONArray = jsonObject.getJSONArray("book")

                    for (i in 0 until arrItems.length()) {
                        val item: JSONObject = arrItems.getJSONObject(i)
                        bookInfo.add(
                            BookInfo(
                                item.getString("title"),
                                item.getJSONArray("authors")[0] as String,
                                item.getString("pageCount"),
                                item.getString("thumbnailUrl"),
                                (100..999).random()
                            )
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, {
                repeat(5) {
                    bookInfo.add(
                        BookInfo(
                            "That didn't work!",
                            "That didn't work!",
                            "That didn't work!",
                            "https://homepages.cae.wisc.edu/~ece533/images/",
                            0
                        )
                    )
                }
            })

        queue.add(stringRequest)

        adapter = RecyclerAdapter(bookInfo, this, R.layout.card_item)
        book_store_rv.adapter = adapter
        book_store_rv.layoutManager = LinearLayoutManager(activity!!)
        book_store_rv.setHasFixedSize(true)
    }

    override fun onItemClick(position: Int) {
        val currentItem = bookInfo[position]
        val db = DatabaseHelper(context!!)
        val (title, author, page, image, price) = currentItem
        val insertedBook =
            db.insertBookData(
                BookInfo(title, author, page, image, price), username, password
            )
        if (insertedBook) {
            adapter.notifyDataSetChanged()
            val snackBar = Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "$title has been added to cart.",
                Snackbar.LENGTH_LONG
            )
            snackBar.setAction("View Cart") {
                navController.navigate(
                    R.id.action_BookStoreFragment_to_CartFragment,
                    bundle
                )
                snackBar.dismiss()
            }.show()
        } else {
            Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "$title not added",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}

