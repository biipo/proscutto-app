package com.ingegneria.app.ui.otherpages

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SocialViewModel : ViewModel() {

    val database = FirebaseDatabase.getInstance().reference.child("friends")
    var userFriends = mutableStateListOf<String>()
    private lateinit var userId: String


    fun retrieveFirebaseData(userId: String) {
        this.userId = userId
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userFriends = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }.toMutableStateList()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Social", "Errore :( - ${error.message}")
            }
        })
    }

    fun addFriend(friendId: String) : Boolean {
        if (userFriends.contains(friendId)) {
            return false // The friend hasn't been added because already exists
        } else {
            userFriends.add(friendId)
            database.child(userId).setValue(userFriends)
            return true
        }
    }
}