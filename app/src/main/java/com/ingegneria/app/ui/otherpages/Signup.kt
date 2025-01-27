package com.ingegneria.app.ui.otherpages

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
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.LoadingDialog
import com.ingegneria.app.ui.common.MascotImage

@Composable
fun Signup(navController: NavHostController, userVM: UserViewModel){
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
                            try {
                                userVM.signup(
                                    username = username,
                                    email = email,
                                    password = password,
                                    confirmPassword = confirmPassword
                                )
                                Toast.makeText(context, "Welcome, $username", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screens.Home.name)  {
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

@Preview
@Composable
fun PreviewSignup(navController: NavHostController = rememberNavController()){
//    AppTheme { Signup(navController = navController) }
}