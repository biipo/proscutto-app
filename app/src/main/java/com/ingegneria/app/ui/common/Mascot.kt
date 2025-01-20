package com.ingegneria.app.ui.common

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.ingegneria.app.R

fun getDrawable(context: Context, key : String) : Int? {
    val drawableMap: Map<String, Int> = mapOf (
        "hat_witch" to R.drawable.hat_witch,
        "hat_santa" to R.drawable.hat_santa,
    )
    return drawableMap[key]
}

@Composable
fun MascotImage(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.tartegas320)
    Box (modifier) {
        Image(
            painter = image,
            contentDescription = null
        )
    }
}

@Composable
fun MascotImageBig(hat: String, modifier: Modifier = Modifier) {
    val mascotImg = painterResource(R.drawable.tartegas640)
    val context = LocalContext.current
    val hatImg = getDrawable(context, hat)
    Box (modifier) {
        Image(
            painter = mascotImg,
            contentDescription = null
        )
        hatImg?.let {
            Image (
                painter = painterResource(id = it),
                contentDescription = null
            )
        }
    }
}