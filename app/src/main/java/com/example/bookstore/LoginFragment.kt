package com.example.bookstore

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var db: DatabaseHelper

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
        db = DatabaseHelper(activity!!)

        view.findViewById<Button>(R.id.btn_login).setOnClickListener(this)
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener(this)
        view.findViewById<TextView>(R.id.txv_crtAccount).setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_login -> {
                val inputMethodManager =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager   //  Hide keyword when button clicked

                val username = inp_loginusername.text.toString()
                val password = inp_loginpass.text.toString()
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    val bundle = bundleOf("username" to username, "password" to password)

                    if (db.isExists("user", username, password)) {
                        Toast.makeText(
                            activity!!,
                            "Successfully Login",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(
                            R.id.action_LoginFragment_to_BookStoreFragment,
                            bundle
                        )
                        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)    //  Hide keyword when button clicked
                    } else {
                        Toast.makeText(
                            activity!!,
                            "Login Failed.\nCreate an account first.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(activity!!, "Enter a username", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity!!, "Enter a password", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.cancel_btn -> activity!!.onBackPressed()
            R.id.txv_crtAccount -> navController.navigate(R.id.action_LoginFragment_to_signupFragment)
        }
    }
}
