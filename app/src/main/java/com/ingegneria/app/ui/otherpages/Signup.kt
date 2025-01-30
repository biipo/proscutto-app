package com.ingegneria.app.ui.otherpages

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.LoadingDialog
import com.ingegneria.app.ui.common.MascotImage

@Composable
fun Signup(navController: NavHostController){
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }


    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(40.dp)
        ) {
            MascotImage()
            Text(
                text = "Sign Up",
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
            )
            LazyColumn {
                item {
                    // email input field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        leadingIcon = {
                            Icon(Icons.Default.Mail, contentDescription = "mail")
                        },
                        label = {
                            Text(text = "Email")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
                    )
                    // username input field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "person")
                        },
                        label = {
                            Text(text = "Username")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
                    )
                    // password input field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "",
                                //tint= MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Password visibility",
                                    tint = if (passwordVisible) MaterialTheme.colorScheme.tertiary
                                    else Color.Gray
                                )
                            }
                        },
                        label = { Text(text = "Password") },
                        placeholder = { Text(text = "Password") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
                    )
                    // TODO: make appear password requirements
                    // confirm password input field
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "",
                                //tint= MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Password visibility",
                                    tint = if (confirmPasswordVisible) MaterialTheme.colorScheme.tertiary
                                    else Color.Gray
                                )
                            }
                        },
                        label = { Text(text = "Confirm Password") },
                        placeholder = { Text(text = "Confirm password") },
                        singleLine = true,
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
                    )
                    // sign up button
                    val context = LocalContext.current

                    Button(
                        onClick = {
                            loading = true
                            if (username.isEmpty() || email.isEmpty()
                                || password.isEmpty() || confirmPassword.isEmpty()) {
                                Toast.makeText(context, "One or more fields are empty", Toast.LENGTH_SHORT).show()
                            } else {
                                if (emailCheck(email)) {
                                    if (username.length > 12) {
                                        Toast.makeText(context, "Username must be under 12 characters", Toast.LENGTH_SHORT).show()
                                    } else {
                                        if (!Regex("[a-zA-Z0-9]*").matches(username)) {
                                            Toast.makeText(context, "Username cannot contains symbols", Toast.LENGTH_SHORT).show()
                                        } else {
                                            if (password != confirmPassword) {
                                                Toast.makeText(context, "Password and confirmation password mismatch", Toast.LENGTH_SHORT).show()
                                            } else {
                                                if(!passwordCheck(password)) {
                                                    Toast.makeText(context, "Invalid password; it must contains symbols, digits and capital letters", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                                        .addOnSuccessListener {
                                                            val user = FirebaseAuth.getInstance().currentUser
                                                            val profileUpdates = userProfileChangeRequest {
                                                                displayName = username
                                                            }
                                                            user!!.updateProfile(profileUpdates)
                                                                .addOnSuccessListener {
                                                                    val id = user.uid
                                                                    val firestore = FirebaseFirestore.getInstance()
                                                                    val db = Firebase.database
                                                                    val petDb = db.getReference("characters")
                                                                    val friendsDb = db.getReference("friends")
                                                                    val requestsDb = db.getReference("friendRequests")
                                                                    val shopDb = db.getReference("shop")
                                                                    petDb.child(id).setValue(
                                                                        mapOf(
                                                                            Pair("hat", ""),
                                                                            Pair("hp", 10),
                                                                            Pair("level", 1),
                                                                            Pair("mult", 1.0),
                                                                            Pair("name", "Pet"),
                                                                            Pair("xp", 0)
                                                                        )
                                                                    )
                                                                    friendsDb.child(id).setValue(listOf(""))
                                                                    requestsDb.child(id).setValue(listOf(""))
                                                                    shopDb.child(id).setValue(listOf(""))

                                                                    val userData = hashMapOf(
                                                                        "userId" to id,
                                                                        "email" to user.email,
                                                                        "username" to user.displayName,
                                                                        "createdAt" to FieldValue.serverTimestamp(),
                                                                        "lastLogin" to FieldValue.serverTimestamp(),
                                                                        "dailyQuestionLimit" to 0,
                                                                        "lastDailyReset" to com.google.firebase.Timestamp.now(),
                                                                        "lastWeeklyReset" to com.google.firebase.Timestamp.now(),
                                                                        "lastMonthlyReset" to com.google.firebase.Timestamp.now(),
                                                                        "selectedDailyTasks" to emptyMap<String, Boolean>(),
                                                                        "selectedWeeklyTasks" to emptyMap<String, Boolean>(),
                                                                        "selectedMonthlyTasks" to emptyMap<String, Boolean>(),
                                                                        "maxLevelReached" to 1,              // livello iniziale
                                                                        "dailyTasksCompleted" to 0,         // numero di task daily completate (totali)
                                                                        "weeklyTasksCompleted" to 0,        // numero di task weekly completate (totali)
                                                                        "monthlyTasksCompleted" to 0,       // numero di task monthly completate (totali)
                                                                        "quizCompleted" to 0,              // quanti quiz completati
                                                                        "totalLoginDays" to 1              // conteggio di giorni in cui si è loggato (parte da 1, ad es.)
                                                                    )
                                                                    firestore.collection("users")
                                                                        .document(id ?: "")
                                                                        .set(userData)
                                                                        .addOnSuccessListener {
                                                                            Toast.makeText(context, "Welcome, $username", Toast.LENGTH_SHORT).show()
                                                                            navController.navigate(Screens.Home.name)  {
                                                                                popUpTo(0)
                                                                            }
                                                                        }.addOnFailureListener { e ->
                                                                            Log.e("SIGNUP ERROR INITIALIZING FIRESTORE ON LOGIN", e.message.toString())
                                                                            Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                }.addOnFailureListener { e ->
                                                                    Log.e("SIGNUP ERROR UPDATING USER", e.message.toString())
                                                                    Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                                                }
                                                        }.addOnFailureListener { e ->
                                                            if (e.message.toString() == "The email address is already in use by another account.") {
                                                                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                                                            } else {
                                                                Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                                            }
                                                            Log.e("SIGNUP ERROR CREATING USER", e.message.toString())
                                                        }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
                                }
                            }
                            loading = false
                        },
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            text = "Sign Up"
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Row {
                        Text(
                            text = "Already have an account? ",
                        )
                        Text(
                            text = "Log in",
                            modifier = Modifier.clickable(onClick = {
                                navController.navigate(Screens.Login.name)
                            }),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    if (loading) {
                        LoadingDialog()
                    }
                }
            }
        }
    }
}

fun passwordCheck(password: String) : Boolean {
    return Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^!&+=]).{4,}$").matches(password)
}

fun emailCheck(email: String) : Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview
@Composable
fun PreviewSignup(navController: NavHostController = rememberNavController()){
//    AppTheme { Signup(navController = navController) }
}
