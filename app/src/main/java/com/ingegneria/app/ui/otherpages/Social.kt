package com.ingegneria.app.ui.otherpages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.QrBitMapPainter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Social(navController: NavController, socialVM: SocialViewModel?, qrValue: String?) {

    var openQrCodeDialog by remember { mutableStateOf(false) }
    // Avoid passing too much arguments, TODO: centralize the current user into a ViewModel
    val currUser = Firebase.auth.currentUser
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    
    var friendName by remember { mutableStateOf("") }
    var friendId by remember { mutableStateOf("") }
    var openAddFriendDialog by remember { mutableStateOf(false) }

    /*
    If the qrValue parameter isn't empty means a QR code han been scanned
    so we open the dialog
    The launched effect is needed so when qrValue change it's content, the
    code inside is executed */
    LaunchedEffect(qrValue) {
        if(!qrValue.isNullOrEmpty()) {
            val tmpArray = qrValue.split("-")
            friendId = tmpArray[0]
            friendName = tmpArray[1]
            openAddFriendDialog = !openAddFriendDialog
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .wrapContentSize(),
                    onClick = {navController.navigate(Screens.Home.name)}
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                    )
                }
                Row {
                    FilledTonalButton(
                        onClick = { openQrCodeDialog = !openQrCodeDialog },
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .wrapContentSize(),
                    ) {
                        Icon(Icons.Filled.QrCode2, "Show QR code button")
                    }
                    FilledTonalButton(
                        onClick = {
                            if(cameraPermissionState.status.isGranted) {
//                                cameraReader(navController)
                                navController.navigate(Screens.Camera.name)
                            } else {
                                cameraPermissionState.run { launchPermissionRequest() }
                            }
                        },
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .wrapContentSize(),
                    ) {
                        Icon(Icons.Filled.CameraAlt, "QR code reader button")
                    }
                }
            }
        },
        bottomBar = {
        }
    ){ padding ->
        Column (
            modifier = Modifier.padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (socialVM != null) {
                socialVM.userFriends.forEach { friend ->
                    Card(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ){
                        Row (
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = friend // friend-id + friend-username
                            )
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = friend, // should be the friend-username
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                Text(text = "You have no friends :)")
            }
        }
    }

    when {
        openQrCodeDialog -> {
            if (currUser != null) {
                ShowQrCodeDialog(
                    user = currUser,
                    onDismissRequest = { openQrCodeDialog = false },
                    )
            }
        }
        openAddFriendDialog -> {
            AcceptFriendDialog(
                onDismissRequest = {openAddFriendDialog = false },
                friendName = friendName,
                friendId = friendId,
                socialVM = socialVM
            )
        }
    }
}

@Composable
fun ShowQrCodeDialog(user: FirebaseUser, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image( // QR code content: [user-id]-[username]
                    painter = QrBitMapPainter("${user.uid}-${user.displayName}"),
                    contentDescription = "${user.displayName}",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(top = 10.dp)
                )
                Text(
                    text = "Username: ${user.displayName}",
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp, bottom = 10.dp)
                )
            }
        }
    }
}

@Composable
fun AcceptFriendDialog(onDismissRequest: () -> Unit, friendName: String, friendId: String, socialVM: SocialViewModel?) {
    val database = FirebaseDatabase.getInstance().reference.child("friends")
//    val currUser = Firebase.auth.currentUser
//    var currUserFriends: MutableList<String> = mutableListOf()
    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (socialVM != null) {
                    Text(
                        text = "Do you want to add $friendName to your friends",
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Row (modifier = Modifier.padding(top = 20.dp)){
                        Button(
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Cancel")
                        }
                        Button(
                            onClick = {
                                /* Add the user-id into currUser's friends list */
                                socialVM.addFriend(friendId = friendId)
                                onDismissRequest.invoke()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Add")
                        }
                    }
                } else {
                    Text(
                        text = "We've had a problem retrieving the needed information",
                        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SocialPreview(navController: NavController = rememberNavController()) {
//    AppTheme { Social(navController = navController) }
}
