package com.amazinline.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ArrayAdapter.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazinline.AppExecutors
import com.amazinline.R
import com.amazinline.data.local.ItemDb
import com.amazinline.data.local.LocalDataSource
import com.amazinline.data.repository.CustomerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.RadioButton
import com.amazinline.data.model.Customer


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    companion object {
        private val TAG by lazy { "MainActivity" }
    }

    // Declaration of auth
    private lateinit var auth: FirebaseAuth

    // Adapters
    private var shirtAdapter: ArrayAdapter<*>? = null
    private var pantAdapter: ArrayAdapter<*>? = null
    private var shoeAdapter: ArrayAdapter<*>? = null
    private var depositAdapter: ArrayAdapter<*>? = null

    private var mIdentityKeyword: String = ""
    private var mDescription: String = ""
    private var mShirtNeckSize: String = ""
    private var mShirtArmLength: String = ""
    private var mShirtSize: String = ""
    private var mPantWaistSize: String = ""
    private var mPantInseam: String = ""
    private var mPantSize: String = ""
    private var mShoeSize: String = ""

    private lateinit var mRepository: CustomerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        mRepository = CustomerRepository.getInstance(LocalDataSource.getInstance(AppExecutors(), ItemDb.getInstance(this).customerDao()))

        initalizeAdapters()
        keywordsRadioGroup.setOnCheckedChangeListener(this)
        descriptionRadioGroup.setOnCheckedChangeListener(this)
        saveButton.setOnClickListener {
            attemptSaving()
        }
    }

    private fun initalizeAdapters() {
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
            shirtSpinner.onItemSelectedListener = this
        }

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
            R.array.shoe_size,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            shoeSpinner.adapter = adapter
            shoeSpinner.onItemSelectedListener = this
        }

        depositAdapter = createFromResource(
            this,
            R.array.shoe_size,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerDepositFrom.adapter = adapter
            spinnerDepositFrom.onItemSelectedListener = this
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
                Log.e(TAG, "Nothing selected but the Shirt size is: " + parent.getItemAtPosition(0))
                mShirtSize = parent.getItemAtPosition(0) as String
            }
            R.id.pantSpinner -> {
                Log.e(TAG, "Nothing selected but the Pant size is: " + parent.getItemAtPosition(0))
                mPantSize = parent.getItemAtPosition(0) as String
            }
            R.id.shoeSpinner -> {
                Log.e(TAG, "Nothing selected but the Shoe size is: " + parent.getItemAtPosition(0))
                mShoeSize = parent.getItemAtPosition(0) as String
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.shirtSpinner -> {
                Log.e(TAG, "Shirt size: " + parent.getItemAtPosition(position))
                mShirtSize = parent.getItemAtPosition(position) as String
            }
            R.id.pantSpinner -> {
                Log.e(TAG, "Pant size: " + parent.getItemAtPosition(position))
                mPantSize = parent.getItemAtPosition(position) as String
            }
            R.id.shoeSpinner -> {
                Log.e(TAG, "Shoe size: " + parent.getItemAtPosition(position))
                mShoeSize = parent.getItemAtPosition(position) as String
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        Log.e(TAG, "mDescription: $mIdentityKeyword")
        when(group?.id) {
            R.id.descriptionRadioGroup -> {
                val descriptionRadioButtonId = descriptionRadioGroup.checkedRadioButtonId
                val radioButton = descriptionRadioGroup.findViewById(descriptionRadioButtonId) as RadioButton
                mDescription = radioButton.getText() as String
                Log.e(TAG, "mDescription: $mDescription")
            }
            R.id.keywordsRadioGroup -> {
                val keywordsRadioButtonId = keywordsRadioGroup.checkedRadioButtonId
                val radioButton = keywordsRadioGroup.findViewById(keywordsRadioButtonId) as RadioButton
                mIdentityKeyword = radioButton.getText() as String
                Log.e(TAG, "mDescription: $mIdentityKeyword")
            }
        }
    }

    /**
     * Do a check and save customer data to database
     */
    private fun attemptSaving() {
        mShirtNeckSize = neckSizeEditText.text.toString()
        mShirtArmLength = armSizeEditText.text.toString()
        mPantWaistSize = waistSizeEditText.text.toString()
        mPantInseam = inseamEditText.text.toString()

        var email = auth.currentUser?.email

        mRepository.saveCustomer(
            Customer(
                0,
                email,
            mIdentityKeyword,
            mDescription,
            mShirtNeckSize,
            mShirtArmLength,
            mShirtSize,
            mPantWaistSize,
            mPantInseam,
            mPantSize,
            mShoeSize
        ))

        startActivity(Intent(this, ResultsActivity::class.java))
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
