package com.ingegneria.app.ui.otherpages

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database


class SocialViewModel : ViewModel() {

    val friendsDatabase = Firebase.database.getReference("friends")
    val requestsDatabase = Firebase.database.getReference("friendRequests")

    var userFriends = mutableStateListOf<String>() // List of friends of the current user
    var userFriendRequests = mutableStateMapOf<String, String>() // List of requests received by the current user
    lateinit var userId: String
        private set
    lateinit var username: String
        private set

    fun retrieveFirebaseData(userId: String, username: String) {
        this.userId = userId
        this.username = username
        friendsDatabase.child(userId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.value.toString()
                if (value.isNotEmpty() && !userFriends.contains(value)) {
                    userFriends.add(value)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("User friends", "child changed")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                userFriends.remove(snapshot.value)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("User friends", "child moved")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("User friends", "Error - ${error.message}")
            }

        })

        requestsDatabase.child(userId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key.toString()
                val value = snapshot.value.toString()
                if (key.isNotEmpty() && value.isNotEmpty()) {
                    userFriendRequests.set(key, value)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key.toString()
                val value = snapshot.value.toString()
                if (key.isNotEmpty() && value.isNotEmpty()) {
                    userFriendRequests.set(key, value)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                userFriendRequests.remove(snapshot.key)
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.e("FriendRequest", "Moved ${snapshot.value}")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FriendRequestError", "Error - ${error.message}")
            }
        })
    }

    fun acceptRequest(key: String) {
        addFriendDefinitively(userFriendRequests[key]!!)
        requestsDatabase.child(userId).child(key).removeValue()
    }
    fun rejectRequest(key: String) {
        requestsDatabase.child(userId).child(key).removeValue()
    }

    private fun addFriendDefinitively(friend: String) {
        val friendId = Regex("([a-z0-9A-Z]*)-\\D*")
            .find(friend)?.groups?.get(1)?.value ?: "bob"
        val friendDb = friendsDatabase.child(friendId)
        val myDb = friendsDatabase.child(userId)
        val friendDbKey = friendDb.push().key
        val myDbKey = myDb.push().key
        if (friendDbKey != null && myDbKey != null) {
            friendDb.child(friendDbKey).setValue("${userId}-${username}") // Adding my data in his friendsList
            myDb.child(myDbKey).setValue(friend) // Adding his data on my friendsList
        } else  {
            Log.e("SendingRequest", "Errore :(")
        }
    }


    fun sendFriendRequest(friend: String) : Boolean {
        if (userFriends.contains(friend)) {
            return false // The friend hasn't been added because already exists
        } else {
            val friendId =  Regex("([a-z0-9A-Z]*)-\\D*")
                .find(friend)?.groups?.get(1)?.value ?: "bob"
            val friendDb = requestsDatabase.child(friendId)
            val myRequestId = friendDb.push().key
            if (myRequestId == null) {
                Log.e("SendingRequest", "Errore :(")
                return false
            } else  {
                friendDb.child(myRequestId).setValue("${userId}-${username}")
                return true
            }
        }
    }
}