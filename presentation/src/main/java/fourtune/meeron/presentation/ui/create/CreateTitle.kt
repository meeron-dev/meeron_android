package fourtune.meeron.presentation.ui.create

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun CreateTitle(
    title: Int,
    selectedDate: String = "",
    selectedTime: String = "",
    extraContents: @Composable RowScope.() -> Unit = { CreateText(text = "") }
) {
    Column {
        Text(
            text = stringResource(id = title),
            fontSize = 25.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                CreateText(text = selectedDate)
                CreateText(text = selectedTime)
            }
            extraContents()
        }
    }
}

@Composable
fun CreateText(text: String) {
    Text(text = text, fontSize = 13.sp, color = colorResource(id = R.color.light_gray))
}

@Preview
@Composable
private fun Preview() {
    CreateTitle(title = R.string.info_title)
}