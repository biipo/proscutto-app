package com.ingegneria.app.authentication

import android.os.Bundle
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
            val sUsername: String? = username as? String
            val sPassword: String? = password as? String
            val sConfirmPassword: String? = confirmPassword as? String
            if(sUsername != null && sUsername.isBlank()) {
                Snackbar.make(view, "Missing username", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(sPassword != null && sPassword.isBlank()){
                Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(sConfirmPassword != null && sConfirmPassword.isBlank()){
                Snackbar.make(view, "Missing confirmation password", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(sPassword != sConfirmPassword) {
                Snackbar.make(view, "Password and confirmation password do not match", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            finish()
        }

        val otherAuthText = binding.authComponents.otherAuthText
        otherAuthText.text = "Already have an account? Login!"
        otherAuthText.setOnClickListener {
            finish() // because the only way to access the registration page is by passing through the login page
        }
    }
}