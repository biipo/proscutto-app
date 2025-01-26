package com.ingegneria.app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ingegneria.app.models.Pet
import com.ingegneria.app.models.PetFirebaseSync

class PetViewModel : ViewModel() {
    var pet by mutableStateOf<Pet?>(null)
    var petFb by mutableStateOf<PetFirebaseSync?>(null)

    fun retrieveFirebaseData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = userId?.let {
            Firebase.database.getReference("characters")
                .child(it)
        }

        db?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                val charactersMap = mutableMapOf<String, Pet>()
                pet = ds.getValue(Pet::class.java)
                if (pet != null) {
                    println("Pet: ${pet.toString()}")
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
        if (db != null && pet != null) {
            petFb = PetFirebaseSync(db, pet!!)
        }
    }
}