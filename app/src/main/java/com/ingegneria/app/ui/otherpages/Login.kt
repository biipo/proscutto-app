package com.ingegneria.app.ui.otherpages

import android.text.TextUtils
import android.view.View
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.LoadingDialog
import com.ingegneria.app.ui.common.MascotImage

@Composable
fun Login(navController: NavController, userVM: UserViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
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
                text = "Login", fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
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
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "",
                        //tint= MaterialTheme.colorScheme.primary
                    )
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
            // Login button
            val context = LocalContext.current
            Button(
                onClick = {
                    loading = true
                    try {
                        userVM.login(email.value, password.value)
                        navController.navigate(Screens.Home.name) {
                            popUpTo(0)
                        }
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    } finally {
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Login"
                )
            }
            // Sign up prompt
            Spacer(modifier = Modifier.padding(10.dp))
            Row {
                Text(
                    text = "Don't have an account? ",
                )
                Text(
                    text = "Sign up",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screens.Signup.name)
                    }),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Row {
                Text(
                    text = "Forgot Password? ",
                )
                Text(
                    text = "Click here",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screens.ForgotPassword.name)
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

private fun inputCheck(sEmail: String?, sPassword: String?, view: View): Boolean {
    if (TextUtils.isEmpty(sEmail)) {
        Snackbar.make(view, "Missing email", Snackbar.LENGTH_SHORT).show()
        return false
    }

    if (TextUtils.isEmpty(sPassword)) {
        Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
        return false
    }
    return true
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin(navController: NavController = rememberNavController()) {
//    AppTheme { Login(navController = navController) }
}
