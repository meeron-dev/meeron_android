package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
 fun Dot(color: Color, size: Dp) {
    Canvas(
        modifier = Modifier
            .size(size)
            .wrapContentWidth(),
        onDraw = { drawCircle(color = color, radius = size.toPx() / 2f) }
    )
}