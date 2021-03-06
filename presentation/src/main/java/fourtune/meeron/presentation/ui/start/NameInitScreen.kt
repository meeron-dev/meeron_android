package fourtune.meeron.presentation.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.EntryPointType
import fourtune.meeron.presentation.ui.common.MeeronSingleButton
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox
import fourtune.meeron.presentation.ui.common.text.MeeronLogoText

@Composable
fun NameInitScreen(
    viewModel: NameInitViewModel = hiltViewModel(),
    goToCreateOrJoin: () -> Unit = {},
    goToCreateWorkspaceProfile: () -> Unit={}
) {
    var text by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            MeeronLogoText(text = "성함을\n입력해주세요.")
            MeeronActionBox(
                factory = ContentFactory.LimitTextField(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = text,
                    onValueChange = { text = it },
                    isEssential = false,
                    limit = 5
                ), title = ""
            )
        }
        MeeronSingleButton(
            modifier = Modifier.padding(bottom = 50.dp, start = 38.dp, end = 38.dp),
            onClick = {
                viewModel.saveName(text)
                when (viewModel.entryPointType) {
                    EntryPointType.Normal -> goToCreateOrJoin()
                    EntryPointType.DynamicLink -> goToCreateWorkspaceProfile()
                }

            },
            enable = text.isNotEmpty()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    NameInitScreen()
}