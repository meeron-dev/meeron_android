package fourtune.meeron.presentation.ui.common.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun DetailTopBar(
    title: String,
    onBack: () -> Unit = {},
    onAction: (() -> Unit)? = null,
    isAdmin: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.topbar_color))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = onBack) {
                Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                fontSize = 18.sp,
                color = colorResource(id = R.color.black)
            )
            if (onAction != null && isAdmin) {
                TextButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = onAction) {
                    Text(
                        text = "삭제",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.dark_primary),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        content()
    }
}