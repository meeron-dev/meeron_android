package fourtune.meeron.presentation.ui.createworkspace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround

@Composable
fun JoinScreen(close: () -> Unit = {}) {
    val text = remember {
        listOf(
            "워크 스페이스\n초대 링크를 공유 받으세요.",
            "워크 스페이스에서의\n프로필을 작성해보세요.",
            "작성을 완료하면\n워크 스페이스에 무사히\n참여할 수 있어요."
        )
    }

    Scaffold(
        topBar = {
            CenterTextTopAppBar("워크 스페이스 참여")
        },
        content = {
            MeeronSingleButtonBackGround(
                modifier = Modifier.padding(bottom = 50.dp, start = 38.dp, end = 38.dp, top = 32.dp),
                text = "닫기",
                onClick = close
            ) {
                Spacer(modifier = Modifier.padding(32.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(text) { index, s ->
                        Description("${index + 1}", s)
                    }
                }
            }
        }
    )
}

@Composable
private fun Description(index: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = index, fontSize = 53.sp, color = colorResource(id = R.color.dark_primary))
        Spacer(modifier = Modifier.padding(17.dp))
        Text(text = text, fontSize = 16.sp, color = colorResource(id = R.color.black))
    }
}

@Preview
@Composable
private fun Preview() {
    JoinScreen()
}