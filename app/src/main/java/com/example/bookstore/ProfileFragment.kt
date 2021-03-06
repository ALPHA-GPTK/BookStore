package com.example.bookstore

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var username: String
    private lateinit var password: String

    private lateinit var db: DatabaseHelper
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments!!.getString("username").toString()
        password = arguments!!.getString("password").toString()
        bundle = bundleOf("username" to username, "password" to password)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseHelper(activity!!)
        navController = Navigation.findNavController(view)

        bottom_nav.setupWithNavController(navController)
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId, bundle)
            true
        }

        val (name, dbUsername, email) = db.getUserData(username, password)

        inp_name.text = Editable.Factory.getInstance().newEditable(name)
        inp_username.text = Editable.Factory.getInstance().newEditable(dbUsername)
        inp_email.text = Editable.Factory.getInstance().newEditable(email)

        view.findViewById<Button>(R.id.btn_update).setOnClickListener(this)
        view.findViewById<Button>(R.id.btn_back).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val inputManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager   //  Hide keyword when button clicked
        bundle = bundleOf("username" to username, "password" to password)
        when (v!!.id) {
            R.id.btn_update -> {
                val updatedData = db.updateUserData(
                    inp_name.text.toString(),
                    inp_username.text.toString(),
                    inp_email.text.toString(),
                    username,
                    password
                )
                if (updatedData.name != "") {
                    val (_, updatedUsername, _) = updatedData
                    username = updatedUsername
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "user $username has been updated.",
                        Snackbar.LENGTH_SHORT
                    ).apply { this.setAction("Undo") { this.dismiss() }.show() }
                } else {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "user $username has not been updated.",
                        Snackbar.LENGTH_SHORT
                    )
                }

                inputManager.hideSoftInputFromWindow(
                    v.windowToken,
                    0
                )  //  Hide keyword when button clicked
            }
            R.id.btn_back -> navController.navigate(
                R.id.action_profileFragment_to_BookStoreFragment,
                bundle
            )
        }
    }
}