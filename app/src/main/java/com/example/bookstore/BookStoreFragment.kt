package com.example.bookstore

import android.os.Bundle
import android.text.TextUtils
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

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {   //Example sa youtube
//        val imageview: ImageView = itemView.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  API Code
        val title = mutableListOf<String>() //  API Book Title
        val author = mutableListOf<String>() //  API Book Author
        val numpages = mutableListOf<String>() //  API Number of Pages

        val textView = findViewById<TextView>(R.id.error_code)

// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://jsonformatter.org/json-viewer/999f29"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
//                textView.text = response.toString()

                    try {
                        val json_obj: JSONObject = JSONObject(response.toString())
                        var arr_items: JSONArray = json_obj.getJSONArray("book")
                        for (i in 0..arr_items.length() - 1) {
                            var item: JSONObject = arr_items.getJSONObject(i)
                            var item_title = item.getString("title")
                            title.add(item_title)   // Add item to mutablelist title
                            // wala pa book author and pages
                        }

//                    textView.text = response.toString()
                        textView.text = title.toString()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { textView.text = "That didn't work!" })

    }
}