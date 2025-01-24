package com.ingegneria.app.models

import android.util.Log
import com.google.firebase.database.DatabaseReference

class PetFirebaseSync(
    private var dbRef: DatabaseReference,
    private var pet: Pet
) {
    fun takeDamage(dmg: Int) {
        pet.takeDamage(dmg)
        updateDb()
    }

    fun heal(amount: Int) {
        pet.heal(amount) // Call the method in Pet
        updateDb() // Update the database
    }

    fun gainXp(amount: Int) {
        pet.gainXp(amount) // Call the method in Pet
        updateDb() // Update the database
    }

    fun resetMult() {
        pet.resetMult() // Call the method in Pet
        updateDb() // Update the database
    }

    fun increaseMult(amount: Double) {
        pet.increaseMult(amount) // Call the method in Pet
        updateDb() // Update the database
    }

    fun decreaseMult(amount: Double) {
        pet.decreaseMult(amount) // Call the method in Pet
        updateDb() // Update the database
    }

    private fun updateDb() {
        dbRef.setValue(pet)
            .addOnSuccessListener {
                // Update successful
                Log.d("Firebase", "Character updated successfully")
            }
            .addOnFailureListener { error ->
                // Update failed
                Log.e("Firebase", "Error updating character: ${error.message}")
            }
    }

}