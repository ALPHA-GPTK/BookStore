package com.example.bookstore

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var handler: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.next_btn).setOnClickListener(this)
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener(this)
        view.findViewById<TextView>(R.id.txv_crtAccount).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        handler = DatabaseHelper(activity!!)
        when (v!!.id) {
            R.id.next_btn -> {
                if (!TextUtils.isEmpty(input_username.text.toString())) {
                    val bundle = bundleOf(
                        "username" to input_username.text.toString(),
                        "password" to input_password.text.toString()
                    )
                    if (handler.isExists(
                            "user", input_username.text.toString(),
                            input_password.text.toString()
                        )
                    ) {
                        Toast.makeText(activity!!, "Successfully Login", Toast.LENGTH_SHORT).show()
                        navController.navigate(
                            R.id.action_LoginFragment_to_BookStoreFragment,
                            bundle
                        )
                    } else {
                        Toast.makeText(activity!!, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Enter a name", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.cancel_btn -> activity!!.onBackPressed()
            R.id.txv_crtAccount -> navController.navigate(R.id.action_LoginFragment_to_signupFragment)
        }
    }
}
