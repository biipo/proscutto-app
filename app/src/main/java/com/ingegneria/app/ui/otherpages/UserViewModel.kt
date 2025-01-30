package com.ingegneria.app.ui.otherpages

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel : ViewModel() {

    fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authRes ->
                    initializeAfterLogin(authRes.user?.uid)
                }.addOnFailureListener {
                    throw IllegalArgumentException("Wrong credentials")
                }
        } else {
            throw IllegalArgumentException("One or more fields are empty")
        }
    }

    private fun initializeAfterLogin(userId: String?) {
        if (userId == null) {
            throw IllegalArgumentException("There has been an error on login")
        }
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users")
            .document(userId)
            .update("lastLogin", FieldValue.serverTimestamp())
            .addOnFailureListener {
                throw IllegalArgumentException("There has been an error on login")
            }
    }

    private fun initializeUserOnSignup(user: FirebaseUser) {
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
               Pair("hp", 10),
               Pair("level", 1),
               Pair("mult", 1.0),
               Pair("name", ""),
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
            "selectedDailyTasks" to emptyMap<String, Boolean>(),
            "selectedWeeklyTasks" to emptyMap<String, Boolean>(),
            "selectedMonthlyTasks" to emptyMap<String, Boolean>(),
            "maxLevelReached" to 1,              // livello iniziale
            "dailyTasksCompleted" to 0,         // numero di task daily completate (totali)
            "weeklyTasksCompleted" to 0,        // numero di task weekly completate (totali)
            "monthlyTasksCompleted" to 0,       // numero di task monthly completate (totali)
            "quizCompleted" to 0,              // quanti quiz completati
            "totalLoginDays" to 1              // conteggio di giorni in cui si è loggato (parte da 1, ad es.)

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
            }.addOnFailureListener { e ->
                throw IllegalArgumentException("Something went wrong, error: ${e.message}")
            }
    }

    private fun passwordCheck(password: String) : Boolean {
        return Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^!&+=]).{4,}$").matches(password)
    }

    private fun emailCheck(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}