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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronClickableText
import fourtune.meeron.presentation.ui.common.MeeronTextField
import fourtune.meeron.presentation.ui.common.MerronButton

enum class Info(
    @StringRes val title: Int,
    val isEssential: Boolean = false,
    val limit: Int = 0,
    val isModal: Boolean,
) {
    Title(R.string.meeting_title, true, 30, false),
    Personality(R.string.meeting_personality, true, 10, false),
    Owners(R.string.public_owners, isEssential = false, limit = 0, true),
    Team(R.string.charged_team, isEssential = true, limit = 0, true)
}

@Composable
fun CreateMeetingInfoScreen(
    viewModel: CreateMeetingInfoViewModel = hiltViewModel(),
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onLoad: () -> Unit = {}
) {
    val uiState by viewModel.uiState().collectAsState()

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
                InformationTitle(
                    selectedDate = uiState.date.toString(),
                    selectedTime = "${uiState.startTime.time}${uiState.startTime.hourOfDay} ~ ${uiState.endTime.time}${uiState.endTime.hourOfDay}",
                    onClick = onLoad
                )
                InformationColumn(viewModel.listState, viewModel::clickModal, viewModel::updateText)
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
private fun InformationColumn(
    listState: Map<Info, String>,
    clickModal: (Info) -> Unit,
    onTextChange: (Info, String) -> Unit
) {
    Info.values().forEach { info ->
        Spacer(modifier = Modifier.padding(19.dp))
        if (info.isModal) {
            MeeronClickableText(
                title = stringResource(id = info.title),
                isEssential = info.isEssential,
                limit = info.limit,
                text = listState[info].orEmpty(),
                onClick = { clickModal(info) }
            )
        } else {
            MeeronTextField(
                title = stringResource(id = info.title),
                isEssential = info.isEssential,
                limit = info.limit,
                text = listState[info].orEmpty(),
                onValueChange = { onTextChange(info, it) }
            )
        }
    }
}


@Composable
private fun InformationTitle(selectedDate: String, selectedTime: String, onClick: () -> Unit) {
    Text(text = "회의의 기본 정보를\n입력해 주세요.", fontSize = 25.sp, color = colorResource(id = R.color.black))
    Spacer(modifier = Modifier.padding(4.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(text = selectedDate, fontSize = 13.sp, color = colorResource(id = R.color.light_gray))
            Text(text = selectedTime, fontSize = 13.sp, color = colorResource(id = R.color.light_gray))
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

@Preview
@Composable
private fun CreateMeetingPrev() {
    CreateMeetingInfoScreen()
}