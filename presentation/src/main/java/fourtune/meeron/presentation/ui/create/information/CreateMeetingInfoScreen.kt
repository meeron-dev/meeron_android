package fourtune.meeron.presentation.ui.create.information

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronTextField
import fourtune.meeron.presentation.ui.common.MerronButton

private enum class MeetingInfo(@StringRes val text: Int, val isEssential: Boolean = false, val limit: Int = 0) {
    Title(R.string.meeting_title, true, 30),
    Personality(R.string.meeting_personality, true, 10),
    Owners(R.string.public_owners),
    Team(R.string.charged_team, true)
}

@Composable
fun CreateMeetingInfoScreen(
    viewModel: CreateMeetingInfoViewModel = hiltViewModel(),
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onLoad: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = { },
                text = {
                    Text(
                        text = stringResource(id = R.string.create_meeting),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black)
                    )
                }
            )
        },
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(vertical = 40.dp, horizontal = 20.dp)
                .fillMaxSize()
                .scrollable(state = scrollState, orientation = Orientation.Vertical),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                InformationTitle(onClick = onLoad)
                TextFields(onChangeText = viewModel::updateText)
            }
            MerronButton(
                leftClick = onPrevious,
                rightClick = {
                    onNext()
                }
            )
        }
    }
}

@Composable
private fun InformationTitle(onClick: () -> Unit) {
    Text(text = "회의의 기본 정보를\n입력해 주세요.", fontSize = 25.sp, color = colorResource(id = R.color.black))
    Spacer(modifier = Modifier.padding(4.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(text = "선택날짜 들어가면댐", fontSize = 13.sp, color = colorResource(id = R.color.light_gray))
            Text(text = "선택 시간 들어가면 댐", fontSize = 13.sp, color = colorResource(id = R.color.light_gray))
        }
        Button(
            onClick = onClick,
            contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.medium_primary)),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(text = "불러오기", fontSize = 15.sp, color = colorResource(id = R.color.light_gray_white))
        }
    }
}

@Composable
private fun TextFields(onChangeText: (Int, String) -> Unit) {
    MeetingInfo.values().forEach {
        Spacer(modifier = Modifier.padding(19.dp))
        var text by remember {
            mutableStateOf("")
        }
        MeeronTextField(
            stringResource(it.text) + if (it.isEssential) " *" else "",
            isEssential = it.isEssential,
            limit = it.limit,
            text = text,
            onValueChange = { input ->
                text = input
                onChangeText(it.ordinal, input)
            }
        )
    }
}

@Preview
@Composable
private fun CreateMeetingPrev() {
    CreateMeetingInfoScreen()
}