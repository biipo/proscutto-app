package com.ingegneria.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Signup(navController: NavHostController){
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val confirmPassword = remember {
        mutableStateOf("")
    }
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
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 25.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, 50.dp, 0.dp, 0.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSignup(navController: NavHostController = rememberNavController()){
    Signup(navController = navController)
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

    private fun inputCheck(sUsername: String?, sEmail: String?, sPassword: String?, sConfirmPassword: String?, view: View): Boolean {
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
}*/