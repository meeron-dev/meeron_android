package fourtune.meeron.presentation.ui.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import fourtune.meeron.presentation.R

@Composable
fun MultiTextDialog(
    title: String,
    text: String,
    topText: String,
    buttonText: String,
    onDismissRequest: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest(false) }) {

        Column(
            modifier = Modifier.background(colorResource(id = R.color.white), shape = RoundedCornerShape(5.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 12.dp, start = 17.dp),
                text = topText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium
            )
            Column(
                modifier = Modifier.padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.dark_gray),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = text,
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.gray),
                )
            }
            DialogButton(onDismissRequest, buttonText, onClick)
        }
    }
}