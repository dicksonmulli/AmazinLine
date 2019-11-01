package com.amazinline.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.amazinline.AppExecutors
import com.amazinline.R
import com.amazinline.data.local.CustomerDataSource
import com.amazinline.data.local.ItemDb
import com.amazinline.data.local.LocalDataSource
import com.amazinline.data.model.Customer
import com.amazinline.data.repository.CustomerRepository
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.content_results.*

class ResultsActivity : AppCompatActivity() {

    // Declaration of auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        setSupportActionBar(toolbar)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        title = "Results"

        val repository = CustomerRepository.getInstance(LocalDataSource.getInstance(AppExecutors(), ItemDb.getInstance(this).customerDao()))

        auth.currentUser?.email?.let { repository.getCustomerByEmail(it, object: CustomerDataSource.GetCustomerCallback {
            override fun onCustomerLoaded(customer: Customer) {
                resultsTextView.text = "Results For: \n${customer.identityKeyword}"
            }

            override fun onDataNotAvailable() {
                resultsTextView.text = "Sorry your info is missing or we didn't find a match for you"
            }

        }) }

        fab.setOnClickListener { view ->
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
