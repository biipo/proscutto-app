package com.ingegneria.app.ui.common

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Generate the QR code
@Composable
fun QrBitMapPainter (content: String, size: Dp = 150.dp, padding: Dp = 1.dp): BitmapPainter {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    var bitmap by remember(content) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(bitmap) {
        if (bitmap != null) return@LaunchedEffect

        launch(Dispatchers.IO) {
            val qrCodeWriter = QRCodeWriter()

            val encodeHints = mutableMapOf<EncodeHintType, Any?>()
                .apply {
                    this[EncodeHintType.MARGIN] = paddingPx
                }

            val bitmapMatrix = try {
                qrCodeWriter.encode(
                    content, BarcodeFormat.QR_CODE,
                    sizePx, sizePx, encodeHints
                )
            } catch (ex: WriterException) {
                null
            }

            val matrixWidth = bitmapMatrix?.width ?: sizePx
            val matrixHeight = bitmapMatrix?.height ?: sizePx

            val newBitmap = Bitmap.createBitmap(
                bitmapMatrix?.width ?: sizePx,
                bitmapMatrix?.height ?: sizePx,
                Bitmap.Config.ARGB_8888,
            )

            for (x in 0 until matrixWidth) {
                for (y in 0 until matrixHeight) {
                    val shouldColorPixel = bitmapMatrix?.get(x, y) ?: false
                    val pixelColor = if (shouldColorPixel) Color.BLACK else Color.WHITE

                    newBitmap.setPixel(x, y, pixelColor)
                }
            }

            bitmap = newBitmap
        }
    }


    return remember(bitmap) {
        val currentBitmap = bitmap ?: Bitmap.createBitmap(
            sizePx, sizePx,
            Bitmap.Config.ARGB_8888
        ).apply { eraseColor(Color.TRANSPARENT) }
        BitmapPainter(currentBitmap.asImageBitmap())
    }
}

// Open the camera for reading the QR code
@Composable
fun cameraReader() {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        mutableStateOf<ProcessCameraProvider?>(null)
    }
    //https://medium.com/@alexander13oster/compose-barcode-scanner-is-simple-866f20dba6d8
    // There's an image analyzer on zxing too
    LaunchedEffect (localContext) {
        ProcessCameraProvider.getInstance(localContext)
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            runCatching {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview
                )
            }.onFailure {
                Log.e("CAMERA", "U :(")
            }
            previewView
        }
    )
}