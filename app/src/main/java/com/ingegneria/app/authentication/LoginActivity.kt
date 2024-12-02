package com.ingegneria.app.authentication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ingegneria.app.MainActivity
import com.ingegneria.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: implement authentication
        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Because we've used the "include" in the xml we have to use nested findViewById
        // TITLE
        binding.authComponents.pageTitle.text = "Login"

        // EMAIL field
        val email = binding.authComponents.emailTextbox.text

        // PASSWORD field
        val password = binding.authComponents.passwordTextbox.text

        // LOGIN BUTTON
        val authButton = binding.authComponents.authButton
        authButton.text = "LOGIN"
        authButton.setOnClickListener { view ->
            val sEmail = email.toString()
            val sPassword = password.toString()
            if (!inputCheck(sEmail, sPassword, view)) {
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish() // removes this activity from the backStack
                    } else {
                        Snackbar.make(view, "Login error", Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }
        }

        // LINK -> SIGNUP
        val otherAuthText = binding.authComponents.otherAuthText
        otherAuthText.text = "Don't have an account? Signup!"
        otherAuthText.setOnClickListener {
            startActivity(Intent(applicationContext, SignupActivity::class.java))
            // the finish is not called because the user will return to the login
            // after registration, so we need to keep this activity in the backStack
        }
    }

    private fun inputCheck(sEmail: String?, sPassword: String?, view: View): Boolean {
        if(TextUtils.isEmpty(sEmail)) {
            Snackbar.make(view, "Missing email", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if(TextUtils.isEmpty(sPassword)) {
            Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()

        val currUser = auth.currentUser
        if(currUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}