package com.ingegneria.app.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ingegneria.app.models.Pet
import com.ingegneria.app.models.PetFirebaseSync

class PetViewModel : ViewModel() {

    val database = FirebaseDatabase.getInstance().reference.child("characters")

    var pet by mutableStateOf<Pet?>(null)
    var petFb by mutableStateOf<PetFirebaseSync?>(null)

    fun retrieveFirebaseData(userId: String) {
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                val charactersMap = mutableMapOf<String, Pet>()
                pet = ds.getValue(Pet::class.java)
                if (pet != null) {
                    println("Pet: ${pet.toString()}")
                } else {
                    Log.e("RetrieveFirebaseData pet", "Something went wrong inside OnDataChange")
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("RetrieveFirebaseData pet", "Something went wrong, ${p0.message}")
            }
        })
        if (pet != null) {
            Log.e("Retrieving pet's information", "Successfully")
            petFb = PetFirebaseSync(database.child(userId), pet!!)
        }
    }
}