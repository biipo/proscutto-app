package com.ingegneria.app.ui.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.ingegneria.app.navigation.Screens

@Composable
fun Settings(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val user = Firebase.auth.currentUser

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
        ) {
            Row (
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ){
                Box (
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        modifier = Modifier.size(100.dp, 100.dp),
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Profile image"
                    )
                }
                Column (
                    modifier = Modifier.padding(start = 15.dp)
                        .fillMaxWidth(),
                ){
                    Text(
                        modifier = Modifier,
                        text = "Username: ${user?.displayName}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        text = "Email: ${user?.email}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 15.dp).padding(bottom = 15.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(),
                onClick = {
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    if (auth.currentUser == null) {
                        navController.navigate(Screens.Login.name) {
                            popUpTo(Screens.Home.name) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("LOGOUT")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth(),
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("DELETE ACCOUNT")
            }
        }
    }

    if (showDialog) {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
           Column (
               modifier = Modifier.wrapContentSize()
           ){
               Text(
                   modifier = Modifier.padding(vertical = 5.dp),
                   text = "Delete Account"
               )
               Text(
                   modifier = Modifier.padding(vertical = 5.dp),
                   text = "You need to enter your credentials to confirm"
               )

               // Email input field
               OutlinedTextField(
                   value = email.value,
                   onValueChange = { email.value = it },
                   leadingIcon = {
                       Icon(Icons.Default.Mail, contentDescription = "person")
                   },
                   label = {
                       Text(text = "Email")
                   },
                   singleLine = true,
                   modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
               )
               // Password input field
               OutlinedTextField(
                   value = password.value,
                   onValueChange = { password.value = it },
                   leadingIcon = {
                       Icon( Icons.Default.Lock, contentDescription = "", )
                   },
                   label = { Text(text = "Password") },
                   placeholder = { Text(text = "Password") },
                   singleLine = true,
                   visualTransformation = PasswordVisualTransformation(),
                   modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
               )
               Row (
                   modifier = Modifier.fillMaxWidth()
                       .padding(horizontal = 20.dp, vertical = 20.dp),
                   horizontalArrangement = Arrangement.SpaceBetween
               ){
                   OutlinedButton(
                       modifier = Modifier.padding(horizontal = 5.dp),
                       onClick = { showDialog = false }
                   ) {
                       Text("CANCEL")
                   }

                   FilledTonalButton(
                       modifier = Modifier.padding(horizontal = 5.dp),
                       onClick = {
                           if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                               FirebaseAuth.getInstance()
                                   .signInWithEmailAndPassword(email.value, password.value)
                                   .addOnSuccessListener { res ->
                                       val id = res.user?.uid
                                       val db = Firebase.database
                                       if (id != null) {
                                           FirebaseFirestore.getInstance().collection("users").document(id).delete()
                                           db.getReference("characters").child(id).removeValue()
                                           db.getReference("friends").child(id).removeValue()
                                           db.getReference("friendRequests").child(id).removeValue()
                                           db.getReference("shop").child(id).removeValue()
                                       }
                                       res.user?.delete()
                                           ?.addOnSuccessListener {
                                               Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                               navController.navigate(Screens.Login.name) {
                                                   popUpTo(0)
                                               }
                                           }?.addOnFailureListener { e ->
                                               Log.e("FAILED DELETING USER", e.message.toString())
                                               Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                           }
                                   }.addOnFailureListener {
                                       Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show()
                                   }
                           } else {
                               Toast.makeText(context, "One or more fields are empty", Toast.LENGTH_SHORT).show()
                           }
                           showDialog = false
                       },
                       colors = ButtonDefaults.buttonColors(
                           MaterialTheme.colorScheme.errorContainer,
                           MaterialTheme.colorScheme.error
                       )
                   ) {
                       Text("CONFIRM")
                   }
               }
           }
        }
    }
}

@Preview
@Composable
fun PreviewSettings(navController: NavController = rememberNavController()) {
    Settings(navController = navController)
}
