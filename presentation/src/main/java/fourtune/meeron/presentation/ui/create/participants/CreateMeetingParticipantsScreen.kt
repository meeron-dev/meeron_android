package fourtune.meeron.presentation.ui.create.participants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.create.CreateText
import fourtune.meeron.presentation.ui.create.CreateTitle

@Composable
fun CreateMeetingParticipantsScreen(viewModel: CreateMeetingParticipantsViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()
    CreateMeetingParticipantsScreen(uiState = uiState)
}

@Composable
private fun CreateMeetingParticipantsScreen(uiState: CreateMeetingParticipantsViewModel.UiState) {
    Scaffold(topBar = {
        CenterTextTopAppBar(
            onAction = { /*TODO*/ },
            text = {
                Text(
                    text = stringResource(id = R.string.create_meeting),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.black)
                )
            })
    }) {
        MeeronButtonBackGround(modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp)) {
            Column {
                CreateTitle(
                    title = R.string.create_participants,
                    selectedTime = "${uiState.startTime} ~ ${uiState.endTime}",
                    selectedDate = uiState.date.toString(),
                    extraContents = {
                        CreateText(text = uiState.title)
                    }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%d명 선택됨", 1),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.dark_primary)
                    )
                    Image(painter = painterResource(id = R.drawable.ic_home_search), contentDescription = "search")
                }
            }
        }
    }
}