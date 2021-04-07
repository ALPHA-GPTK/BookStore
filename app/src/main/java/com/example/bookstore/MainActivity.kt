package com.example.bookstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private val booksFragment = BookStoreFragment()
    private val cartFragment = CartFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        replaceFragment(booksFragment)
//
//        bottom_navigation.setOnNavigationItemSelectedListener{
//            when(it.itemId){
//                R.id.ic_books -> replaceFragment(booksFragment)
//                R.id.ic_cart -> replaceFragment(cartFragment)
//            }
//            true
//        }
    }

    //For navigating fragments -----
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}

















