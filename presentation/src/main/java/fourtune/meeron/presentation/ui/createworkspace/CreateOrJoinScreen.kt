package fourtune.meeron.presentation.ui.createworkspace

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButton

private enum class Selection(
    @DrawableRes val selectDrawableRes: Int,
    @DrawableRes val unselectDrawableRes: Int,
    val text: String,
) {
    Create(
        selectDrawableRes = R.drawable.ic_ws_create_able,
        unselectDrawableRes = R.drawable.ic_ws_create_disable,
        text = "생성",
    ),
    Join(
        selectDrawableRes = R.drawable.ic_ws_participate_able,
        unselectDrawableRes = R.drawable.ic_ws_participate_disable,
        text = "참가"
    )
}

@Composable
fun CreateOrJoinScreen(onCreate: () -> Unit = {}, onJoin: () -> Unit = {}) {
    var selected: Selection? by remember {
        mutableStateOf(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.padding(33.dp))
            Text(text = "자, 이제\n미론을 시작해봐요", fontSize = 24.sp, color = colorResource(id = R.color.black))
            Spacer(modifier = Modifier.padding(6.dp))
            Text(text = "멋진 회의가 이루어질\n워크 스페이스가 필요해요", fontSize = 15.sp, color = colorResource(id = R.color.gray))
            Spacer(modifier = Modifier.padding(40.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Selection.values().forEach {
                    SelectionItem(it, selected == it) { selected = it }
                }
            }
        }
        MeeronSingleButton(
            modifier = Modifier.padding(bottom = 50.dp, start = 18.dp, end = 18.dp),
            onClick = {
                when (selected) {
                    Selection.Create -> onCreate()
                    Selection.Join -> onJoin()
                }
            },
            enable = selected != null
        )

    }
}

@Composable
private fun SelectionItem(selection: Selection, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = if (isSelected) selection.selectDrawableRes else selection.unselectDrawableRes),
            contentDescription = null
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = selection.text,
            fontSize = 18.sp,
            color = colorResource(id = if (isSelected) R.color.medium_primary else R.color.light_gray),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}


@Preview
@Composable
private fun Preview() {
    CreateOrJoinScreen()
}