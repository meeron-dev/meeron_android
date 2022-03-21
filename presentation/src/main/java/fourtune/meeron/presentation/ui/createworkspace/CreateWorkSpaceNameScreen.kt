package fourtune.meeron.presentation.ui.createworkspace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox

@Composable
fun CreateWorkSpaceNameScreen(onPrevious: () -> Unit = {}, onNext: (String) -> Unit = {}) {
    var workspaceName by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            CenterTextTopAppBar(text = "워크 스페이스 생성")
        },
        content = {
            MeeronButtonBackGround(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp),
                rightEnable = workspaceName.isNotEmpty(),
                rightClick = { onNext(workspaceName) },
                leftClick = onPrevious
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "워크 스페이스의\n이름을 정해주세요.", fontSize = 25.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(77.dp))
                    MeeronActionBox(
                        factory = ContentFactory.LimitTextField(
                            text = workspaceName,
                            onValueChange = { workspaceName = it },
                            limit = 10,
                            isEssential = false
                        ),
                        title = ""
                    )
                }
            }

        }
    )
}

@Preview
@Composable
private fun Preview() {
    CreateWorkSpaceNameScreen()
}