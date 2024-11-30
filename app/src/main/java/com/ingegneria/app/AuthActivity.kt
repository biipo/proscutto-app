package com.ingegneria.app

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ingegneria.app.authfragments.LoginFragment
import com.ingegneria.app.databinding.ActivityAuthBinding
import com.ingegneria.app.databinding.ActivityMainBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IF user already logged redirect to MainActivity

        setContentView(R.layout.activity_auth)

        // uses fragment's transaction to start the login fragment (if the user
        // don't have an account he has to click in the button below the LOGIN button)
        supportFragmentManager.beginTransaction()
            .add(R.id.auth_layout, LoginFragment()) // QUESTION: should 'disallowAddToBackStack()' be used here?
            .commit()
    }
}