package com.example.bookstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_a_pi.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class APiFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        val tvBookTitle = view.findViewById<TextView>(R.id.txv_booktitle)
        val tvAuthor = view.findViewById<TextView>(R.id.txv_author)
        val tvNumPage = view.findViewById<TextView>(R.id.txv_numpages)

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
                        author = item.getJSONArray("authors")
                        numPages.add(item.getString("pageCount"))
                    }

                    tvBookTitle.text = title[0]
                    tvAuthor.text = author[0].toString()
                    tvNumPage.text = numPages[0]

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
                navController.navigate(R.id.action_APiFragment_to_CartFragment)
            }
        }
    }
}