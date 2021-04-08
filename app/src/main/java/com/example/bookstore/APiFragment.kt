package com.example.bookstore

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import kotlinx.android.synthetic.main.fragment_shop_layout.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class APiFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var username: String
    lateinit var password: String
    lateinit var handler: DatabaseHelper
    val title = mutableListOf<String>()
    var author = JSONArray()
    val numPages = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments!!.getString("username").toString()
        password = arguments!!.getString("password").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_shop_layout, container, false)
        val myBooks = mutableListOf(
            BookOwner("Prince the Great", "Prince Velasco", "123"),
            BookOwner("The Adventures of Prince", "Prince Velasco", "321"),
            BookOwner("The Chronicles of Prince", "Prince Velasco", "89")
        )
        val customAdapter = CustomAdapter(activity!!, myBooks)
        rootView.book_items.adapter = customAdapter
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_pi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        //  API Code
        val title = mutableListOf<String>()
        var author = JSONArray()
        val numPages = mutableListOf<String>()
        var image: Drawable?

        val tvBookTitle = view.findViewById<TextView>(R.id.txv_booktitle)
        val tvAuthor = view.findViewById<TextView>(R.id.txv_author)
        val tvNumPage = view.findViewById<TextView>(R.id.txv_numpages)
        var ivBookImg = view.findViewById<ImageView>(R.id.ivw_book)

        // Instantiate the RequestQueue.
        val queue = newRequestQueue(activity!!)
        val url = "http://10.0.2.2/PHP_acad/FinalProject/"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val arrItems: JSONArray = jsonObject.getJSONArray("book")
                    for (i in 0 until arrItems.length()) {
                        val item: JSONObject = arrItems.getJSONObject(i)
                        title.add(item.getString("title"))
                        author = item.getJSONArray("authors")
                        numPages.add(item.getString("pageCount"))
//                        image = Drawable.createFromPath(item.getString("ivBookImg"))
                    }

                    tvBookTitle.text = title[0]
                    tvAuthor.text = author[0].toString()
                    tvNumPage.text = numPages[0]
                    val img =
                        Drawable.createFromPath("https://s3.amazonaws.com/AKIAJC5RLADLUMVRPFDQ.book-thumb-images/ableson2.jpg")
                    ivBookImg.setImageDrawable(img)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { tvBookTitle!!.text = "That didn't work!" })

        queue.add(stringRequest)
        view.findViewById<Button>(R.id.btn_add_cart).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_add_cart -> {
                handler = DatabaseHelper(activity!!)
                handler.insertBookData(
                    title[0],
                    author[0].toString(),
                    numPages[0],
                    username,
                    password
                )
                navController.navigate(R.id.action_APiFragment_to_CartFragment)
            }
        }
    }

    private class CustomAdapter(
        val context: Activity,
        val itemList: MutableList<BookOwner>
    ) :
        BaseAdapter() {
        private lateinit var bookTitle: TextView
        private lateinit var bookAuthor: TextView
        private lateinit var bookPages: TextView
        private lateinit var addCartBtn: ImageButton

        override fun getCount(): Int {
            return itemList.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder", "SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var customView = convertView
            customView =
                LayoutInflater.from(context).inflate(R.layout.fragment_a_pi, parent, false)
            bookTitle = customView.findViewById(R.id.txv_booktitle)
            bookAuthor = customView.findViewById(R.id.txv_author)
            bookPages = customView.findViewById(R.id.txv_numpages)

//            addCartBtn = customView.findViewById(R.id.btn_add_cart)

            bookTitle.text = itemList[position].bookTitle
            bookAuthor.text = itemList[position].author
            bookPages.text = itemList[position].Pages

//            addCartBtn.setOnClickListener {
//                itemList[position].quantity += 1
//                itemQuantity.text = itemList[position].quantity.toString()
//            }
            return customView
        }
    }
}

class BookOwner(val bookTitle: String, val author: String, val Pages: String)