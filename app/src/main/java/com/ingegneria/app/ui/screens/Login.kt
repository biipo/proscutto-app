package com.ingegneria.app.ui.screens

import android.text.TextUtils
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.snackbar.Snackbar

@Composable
fun Login(navController: NavController) {
    val userEmail = remember {
        mutableStateOf("")
    }
    val userPass = remember {
        mutableStateOf("")
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
                value = userEmail.value,
                onValueChange = { userEmail.value = it },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "person")
                },
                label = {
                    Text(text = "Email")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
            )
            // password input field
            OutlinedTextField(
                value = userPass.value,
                onValueChange = { userPass.value = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "",
                        //tint= MaterialTheme.colorScheme.primary
                    )
                },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 20.dp, 0.dp, 0.dp)
            )
            // login button
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Login"
                )
            }
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
    Login(navController)
}