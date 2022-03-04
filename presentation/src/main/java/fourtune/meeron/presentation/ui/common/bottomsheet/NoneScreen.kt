package fourtune.meeron.presentation.ui.common.bottomsheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 바텀싯을 여러개 쓸 때, sheetContent 에 아무런 Composable 도 없으면 (height=0.dp일 경우 발생)
 * IllegalArgumentException : The initial value must have an associated anchor. 발생
 */
@Composable
fun NoneScreen() {
    Spacer(modifier = Modifier.padding(1.dp))
}