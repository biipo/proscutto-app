package com.ingegneria.app.otherpages

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ShopViewModel : ViewModel() {

    // TODO: maybe set some fields private??

    val database = FirebaseDatabase.getInstance().reference.child("shop")
    var shopItems by mutableStateOf<List<String>>(emptyList())


    fun retrieveFirebaseData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shopItems = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Task", "Errore :( - ${error.message}")
            }
        })
    }
}