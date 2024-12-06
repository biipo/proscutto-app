package com.ingegneria.app.ui.screens

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.ingegneria.app.ui.theme.AppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ingegneria.app.navigation.Screens
import androidx.compose.ui.graphics.Color

@Composable
fun Login(navController: NavController) {
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(40.dp)
        ) {
            Text(
                text = "Login", fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
            )

            // email input field
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
            // password input field
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
            // login button
            val context = LocalContext.current
            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email.value, password.value)
                            .addOnSuccessListener() {
                                navController.navigate(Screens.Home.name)
                            }
                            .addOnFailureListener() {
                                Toast.makeText(
                                    context,
                                    it.message,
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            context,
                            "One or more fields are empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Login"
                )
            }
            // sign up prompt
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
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: implement authentication
        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Because we've used the "include" in the xml we have to use nested findViewById
        // TITLE
        binding.authComponents.pageTitle.text = "Login"

        // EMAIL field
        val email = binding.authComponents.emailTextbox.text

        // PASSWORD field
        val password = binding.authComponents.passwordTextbox.text

        // LOGIN BUTTON
        val authButton = binding.authComponents.authButton
        authButton.text = "LOGIN"
        authButton.setOnClickListener { view ->
            val sEmail = email.toString()
            val sPassword = password.toString()
            if (!inputCheck(sEmail, sPassword, view)) {
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish() // removes this activity from the backStack
                    } else {
                        Snackbar.make(view, "Login error", Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }
        }

        // LINK -> SIGNUP
        val otherAuthText = binding.authComponents.otherAuthText
        otherAuthText.text = "Don't have an account? Signup!"
        otherAuthText.setOnClickListener {
            startActivity(Intent(applicationContext, SignupActivity::class.java))
            // the finish is not called because the user will return to the login
            // after registration, so we need to keep this activity in the backStack
        }
    }*/

private fun inputCheck(sEmail: String?, sPassword: String?, view: View): Boolean {
    if(TextUtils.isEmpty(sEmail)) {
        Snackbar.make(view, "Missing email", Snackbar.LENGTH_SHORT).show()
        return false
    }

    if(TextUtils.isEmpty(sPassword)) {
        Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
        return false
    }
    return true
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin(navController: NavController = rememberNavController()){
    AppTheme {Login(navController = navController)}
}