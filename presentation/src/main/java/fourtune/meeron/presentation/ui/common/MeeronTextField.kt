package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun MeeronClickableText(
    title: String,
    isEssential: Boolean = false,
    limit: Int = 0,
    text: String = "",
    onClick: () -> Unit = {}
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Text(text = title, fontSize = 17.sp, color = colorResource(id = R.color.dark_gray))
        Spacer(modifier = Modifier.padding(6.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            FieldBody(isEssential = isEssential, text = text, limit = limit) {
                Text(text = text, fontSize = 16.sp, color = colorResource(id = R.color.dark_gray))
            }
        }

    }
}

@Composable
fun MeeronTextField(
    title: String,
    isEssential: Boolean = false,
    limit: Int = 0,
    text: String = "",
    onValueChange: (String) -> Unit = {}
) {
    Column {
        Text(text = title, fontSize = 17.sp, color = colorResource(id = R.color.dark_gray))
        Spacer(modifier = Modifier.padding(6.dp))
        BasicTextField(
            modifier = Modifier,
            value = text,
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                FieldBody(isEssential, text, limit, contents = { innerTextField() })
            }
        )
    }
}

@Composable
private fun FieldBody(
    isEssential: Boolean,
    text: String,
    limit: Int,
    contents: @Composable BoxScope.() -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (isEssential && text.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(R.string.info_essential),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.gray)
                )
            }
            if (text.isNotEmpty()) {
                contents()
            } else if (limit != 0) {
                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = String.format(stringResource(id = R.string.info_limit), limit),
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.light_gray)
                )
            }
        }
        Divider(Modifier.padding(top = 1.dp))
    }
}

@Preview(name = "emptyText", showBackground = true)
@Composable
private fun Preview1() {
    MeeronTextField("Meeron", false)
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    MeeronTextField("Meeron", true)

}

@Preview(showBackground = true)
@Composable
private fun Preview3() {
    MeeronTextField("Meeron", false, 30)
}

@Preview(showBackground = true)
@Composable
private fun Preview4() {
    MeeronTextField("Meeron", true, 30)
}

@Preview(showBackground = true)
@Composable
private fun Preview5() {
    MeeronClickableText(text = "sadaskjdasjd", onClick = {}, title = "Meeron")
}