package com.ingegneria.app.ui.otherpages

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.QrBitMapPainter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Social(navController: NavController, socialVM: SocialViewModel, qrValue: String?) {

    var openQrCodeDialog by remember { mutableStateOf(false) }
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
                    onClick = {navController.popBackStack()}
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
        LazyColumn (
            modifier = Modifier.padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (socialVM.userFriends.isNotEmpty()) {
                items(socialVM.userFriends) { friend ->
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
                                text = Regex("[a-z0-9A-Z]*-(\\D*)")
                                    .find(friend)?.groups?.get(1)?.value ?: "bob", // "bob" is a tmp name if the regex fails
                                textAlign = TextAlign.Center
                            )
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
                        Text(text = "You have no friends :)")
                    }
                }
            }
        }
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ){
            FloatingActionButton(
                modifier = Modifier.wrapContentSize()
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                onClick = { navController.navigate(Screens.RequestsPage.name) }
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonAdd,
                    contentDescription = "Friend requests page"
                )
            }
        }
    }

    when {
        openQrCodeDialog -> {
            ShowQrCodeDialog(
                userId = socialVM.userId,
                username = socialVM.username,
                onDismissRequest = { openQrCodeDialog = false },
                )
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
fun ShowQrCodeDialog(userId: String, username: String, onDismissRequest: () -> Unit) {
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
                    painter = QrBitMapPainter("$userId-$username"),
                    contentDescription = username,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(top = 10.dp)
                )
                Text(
                    text = "Username: $username",
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp, bottom = 10.dp)
                )
            }
        }
    }
}

@Composable
fun AcceptFriendDialog(onDismissRequest: () -> Unit, friendName: String, friendId: String, socialVM: SocialViewModel) {
    val context = LocalContext.current
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
                    text = "Do you want to add $friendName to your friends?",
                    modifier = Modifier.padding(top = 20.dp)
                )
                Row (modifier = Modifier.padding(vertical = 20.dp)){
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            /* Add the user-id into currUser's friends list */
                            if (!socialVM.sendFriendRequest(friend = "$friendId-$friendName")) {
                                Toast.makeText(context, "$friendName is already your friend", Toast.LENGTH_LONG).show()
                            }
                            onDismissRequest.invoke()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp)
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
