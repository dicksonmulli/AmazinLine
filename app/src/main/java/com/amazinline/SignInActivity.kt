package com.amazinline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    companion object {
        private val TAG by lazy { "SignInActivity" }
    }

    // Declaration of auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        configureGoogle()

        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun configureGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.e(TAG, "currentUser ${currentUser?.email}")
        updateUI(currentUser)
    }

    private fun firebaseAuthWithEmail() {
        auth.signInWithEmailAndPassword("username@gmail.com", "passwor")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information and go to the next page
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication Failed. Try again", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                hideProgressDialog()
            }
    }

    /**
     * Singin
     */
    private fun signIn() {
        firebaseAuthWithEmail()
        //firebaseSignUpWithEmail()
    }

    private fun firebaseSignUpWithEmail() {
        auth.createUserWithEmailAndPassword("username@gmail.com", "password")
    }

//    /**
//     * Singout
//     */
//    private fun signOut() {
//        // Firebase sign out
//        auth.signOut()
//    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {

            // Save user details on prefs
//            status.text = getString(R.string.google_status_fmt, user.email)
//            detail.text = getString(R.string.firebase_status_fmt, user.uid)
            //startActivity(Intent(this, MainActivity::class.java))
        } else {
            Log.e(TAG, "User not null")
        }
    }

    private fun hideProgressDialog() {
    }
}
