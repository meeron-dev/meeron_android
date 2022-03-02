package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun CenterTextTopAppBar(onAction: () -> Unit, text: @Composable BoxScope.() -> Unit) {
    TopAppBar(
        actions = {
            IconButton(onClick = onAction) {
                Image(painter = painterResource(id = R.drawable.ic_calender_close), contentDescription = null)
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center, content = text)
        }
    )
}

@Preview
@Composable
private fun CenterTextTopAppBarPrev() {
    CenterTextTopAppBar(onAction = { /*TODO*/ }) {
        Text(
            text = stringResource(id = R.string.create_meeting),
            fontSize = 18.sp,
            color = colorResource(id = R.color.black)
        )
    }
}