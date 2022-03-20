package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    top.linkTo(content.bottom)
                }
                .padding(top = 10.dp),
            leftClick,
            rightClick,
            leftText,
            rightText,
            rightEnable
        )
    }
}

@Composable
fun MeeronSingleButtonBackGround(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = stringResource(R.string.complete),
    enable: Boolean = true,
    contents: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        contents()
        MeeronSingleButton(modifier = Modifier.align(Alignment.BottomCenter), onClick = onClick, enable = enable)
    }
}


@Composable
fun MeeronSingleButton(modifier: Modifier = Modifier, onClick: () -> Unit, enable: Boolean) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.primary),
        ),
        contentPadding = PaddingValues(vertical = 18.dp),
        shape = RoundedCornerShape(7.dp)
    ) {
        Text(
            text = stringResource(id = R.string.next),
            fontSize = 18.sp,
            color = colorResource(id = if (enable) R.color.white else R.color.light_gray),
            maxLines = 1,
            textAlign = TextAlign.Center,
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
    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(4f),
            onClick = leftClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_gray_white)),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = leftText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.weight(4f),
            onClick = rightClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.primary)),
            contentPadding = PaddingValues(vertical = 16.dp),
            enabled = rightEnable
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = rightText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center
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

@Preview
@Composable
private fun Preview2() {
    MeeronSingleButtonBackGround(enable = false) {

    }
}
