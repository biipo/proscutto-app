package com.ingegneria.app.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

fun updateDb(dbRef: DatabaseReference, pet: Pet) {
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

data class Pet(
    // Stats
    var name: String = "",
    var level: Int = 1,
    var mult: Double = 1.0,
    var xp: Int = 0,
    var hp: Int = 0,
    // ID of the customization on the pet
    var hat: String? = null,
) {
    @Exclude
    fun toMap() : Map<String, Any?> {
        return mapOf(
            "name" to name,
            "level" to level,
            "mult" to mult,
            "xp" to xp,
            "hp" to hp,
            "hat" to hat,
        )
    }

    fun maxXp(): Int {
        return level * 15
    }

    fun maxHp(): Int {
        return level * 10
    }

    fun takeDamage(dmg: Int) {
        hp -= dmg
        if (hp < 0)
            hp = 0
    }

    fun heal(amount: Int) {
        hp += amount
        if (hp > maxHp())
            hp = maxHp()
    }

    fun gainXp(amount: Int) {
        xp += (amount*mult).toInt()

        val user = FirebaseAuth.getInstance().currentUser
        val firestore = FirebaseFirestore.getInstance()
        // If xp > max
        while (xp >= maxXp()) {
            xp -= maxXp()
            var hpRatio = hp / maxHp()
            level++;
            hp = maxHp() * hpRatio

            user?.let {
                val userRef = firestore.collection("users").document(it.uid)
                userRef.get().addOnSuccessListener { document ->
                    val maxLevelReached = document.getLong("maxLevelReached")?.toInt() ?: 1
                    if (level > maxLevelReached) {
                        userRef.set(mapOf("maxLevelReached" to level), SetOptions.merge())
                    }
                }
            }
        }
    }

    fun resetMult() {
        mult = 1.0
    }

    fun increaseMult(amount: Double) {
        mult += amount
    }

    fun decreaseMult(amount: Double) {
        mult -= amount
    }
}