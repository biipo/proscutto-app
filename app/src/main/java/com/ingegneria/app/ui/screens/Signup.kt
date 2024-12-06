package com.ingegneria.app.ui.screens

import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp.initializeApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun Signup(navController: NavHostController){
    val username = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val confirmPassword = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    val confirmPasswordVisible = remember {
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
                text = "Sign Up",
                fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
            )
            // email input field
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
            // username input field
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
            // confirm password input field
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "",
                        //tint= MaterialTheme.colorScheme.primary
                    )
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
            // sign up button
            val context = LocalContext.current

            Button(
                onClick = {
                    if (username.value.isEmpty() || email.value.isEmpty()
                        || password.value.isEmpty() || confirmPassword.value.isEmpty()) {
                        Toast.makeText(
                            context,
                            "One or more fields are empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (username.value.length > 12) {
                            Toast.makeText(
                                context,
                                "Username must be under 12 characters",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (password.value == confirmPassword.value) {
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                                    email.value,
                                    password.value
                                ).addOnSuccessListener {
                                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                                    val user = hashMapOf(
                                        "username" to username.value
                                    )
                                    Firebase.firestore.collection("users")
                                        .document(userId!!)
                                        .set(user)
                                        .addOnSuccessListener {}
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                it.message,
                                                Toast.LENGTH_SHORT,
                                            ).show()
                                        }
                                    //navController.navigate(Screens.Home.name)
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        it.message,
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Password and confirmation password mismatch",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Sign Up"
                )
            }
        }
    }
}
/*
private fun inputCheck(sUsername: String?, sEmail: String?, sPassword: String?, sConfirmPassword: String?): Boolean {
    if(TextUtils.isEmpty(sUsername)) {
        Snackbar.make(view, "Missing username", Snackbar.LENGTH_SHORT).show()
        return false
    }
    if(TextUtils.isEmpty(sEmail)) {
        Snackbar.make(view, "Missing email", Snackbar.LENGTH_SHORT).show()
        return false
    }
    if(TextUtils.isEmpty(sPassword)) {
        Snackbar.make(view, "Missing password", Snackbar.LENGTH_SHORT).show()
        return false
    }
    if(TextUtils.isEmpty(sConfirmPassword)) {
        Snackbar.make(view, "Missing confirmation password", Snackbar.LENGTH_SHORT).show()
        return false
    }
    if(sPassword != sConfirmPassword) {
        Snackbar.make(view, "Password and confirmation password do not match", Snackbar.LENGTH_SHORT).show()
        return false
    }
    return true
}
*/

@Preview
@Composable
fun PreviewSignup(navController: NavHostController = rememberNavController()){
    AppTheme { Signup(navController = navController) }
}

/*
class SignupFragment : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.authComponents.pageTitle.text = "Signup"

        val username = binding.usernameTextbox.text

        val email = binding.authComponents.emailTextbox.text

        val password = binding.authComponents.passwordTextbox.text
        val confirmPassword = binding.signupConfirmPasswordTextbox.text

        val authButton = binding.authComponents.authButton
        authButton.text = "SIGNUP"
        authButton.setOnClickListener { view ->
            val sUsername = username.toString()
            val sEmail = email.toString()
            val sPassword = password.toString()
            val sConfirmPassword = confirmPassword.toString()
            if (!inputCheck(sUsername, sEmail, sPassword, sConfirmPassword, view)) {
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        task.result.user?.updateProfile(
                            userProfileChangeRequest { displayName = sUsername }
                            )
                        finish()
                    } else {
                        Snackbar.make(view, "Signup error", Snackbar.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                }
        }

        val otherAuthText = binding.authComponents.otherAuthText
        otherAuthText.text = "Already have an account? Login!"
        otherAuthText.setOnClickListener {
            finish() // we got back to login, to be sure the account has been created in the db
        }
    }
*/
