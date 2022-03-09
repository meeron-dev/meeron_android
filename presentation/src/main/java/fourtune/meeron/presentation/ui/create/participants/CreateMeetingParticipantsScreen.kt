package fourtune.meeron.presentation.ui.create.participants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.Team
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.create.CreateText
import fourtune.meeron.presentation.ui.create.CreateTitle

@Composable
fun CreateMeetingParticipantsScreen(
    viewModel: CreateMeetingParticipantsViewModel = hiltViewModel(),
    onAction: () -> Unit,
    onNext: (meeting: Meeting) -> Unit,
    onPrevious: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    CreateMeetingParticipantsScreen(
        uiState = uiState,
        event = { event ->
            when (event) {
                CreateMeetingParticipantsViewModel.Event.Action -> onAction()
                CreateMeetingParticipantsViewModel.Event.Next -> onNext(uiState.meeting)
                CreateMeetingParticipantsViewModel.Event.Previous -> onPrevious()
            }
        }
    )
}

@Composable
private fun CreateMeetingParticipantsScreen(
    uiState: CreateMeetingParticipantsViewModel.UiState,
    event: (CreateMeetingParticipantsViewModel.Event) -> Unit = {}
) {
    Scaffold(topBar = {
        CenterTextTopAppBar(
            onAction = { event(CreateMeetingParticipantsViewModel.Event.Action) },
            text = {
                Text(
                    text = stringResource(id = R.string.create_meeting),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.black)
                )
            })
    }) {
        MeeronButtonBackGround(
            modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
            rightText = "완료",
            rightClick = { event(CreateMeetingParticipantsViewModel.Event.Next) },
            leftClick = { event(CreateMeetingParticipantsViewModel.Event.Previous) }
        ) {
            Column {
                CreateTitle(
                    title = R.string.create_participants,
                    selectedTime = uiState.meeting.time,
                    selectedDate = uiState.meeting.date,
                    extraContents = {
                        CreateText(text = uiState.meeting.title)
                    }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%d명 선택됨", 0),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.dark_primary)
                    )
                    Image(painter = painterResource(id = R.drawable.ic_home_search), contentDescription = "search")
                }

                Spacer(modifier = Modifier.padding(4.dp))
                TeamExpandItem(uiState.teams)
                Spacer(modifier = Modifier.padding(10.dp))
                //TODO 여기에 팀 선택화면 들어가기 (재활용 가능해보임 데이터셋 나오면 적용하자)
                // @See OwnerSelectScreen


            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun TeamExpandItem(teams: List<Team>) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Image(
                painter = painterResource(id = if (expanded) R.drawable.ic_expanded else R.drawable.ic_expand),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(6.dp))
            Text(text = "팀이름", fontSize = 18.sp, color = colorResource(id = R.color.dark_gray))
        }
        Spacer(modifier = Modifier.padding(10.dp))

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 36.dp),
                verticalArrangement = Arrangement.spacedBy(35.dp)
            ) {
                items(teams) {
                    Text(
                        text = it.name,
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.gray)
                    )
                }
            }
        }
    }


}


@Preview
@Composable
private fun Preview() {
    CreateMeetingParticipantsScreen(CreateMeetingParticipantsViewModel.UiState(Meeting()))
}