package fourtune.meeron.presentation.ui.common.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun CircleBackgroundText(
    modifier: Modifier = Modifier,
    text: String = "123",
    fontColor: Color = colorResource(id = R.color.white)
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = fontColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            softWrap = false
        )
    }
}

@Preview
@Composable
private fun CircleBackgroundTextPrev() {
    CircleBackgroundText(
        modifier = Modifier
            .size(24.dp)
            .background(colorResource(id = R.color.primary))
    )
}