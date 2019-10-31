package com.amazinline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG by lazy { "SignInActivity" }
    }

    // Declaration of auth
    private lateinit var auth: FirebaseAuth

    private var mUserName = ""
    private var mPassword = ""
    private var mOnLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        signInButton.setOnClickListener(this)
        switchLoginTextView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signInButton -> attemptLoginIn()
            R.id.switchLoginTextView -> switchLogIn()
        }
    }

    /**
     * Method to switch from login to sign_up and vice versa
     */
    private fun switchLogIn() {
        if (mOnLogin) {
            logInTextView.text = getString(R.string.signup)
            switchLoginTextView.text = getString(R.string.switch_to_login)
            welcomeTitle.text = getString(R.string.welcome)
            mOnLogin = false
        } else {
            logInTextView.text = getString(R.string.login)
            switchLoginTextView.text = getString(R.string.switch_to_sign_up)
            welcomeTitle.text = getString(R.string.welcome_back)
            mOnLogin = true
        }
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.e(TAG, "currentUser ${currentUser?.email}")
        updateUI(currentUser, true)
    }

    private fun firebaseAuthWithEmail() {
        auth.signInWithEmailAndPassword(mUserName, mPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information and go to the next page
                    val user = auth.currentUser
                    updateUI(user, true)
                } else {

                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    val toastMessage = task.exception?.message
                    if (toastMessage.isNullOrEmpty() || toastMessage.contains("user may have been deleted")) {
                        Toast.makeText(
                            this,
                            "Email you entered is incorrect. Try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                    }
                    updateUI(null, false)
                }

                hideProgressDialog()
            }
    }

    /**
     * Signin or sign up
     */
    private fun attemptLoginIn() {
        if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
            mUserName = emailEditText.text.toString()
            mPassword = passwordEditText.text.toString()

            progressBar.visibility = View.VISIBLE
            if (mOnLogin) {
                firebaseAuthWithEmail()
            } else {
                firebaseSignUpWithEmail()
            }
        } else {
            Toast.makeText(this, "Username or password cannot be empty!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Create a new account
     */
    private fun firebaseSignUpWithEmail() {
        auth.createUserWithEmailAndPassword(mUserName, mPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update
                    // UI with the signed-in user's information and go to the next page
                    val user = auth.currentUser
                    updateUI(user, true)
                } else {

                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signUpWithCredential:failure", task.exception)
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    updateUI(null, false)
                }

                hideProgressDialog()
            }
    }

    private fun updateUI(user: FirebaseUser?, changeWelcomeTitle: Boolean) {
        hideProgressDialog()
        if (user != null) {

            if (changeWelcomeTitle) {
                welcomeTitle.text = getString(R.string.welcome_back)
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Log.e(TAG, "User null")
        }
    }

    private fun hideProgressDialog() {
        progressBar.visibility = View.INVISIBLE
    }
}
