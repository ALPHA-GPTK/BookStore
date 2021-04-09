package com.example.bookstore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_a_pi.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class APiFragment : Fragment(), View.OnClickListener, AdapterView.OnItemClickListener {
    lateinit var navController: NavController
    lateinit var username: String
    lateinit var password: String

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_a_pi, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = Navigation.findNavController(view)
        //  API Code
        val title = mutableListOf<String>()
        val author = mutableListOf<String>()
        val numPages = mutableListOf<String>()
        val imageUrl = mutableListOf<String>()

        val tvBookTitle = view.findViewById<TextView>(R.id.item_title)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(activity!!)
        val url = "http://10.0.2.2/PHP_acad/FinalProject/"

        // Request a string response from the provided URL.
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
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { tvBookTitle.text = "That didn't work!" })

        queue.add(stringRequest)

        layoutManager = LinearLayoutManager(activity!!)

        lsw_card.layoutManager = layoutManager

        adapter = RecyclerAdapter(
            activity!!,
            title,
            author,
            numPages,
            imageUrl,
            username,
            password,
            navController
        )
        lsw_card.adapter = adapter
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_add -> {
                val bundle = bundleOf("username" to username, "password" to password)
                navController.navigate(R.id.action_APiFragment_to_CartFragment, bundle)
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}
