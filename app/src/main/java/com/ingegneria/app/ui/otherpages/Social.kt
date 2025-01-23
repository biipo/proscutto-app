package com.ingegneria.app.ui.otherpages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.QrBitMapPainter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Social(navController: NavController, qrValue: String?) {

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
                friendId = friendId
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
fun AcceptFriendDialog(onDismissRequest: () -> Unit, friendName: String, friendId: String) {
    val database = FirebaseDatabase.getInstance().reference.child("friends")
    val currUser = Firebase.auth.currentUser
    var currUserFriends: MutableSet<String> = mutableSetOf()
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
                            if (currUser != null) {
                                database.child(currUser.uid).addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        currUserFriends = snapshot.children.mapNotNull {
                                            it.getValue(String::class.java)
                                        }.toMutableSet()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("Adding friend", "Errore :( - ${error.message}")
                                    }
                                })
                            }
                            /* TODO: check if the friendId is already in the currUser's friends list */
                            currUserFriends.add(friendId)
                            if (currUser != null) {
                                database.child(currUser.uid).setValue(currUserFriends.toList())
                            }
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SocialPreview(navController: NavController = rememberNavController()) {
//    AppTheme { Social(navController = navController) }
}
