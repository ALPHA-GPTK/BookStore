package com.example.bookstore

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_book_store.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class BookStoreFragment : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var recipient: String

    class ExampleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {   //Example sa youtube
//        val imageview: ImageView = itemView.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_book_store)

        //  API Code
        val title = mutableListOf<String>() //  API Book Title
        val author = mutableListOf<String>() //  API Book Author
        val numpages = mutableListOf<String>() //  API Number of Pages

        val textView_Result = findViewById<TextView>(R.id.txv_result)

// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "http:/10.0.2.2/FinalProject/"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
//                textView.text = response.toString()
                Log.i("test", "test")
                try {
                    val json_obj: JSONObject = JSONObject(response.toString())
                    val arr_items: JSONArray = json_obj.getJSONArray("book")
                    for (i in 0..arr_items.length() - 1) {
                        val item: JSONObject = arr_items.getJSONObject(i)
                        val item_title = item.getString("title")    //  Get Book Title
                        val item_author = item.getString("authors")[0]  //  Get Author
                        val item_numpage = item.getString("pageCount")  //  Get Book Number of Page

                        title.add(item_title)   // Add item to mutablelist title
                        author.add(item_author.toString())
                        numpages.add(item_numpage)  //  Add num page to num page mutable list
                    }

//                    textView.text = response.toString()
                    textView_Result.text = title[0]

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { textView_Result.text = "That didn't work!" })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}