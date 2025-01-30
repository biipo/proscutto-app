package com.ingegneria.app.ui.otherpages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.theme.AppTheme

@Composable
fun FriendRequests(navController: NavController, socialVM: SocialViewModel) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TextButton(
                modifier = Modifier
                    .padding(top = 10.dp, start = 7.dp)
                    .wrapContentSize(),
                onClick = {
                    // The qrValue isn't needed, but is specified as empty for the case in which
                    // the user scanned a QR before entering this page; in that way we avoid passing again
                    // the argument to the Social page
                    navController.popBackStack("${Screens.Social.name}?qrValue=", false)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                )
            }
        },
    ){ padding ->
        LazyColumn (
            modifier = Modifier.padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (socialVM.userFriendRequests.isNotEmpty()) {
                item {
                    socialVM.userFriendRequests.forEach { (key, value) ->
                        Card(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                                .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                        ){
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Icon(
                                        modifier = Modifier.padding(start = 10.dp),
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = value // friend-id + friend-username
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        text = Regex("[a-z0-9A-Z]*-([a-zA-Z0-9]*)")
                                            .find(value)?.groups?.get(1)?.value ?: "bob", // "bob" is a tmp name if the regex fails
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Row (modifier = Modifier.padding(vertical = 10.dp)){
                                    Button(
                                        modifier = Modifier.wrapContentSize().padding(end = 10.dp),
                                        onClick = {
                                            socialVM.rejectRequest(key)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Red,
                                            contentColor = Color.White
                                        ),
                                    ) {
                                        Text(text = "Reject")
                                    }
                                    Button(
                                        modifier = Modifier.wrapContentSize().padding(end = 10.dp),
                                        onClick = {
                                            socialVM.acceptRequest(key)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Green,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = "Accept")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                item {
                    Column (
                        modifier = Modifier.fillMaxSize()
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "You have no requests")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendRequestsPreview(navController: NavController = rememberNavController()) {
    AppTheme { FriendRequests(navController = navController, socialVM = viewModel<SocialViewModel>()) }
}