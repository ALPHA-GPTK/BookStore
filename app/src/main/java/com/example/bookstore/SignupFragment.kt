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
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var handler: DatabaseHelper
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.sign_up).setOnClickListener(this)
        view.findViewById<TextView>(R.id.txv_exAccount).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        handler = DatabaseHelper(activity!!)
        when (v!!.id) {
            R.id.sign_up -> {
                handler.insertUserData(
                    activity!!,
                    input_name.text.toString(),
                    input_email.text.toString(),
                    input_username.text.toString(),
                    input_password.text.toString()
                ).show()

                navController.navigate(R.id.action_signupFragment_to_LoginFragment)
            }
            R.id.txv_exAccount -> navController.navigate(R.id.action_signupFragment_to_LoginFragment)
        }
    }

}