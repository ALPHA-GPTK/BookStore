package com.example.bookstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookstore.ui.main.AccountFragment2

class AccountFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountFragment2.newInstance())
                .commitNow()
        }
    }
}