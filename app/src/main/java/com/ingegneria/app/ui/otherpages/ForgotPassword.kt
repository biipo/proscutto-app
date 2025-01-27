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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.MascotImage
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun ForgotPassword(navController: NavController) {

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
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
                text = "Please insert your e-mail address", fontSize = 20.sp,
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

            val context = LocalContext.current
            Button(
                onClick = {
                    loading = true
                    if (email.value.isNotEmpty()) {
                        Firebase.auth.sendPasswordResetEmail(email.value)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    context,
                                    "E-mail sent!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        loading = false
                        Toast.makeText(
                            context,
                            "E-mail field is empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(0.dp, 25.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "Send reset e-mail"
                )
            }
            // Sign up prompt
            Spacer(modifier = Modifier.padding(10.dp))
            Row {
                Text(
                    text = "Would you like to go back? ",
                )
                Text(
                    text = "Click here",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screens.Login.name)
                    }),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgotPassword(navController: NavController = rememberNavController()) {
    AppTheme { ForgotPassword(navController = navController) }
}