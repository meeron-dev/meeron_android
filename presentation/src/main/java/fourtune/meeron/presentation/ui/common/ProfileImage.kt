package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import fourtune.meeron.presentation.R

@Composable
fun ProfileImage(image: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(color = Color(0xfff2f5f9)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(data = image, builder = {
                    placeholder(R.drawable.ic_profile_non)
                    error(R.drawable.ic_profile_non)
                }),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}