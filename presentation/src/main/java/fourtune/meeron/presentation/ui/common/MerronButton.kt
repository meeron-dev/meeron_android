package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun MerronButton(leftClick: () -> Unit, rightClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Button(
            onClick = { leftClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_gray_white)),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 60.dp)
        ) {
            Text(
                text = stringResource(R.string.previous),
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                maxLines = 1
            )
        }
        Button(
            onClick = { rightClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.primary)),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 60.dp)
        ) {
            Text(
                text = stringResource(R.string.next),
                fontSize = 16.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun MeeronButtonPrev() {
    MerronButton({}, {})
}