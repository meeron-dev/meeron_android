package fourtune.meeron.presentation.ui.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
internal fun DialogButton(onDismissRequest: (Boolean) -> Unit, actionText: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            modifier = Modifier
                .background(color = colorResource(id = R.color.dark_gray_white))
                .weight(1f),
            onClick = { onDismissRequest(false) },
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            Text(
                text = "취소",
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
            )
        }
        Divider(modifier = Modifier.width(1.dp))
        TextButton(
            modifier = Modifier
                .background(color = colorResource(id = R.color.dark_gray_white))
                .weight(1f),
            onClick = { onDismissRequest(false);onAction() },
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            Text(
                text = actionText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
            )
        }
    }
}