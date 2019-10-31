package com.amazinline

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ArrayAdapter.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        private val TAG by lazy { "MainActivity" }
    }

    // Declaration of auth
    private lateinit var auth: FirebaseAuth

    private var shirtAdapter: ArrayAdapter<*>? = null
    private var pantAdapter: ArrayAdapter<*>? = null
    private var shoeAdapter: ArrayAdapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Create an ArrayAdapter using the string array and a default spinner layout
        shirtAdapter = createFromResource(
            this,
            R.array.sizes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            shirtSpinner.adapter = adapter
        }

        shirtSpinner.onItemSelectedListener = this
        pantAdapter = createFromResource(
                this,
        R.array.sizes,
        android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            pantSpinner.adapter = adapter
            pantSpinner.onItemSelectedListener = this
        }

        shoeAdapter = createFromResource(
            this,
            R.array.sizes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            shoeSpinner.adapter = adapter
            shoeSpinner.onItemSelectedListener = this
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // Inflate the menu
        menuInflater.inflate(R.menu.sign_out_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // noinspection SimplifiableIfStatement
        if (item.itemId == R.id.action_sign_out) run {
            signOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        when (parent?.id) {
            R.id.shirtSpinner -> {
                Log.e(TAG, "Nothing selected but the Shirt size is: " + parent?.adapter?.getItem(0))
            }
            R.id.pantSpinner -> {
                Log.e(TAG, "Nothing selected but the Pant size is: " + parent?.adapter?.getItem(0))
            }
            R.id.shoeSpinner -> {
                Log.e(TAG, "Nothing selected but the Shoe size is: " + parent?.adapter?.getItem(0))
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.shirtSpinner -> {
                Log.e(TAG, "Shirt size: " + parent?.getItemAtPosition(position))
            }
            R.id.pantSpinner -> {
                Log.e(TAG, "Pant size: " + parent?.getItemAtPosition(position))
            }
            R.id.shoeSpinner -> {
                Log.e(TAG, "Shoe size: " + parent?.getItemAtPosition(position))
            }
        }
    }

    /**
     * Singout
     */
    private fun signOut() {
        // Firebase sign out
        auth.signOut()
        onBackPressed()
    }

    override fun onBackPressed() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "You have Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
        }
        finish()
        super.onBackPressed()
    }
}
