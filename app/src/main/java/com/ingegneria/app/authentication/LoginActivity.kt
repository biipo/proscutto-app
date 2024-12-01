package com.ingegneria.app.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ingegneria.app.databinding.ActivityLoginBinding

/* How authentication is handled:
*  MainActivity (check if logged) --no--> AuthActivity(Login) --if no account --> SignupFragment
*                                                ^                                       |
*                                                ^------------back to--------------------|
* We send the user back to login after registration, so we're sure the account has been added in the db
* */
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

        // USERNAME field
        val username = binding.authComponents.usernameTextbox.text

        // PASSWORD field
        val password = binding.authComponents.passwordTextbox.text

        // LOGIN BUTTOn
        val authButton = binding.authComponents.authButton
        authButton.text = "LOGIN"
        authButton.setOnClickListener { view ->
            val sUsername: String? = username as? String
            val sPassword: String? = password as? String
            if(sUsername != null && sUsername.isBlank()) {
                Snackbar.make(view, "Missing username", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (sUsername == null) {
                return@setOnClickListener
            }

            if(sPassword != null && sPassword.isBlank()) {
                Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (sPassword == null) {
                return@setOnClickListener
            }
            // tmp solution, just check that the button works
            finish()
        }

        // LINK -> SIGNUP
        val otherAuthText = binding.authComponents.otherAuthText
        otherAuthText.text = "Don't have an account? Signup!"
        otherAuthText.setOnClickListener {
            startActivity(Intent(applicationContext, SignupActivity::class.java))
        }
    }
}