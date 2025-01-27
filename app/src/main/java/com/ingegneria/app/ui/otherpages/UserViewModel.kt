package com.ingegneria.app.ui.otherpages

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {

    private lateinit var userA: FirebaseUser


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

    fun initializeAfterLogin(userId: String?) {
        if (userId == null) {
            throw IllegalArgumentException("There has been an error on login")
        }
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        firestore.collection("users")
            .document(userId)
            .update("lastLogin", FieldValue.serverTimestamp())
            .addOnFailureListener {
                throw IllegalArgumentException("There has been an error on login")
            }
    }

    fun initializeUserOnSignup(user: FirebaseUser) {
        val id = user.uid
        val firestore = FirebaseFirestore.getInstance()
        val db = Firebase.database
        val petDb = db.getReference("characters")
        val friendsDb = db.getReference("friends")
        val requestsDb = db.getReference("friendRequests")
        val shopDb = db.getReference("shop")
        petDb.child(id).setValue(
            mapOf(
               Pair("hat", ""),
               Pair("hp", 0),
               Pair("level", 1),
               Pair("mult", 0),
               Pair("name", 0),
               Pair("xp", 0)
            )
        )
        friendsDb.child(id).setValue(listOf(""))
        requestsDb.child(id).setValue(listOf(""))
        shopDb.child(id).setValue(listOf(""))

        val userData = hashMapOf(
            "userId" to id,
            "email" to user.email,
            "username" to user.displayName,
            "createdAt" to FieldValue.serverTimestamp(),
            "lastLogin" to FieldValue.serverTimestamp(),
            "dailyQuestionLimit" to 0,
            "lastDailyReset" to com.google.firebase.Timestamp.now(),
            "lastWeeklyReset" to com.google.firebase.Timestamp.now(),
            "lastMonthlyReset" to com.google.firebase.Timestamp.now(),
            "selectedDailyTasks" to emptyList<String>(),
            "selectedWeeklyTasks" to emptyList<String>(),
            "selectedMonthlyTasks" to emptyList<String>()
        )
        firestore.collection("users")
            .document(id ?: "")
            .set(userData)
            .addOnSuccessListener {
                val tasksData = mapOf(
                    "dailyTasks" to emptyList<Any>(),
                    "weeklyTasks" to emptyList<Any>(),
                    "monthlyTasks" to emptyList<Any>()
                )
                firestore.collection("users")
                    .document(id ?: "")
                    .collection("tasks")
                    .document("categories")
                    .set(tasksData)
            }
            .addOnFailureListener {
                throw IllegalArgumentException("Something went wrong while setting up your profile")
            }.addOnFailureListener {
                throw IllegalArgumentException("Something went wrong while setting up your profile")
            }
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
                        initializeUserOnSignup(user)
                    }
                }
            }.addOnFailureListener {
                throw IllegalArgumentException("Something went wrong, try again")
            }
    }

    private fun passwordCheck(password: String) : Boolean {
        return Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^!&+=]).{4,}$").matches(password)
    }

    private fun emailCheck(email: String) : Boolean {
        return Regex("[a-zA-Z0-9]*@[a-zA-Z]*.[a-z]*").matches(email)
    }
}