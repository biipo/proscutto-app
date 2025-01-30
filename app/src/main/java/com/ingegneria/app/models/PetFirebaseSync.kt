package com.ingegneria.app.models

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.ingegneria.app.R

fun getHatId(hat : String) : String? {
    val hatIdMap: Map<String, String> = mapOf (
        "Witch Hat" to "hat_witch",
        "Santa Hat" to "hat_santa_beard",
    )
    return hatIdMap[hat] ?: ""
}

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
                Log.d("Firebase PET", "Character updated successfully")
            }
            .addOnFailureListener { error ->
                // Update failed
                Log.e("Firebase PET", "Error updating character: ${error.message}")
            }
    }

    fun setHat(hat: String) {
        pet.hat = getHatId(hat)
        updateDb()
    }

    fun changeName(newName: String) {
        pet.name = newName
        updateDb()
    }
}