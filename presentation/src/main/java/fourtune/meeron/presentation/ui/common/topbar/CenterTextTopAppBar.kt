package fourtune.meeron.presentation.ui.common.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun CenterTextTopAppBar(
    onNavigation: (() -> Unit)? = null,
    navigationIcon: Painter = painterResource(id = R.drawable.ic_back),
    onAction: (() -> Unit)? = null,
    actionIcon: Painter = painterResource(id = R.drawable.ic_calender_close),
    text: @Composable BoxScope.() -> Unit
) {
    TopAppBar(
        actions = {
            IconButton(onClick = { onAction?.invoke() }, enabled = onAction != null) {
                if (onAction != null) {
                    Image(painter = actionIcon, contentDescription = null)
                }
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center, content = text)
        },
        navigationIcon = {
            IconButton(onClick = { onNavigation?.invoke() }, enabled = onNavigation != null) {
                if (onNavigation != null) {
                    Image(painter = navigationIcon, contentDescription = null)
                }
            }
        }
    )
}

@Composable
fun CenterTextTopAppBar(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 23.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = colorResource(id = R.color.black),
            fontWeight = FontWeight.Bold
        )
    }
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

@Preview
@Composable
private fun Preview() {
    CenterTextTopAppBar(text = "?????? ???????????? ??????")
}