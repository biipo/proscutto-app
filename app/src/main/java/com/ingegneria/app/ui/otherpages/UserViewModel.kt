package com.ingegneria.app.ui.otherpages

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.database

class UserViewModel : ViewModel() {

    private lateinit var user: FirebaseUser


    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnFailureListener {
                    throw IllegalArgumentException("Wrong credentials")
                }
        } else {
            throw IllegalArgumentException("One or more fields are empty")
        }
    }

    private fun initializeUser() {
        val db = Firebase.database
        val petDb = db.getReference("characters")
        val friendsDb = db.getReference("friends")
        val requestsDb = db.getReference("friendRequests")
        val shopDb = db.getReference("shop")
    }

    fun signup(username: String, email: String, password: String, confirmPassword: String) {
        if (username.isEmpty() || email.isEmpty()
            || password.isEmpty() || confirmPassword.isEmpty()) {
            throw IllegalArgumentException("One or more fields are empty")
        }
        if (!emailCheck(email)) {
            throw IllegalArgumentException("Invalid email")
        }
        if (username.length > 12) {
            throw IllegalArgumentException("Username must be under 12 characters")
        }
        if (!Regex("[a-zA-Z0-9]*").matches(username)) {
            throw IllegalArgumentException("Username cannot contains symbols")
        }
        if (password != confirmPassword) {
            throw IllegalArgumentException("Password and confirmation password mismatch")
        }
        if(!passwordCheck(password)) {
            throw IllegalArgumentException("Invalid password; it must contains symbols, digits and capital letters")
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = FirebaseAuth.getInstance().currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }
                user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this.user = user
                    }
                }
            }.addOnFailureListener {
                throw IllegalArgumentException("Something went wrong, try again")
            }
        initializeUser()
    }

    private fun passwordCheck(password: String) : Boolean {
        return Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$").matches(password)
    }

    private fun emailCheck(email: String) : Boolean {
        return Regex("[a-zA-Z0-9]*@[a-zA-Z]*.[a-z]*").matches(email)
    }
}