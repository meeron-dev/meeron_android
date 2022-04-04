package fourtune.meeron.presentation.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun StateItem(@DrawableRes resource: Int, count: String) {
    Row {
        Image(painter = painterResource(id = resource), contentDescription = null)
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = count, fontSize = 13.sp, color = colorResource(id = R.color.dark_gray))
    }
}