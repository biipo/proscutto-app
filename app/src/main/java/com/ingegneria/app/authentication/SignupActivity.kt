package com.ingegneria.app.authentication

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ingegneria.app.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.authComponents.pageTitle.text = "Signup"

        val username = binding.authComponents.usernameTextbox.text

        val password = binding.authComponents.passwordTextbox.text
        val confirmPassword = binding.signupConfirmPasswordTextbox.text

        val authButton = binding.authComponents.authButton
        authButton.text = "SIGNUP"
        authButton.setOnClickListener { view ->
            val sUsername = username.toString()
            val sPassword = password.toString()
            val sConfirmPassword = confirmPassword.toString()
            if (!inputCheck(sUsername, sPassword, sConfirmPassword, view)) {
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(sUsername, sPassword)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        finish()
                    } else {
                        Snackbar.make(view, "Signup error", Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }
        }

        val otherAuthText = binding.authComponents.otherAuthText
        otherAuthText.text = "Already have an account? Login!"
        otherAuthText.setOnClickListener {
            finish() // we got back to login, to be sure the account has been created in the db
        }
    }

    private fun inputCheck(sUsername: String?, sPassword: String?, sConfirmPassword: String?, view: View): Boolean {
        if(TextUtils.isEmpty(sUsername)) {
            Snackbar.make(view, "Missing username", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(sPassword)) {
            Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if(TextUtils.isEmpty(sConfirmPassword)) {
            Snackbar.make(view, "Missing confirmation password", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if(sPassword != sConfirmPassword) {
            Snackbar.make(view, "Password and confirmation password do not match", Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}