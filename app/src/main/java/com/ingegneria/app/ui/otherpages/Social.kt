package com.ingegneria.app.ui.otherpages

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
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.firebase.auth.FirebaseUser
import com.ingegneria.app.navigation.Screens
import com.ingegneria.app.ui.common.QrBitMapPainter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Social(navController: NavController, user: FirebaseUser?, cameraPermissionState: PermissionState) {

    var openQrCodeDialog by remember { mutableStateOf(false) }

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
                                /*TODO: open camera*/
                            } else {
                                // SideEffect {
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
            if (user != null) {
                showQrCode(
                    user = user,
                    onDismissRequest = { openQrCodeDialog = false },
                    )
            }
        }
    }
}

@Composable
fun showQrCode(user: FirebaseUser, onDismissRequest: () -> Unit) {
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
                Image(
                    painter = QrBitMapPainter(user.uid),
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

@Preview(showBackground = true)
@Composable
fun SocialPreview(navController: NavController = rememberNavController()) {
//    AppTheme { Social(navController = navController) }
}
