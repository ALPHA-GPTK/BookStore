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


class BookStoreFragment : Fragment(), View.OnClickListener, RecyclerView.Adapter() {

    lateinit var navController: NavController
    lateinit var recipient: String

    class ExampleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageview: ImageView = itemView.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        recipient = arguments!!.getString("recipient").toString()

    }

    public onCreateViewHolder()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
//        view.findViewById<Button>(R.id.send_btn).setOnClickListener(this)
//        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener(this)
//        val message = "Sending money to $recipient"
//        view.findViewById<TextView>(R.id.recipient).text = message
    }

    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.send_btn -> {
//                if (!TextUtils.isEmpty(input_amount.text.toString())) {
//                    val amount = input_amount.text.toString()
//                    val bundle = bundleOf(
//                        "recipient" to recipient,
//                        "amount" to amount
//                    )
//                    navController.navigate(
//                        R.id.action_BookStoreFragment_to_CartFragment,
//                        bundle
//                    )
//                } else {
//                    Toast.makeText(activity, "Enter an amount", Toast.LENGTH_SHORT).show()
//                }
//            }
//            R.id.cancel_btn -> activity!!.onBackPressed()
//        }
    }
}
