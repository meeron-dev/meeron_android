package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fourtune.meeron.presentation.R

@Composable
fun MeeronButtonBackGround(
    modifier: Modifier = Modifier,
    leftClick: () -> Unit = {},
    rightClick: () -> Unit = {},
    leftText: String = stringResource(R.string.previous),
    rightText: String = stringResource(R.string.next),
    rightEnable: Boolean = true,
    contents: @Composable () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (content, button) = createRefs()
        Surface(
            modifier = Modifier.constrainAs(content) {
                top.linkTo(parent.top)
                bottom.linkTo(button.top)
                height = Dimension.fillToConstraints
            },
            content = contents
        )
        MeeronButton(
            Modifier.constrainAs(button) {
                bottom.linkTo(parent.bottom)
                top.linkTo(content.bottom)
            }.padding(top = 10.dp),
            leftClick,
            rightClick,
            leftText,
            rightText,
            rightEnable
        )
    }
}

@Composable
fun MeeronButton(
    modifier: Modifier = Modifier,
    leftClick: () -> Unit = {},
    rightClick: () -> Unit = {},
    leftText: String = stringResource(R.string.previous),
    rightText: String = stringResource(R.string.next),
    rightEnable: Boolean = true
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Button(
            onClick = leftClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_gray_white)),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 60.dp)
        ) {
            Text(
                text = leftText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                maxLines = 1
            )
        }
        Button(
            onClick = rightClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.primary)),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 60.dp),
            enabled = rightEnable
        ) {
            Text(
                text = rightText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 300)
@Composable
private fun Preview() {
    MeeronButtonBackGround {
        Column {
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
            Text(text = "asdasdasd")
        }
    }
}

@Preview
@Composable
private fun MeeronButtonPrev() {
    MeeronButton(Modifier, {}, {})
}