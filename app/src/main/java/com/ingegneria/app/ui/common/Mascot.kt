package com.ingegneria.app.ui.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ingegneria.app.R

@Composable
fun MascotImage(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.tartegas320)
    Image (
        painter = image,
        contentDescription = null
    )
}