package fourtune.meeron.presentation.ui.common.action

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

sealed interface ContentFactory {
    @Composable
    fun Create()

    class LimitTextField(
        private val modifier: Modifier = Modifier,
        private val text: String,
        private val onValueChange: (String) -> Unit,
        private val isEssential: Boolean,
        private val limit: Int,
        private val easyDelete: Boolean = false,
        private val onClickDelete: () -> Unit = {}
    ) : ContentFactory {

        @Composable
        override fun Create() {
            BasicTextField(
                modifier = modifier,
                value = text,
                onValueChange = onValueChange,
                decorationBox = { innerTextField ->
                    ActionBody(
                        isEssential = isEssential,
                        text = text,
                        limit = limit,
                        easyDelete = easyDelete,
                        onClickDelete = onClickDelete,
                        contents = innerTextField
                    )
                }
            )
        }
    }

    class ActionField(
        private val modifier: Modifier = Modifier,
        private val text: String,
        private val isEssential: Boolean = false,
        private val easyDelete: Boolean = false,
        private val onClick: () -> Unit = {},
        private val onClickDelete: () -> Unit = {}
    ) : ContentFactory {
        @Composable
        override fun Create() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
            ) {
                ActionBody(
                    isEssential = isEssential,
                    text = text,
                    limit = 0,
                    easyDelete = easyDelete,
                    onClickDelete = onClickDelete,
                    contents = {
                        Text(text = text, fontSize = 16.sp, color = colorResource(id = R.color.dark_gray))
                    },
                )
            }
        }
    }
}

@Composable
fun MeeronActionBox(
    factory: ContentFactory,
    title: String,
    onClick: () -> Unit = {},
    showIcon: Boolean = false,
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 17.sp, color = colorResource(id = R.color.dark_gray))
            if (showIcon) {
                IconButton(onClick = onClick) {
                    Image(painter = painterResource(id = R.drawable.ic_small_plus), contentDescription = null)
                }
            }
        }
        Spacer(modifier = Modifier.padding(6.dp))
        factory.Create()
    }
}

@Composable
private fun ActionBody(
    isEssential: Boolean,
    text: String,
    limit: Int,
    easyDelete: Boolean = false,
    onClickDelete: () -> Unit = {},
    contents: @Composable () -> Unit,
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
            contents()
            if (limit != 0 && text.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = String.format(stringResource(id = R.string.info_limit), limit),
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.light_gray)
                )
            }
            if (easyDelete && text.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable(onClick = onClickDelete),
                    fontSize = 14.sp,
                    text = stringResource(id = R.string.delete),
                    color = colorResource(id = R.color.gray)
                )
            }
        }
        Divider(Modifier.padding(top = 1.dp))
    }
}

@Preview(name = "emptyText", showBackground = true)
@Composable
private fun Preview1() {
    MeeronActionBox(ContentFactory.LimitTextField(
        text = "아무거나 쳐볼까용",
        isEssential = true,
        limit = 10,
        onValueChange = {}
    ), "Meeron")
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    MeeronActionBox(
        ContentFactory.ActionField(text = "아무거나 ㅋㅋㅋ", isEssential = false, onClick = {}),
        "Meeron",
        showIcon = true
    )

}
