package com.ingegneria.app.ui.otherpages

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ShopViewModel : ViewModel() {

    val database = FirebaseDatabase.getInstance().reference.child("shop")
    var shopItems by mutableStateOf<List<String>>(emptyList())
    var userItems = mutableStateListOf<String>()
    lateinit var userId: String
        private set
    lateinit var username: String
        private set

    fun retrieveFirebaseData(userId: String, username: String) {
        this.userId = userId
        this.username = username
        database.child("shopItems").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shopItems = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Shop", "Errore :( - ${error.message}")
            }
        })
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userItems = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }.toMutableStateList()
                userItems.remove("") // Because we cannot have an empty branch on firebase we put a tmp empty string
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("UserItems", "Errore :( - ${error.message}")
            }
        })
    }

    fun buyItem(item: String) : Boolean {
        if (userItems.contains(item)) {
            return false // The item hasn't been purchased because the user already owns it
        } else {
            userItems.add(item)
            database.child(userId).setValue(userItems)
            return true
        }
    }
}