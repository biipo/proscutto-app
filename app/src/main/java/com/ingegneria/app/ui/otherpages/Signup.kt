package com.ingegneria.app.ui.otherpages

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.LoadingDialog
import com.ingegneria.app.ui.common.MascotImage
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun Signup(navController: NavHostController) {
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

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
            // Email input field
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                leadingIcon = {
                    Icon(Icons.Default.Mail, contentDescription = "mail")
                },
                label = {
                    Text(text = "Email")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
            )
            // Username input field
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "person")
                },
                label = {
                    Text(text = "Username")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
            )
            // Password input field
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "")
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(
                            imageVector = if (passwordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password visibility",
                            tint = if (passwordVisible.value) MaterialTheme.colorScheme.tertiary else Color.Gray
                        )
                    }
                },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
            )
            // Confirm password input field
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "")
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password visibility",
                            tint = if (confirmPasswordVisible.value) MaterialTheme.colorScheme.tertiary else Color.Gray
                        )
                    }
                },
                label = { Text(text = "Confirm Password") },
                placeholder = { Text(text = "Confirm password") },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
            )
            // Sign up button
            Button(
                onClick = {
                    loading = true
                    if (username.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty() || confirmPassword.value.isEmpty()) {
                        loading = false
                        Toast.makeText(context, "One or more fields are empty", Toast.LENGTH_SHORT).show()
                    } else if (username.value.length > 12) {
                        loading = false
                        Toast.makeText(context, "Username must be under 12 characters", Toast.LENGTH_SHORT).show()
                    } else if (password.value != confirmPassword.value) {
                        loading = false
                        Toast.makeText(context, "Password and confirmation mismatch", Toast.LENGTH_SHORT).show()
                    } else {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            email.value,
                            password.value
                        ).addOnSuccessListener { authResult ->
                            val userId = authResult.user?.uid
                            val user = hashMapOf(
                                "userId" to userId,
                                "email" to email.value,
                                "username" to username.value,
                                "createdAt" to FieldValue.serverTimestamp(),
                                "lastLogin" to FieldValue.serverTimestamp(),
                                "dailyQuestionLimit" to 0,
                                "lastDailyReset" to com.google.firebase.Timestamp.now(),
                                "lastWeeklyReset" to com.google.firebase.Timestamp.now(),
                                "lastMonthlyReset" to com.google.firebase.Timestamp.now(),
                                "selectedDailyTasks" to emptyList<String>(),
                                "selectedWeeklyTasks" to emptyList<String>(),
                                "selectedMonthlyTasks" to emptyList<String>()
                            )
                            firestore.collection("users")
                                .document(userId ?: "")
                                .set(user)
                                .addOnSuccessListener {
                                    val tasksData = mapOf(
                                        "dailyTasks" to emptyList<Any>(),
                                        "weeklyTasks" to emptyList<Any>(),
                                        "monthlyTasks" to emptyList<Any>()
                                    )
                                    firestore.collection("users")
                                        .document(userId ?: "")
                                        .collection("tasks")
                                        .document("categories")
                                        .set(tasksData)
                                    loading = false
                                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screens.Home.name) { popUpTo(0) }
                                }
                                .addOnFailureListener { e ->
                                    loading = false
                                    Toast.makeText(context, "Firestore error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }.addOnFailureListener { e ->
                            loading = false
                            Toast.makeText(context, "Signup error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Sign Up"
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Row {
                Text(text = "Already have an account? ")
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

@Preview
@Composable
fun PreviewSignup(navController: NavHostController = rememberNavController()) {
    AppTheme { Signup(navController = navController) }
}
