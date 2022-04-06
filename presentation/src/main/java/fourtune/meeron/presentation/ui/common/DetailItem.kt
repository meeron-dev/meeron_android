package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun DetailItem(
    title: String,
    onClickDetail: () -> Unit = {},
    enable: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                color = colorResource(id = if (enable) R.color.black else R.color.gray),
                lineHeight = 39.sp
            )
            IconButton(onClick = onClickDetail, enabled = enable) {
                Image(
                    painter = painterResource(id = if (enable) R.drawable.ic_right_arrow_21 else R.drawable.ic_right_arrow_disable_21),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        content()
    }
}