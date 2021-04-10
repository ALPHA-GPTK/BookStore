package com.example.bookstore

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

//    private val booksFragment = BookStoreFragment()
//    private val cartFragment = CartFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Action bar with logo
        supportActionBar?.apply {
            title = "\tAlpha Bookstore"
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(R.drawable.alpha_logo_white)
        }

        fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_menu,menu)
            return true
        }

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            return if (id == R.id.add_action){
                Toast.makeText(this, "item Add Clicked", Toast.LENGTH_SHORT).show()
                true
            }else{
                Toast.makeText(this,"Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            return super.onOptionsItemSelected(item)
        }


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
//    private fun replaceFragment(fragment: Fragment) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container, fragment)
//        transaction.commit()
//    }
}

















