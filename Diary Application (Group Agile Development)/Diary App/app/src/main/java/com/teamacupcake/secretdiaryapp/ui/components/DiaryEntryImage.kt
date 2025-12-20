package com.teamacupcake.secretdiaryapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.teamacupcake.secretdiaryapp.data.DiaryEntry

class DiaryEntryImage {
    @SuppressLint("NotConstructor")
    @Composable
    fun DiaryEntryImage(entry: DiaryEntry) {
        // Assuming entry.imageUrl is a nullable String holding the URL
        entry.imageUrl?.let { imageUrl ->
            // Remember the current imageUrl to use in LaunchedEffect
            val currentImageUrl = rememberUpdatedState(imageUrl)

            // Force image reload whenever imageUrl changes
            LaunchedEffect(currentImageUrl.value) {
                // This block will execute whenever currentImageUrl changes,
                // effectively "forcing" a reload by re-composing the Image composable below
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentImageUrl.value)
                            .apply(block = fun ImageRequest.Builder.() {
                                // Here you can add any custom logic for the request
                                // For example, placeholders, errors, etc.
                                crossfade(true)
                            }).build()
                    ),
                    contentDescription = "Picked image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .heightIn(max = 200.dp)
                )
            }
        }
    }

}