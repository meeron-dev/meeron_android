package fourtune.meeron.presentation.ui.createmeeting.participants

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.UserGrids
import fourtune.meeron.presentation.ui.common.bottomsheet.UserSelectScreen
import fourtune.meeron.presentation.ui.createmeeting.CreateText
import fourtune.meeron.presentation.ui.createmeeting.CreateTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateMeetingParticipantsScreen(
    viewModel: CreateMeetingParticipantsViewModel = hiltViewModel(),
    onAction: () -> Unit,
    onNext: (meeting: Meeting) -> Unit,
    onPrevious: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val bottomSheet = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    var searchUserText by remember {
        mutableStateOf("")
    }

    val selectedUsers = remember {
        mutableStateListOf<WorkspaceUser>()
    }

    BackHandler {
        if (bottomSheet.isVisible) {
            scope.launch { bottomSheet.hide() }
        } else {
            onPrevious()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheet,
        sheetContent = {
            UserSelectScreen(
                title = stringResource(R.string.participant_select_title),
                users = uiState.searchedUsers,
                onSearch = {
                    searchUserText = it
                    viewModel.onSearch(it)
                },
                onComplete = { selectedUsers ->
                    //todo 완료 누르면 추가하도록 변경해야 함
                    scope.launch { bottomSheet.hide() }
                },
                selectedUsers = selectedUsers,
                searchText = searchUserText,
                ownerIds = uiState.meeting.ownerIds
            )

        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        content = {
            CreateMeetingParticipantsScreen(
                event = { event ->
                    when (event) {
                        CreateMeetingParticipantsViewModel.Event.Action -> onAction()
                        is CreateMeetingParticipantsViewModel.Event.Next -> {
                            onNext(uiState.meeting.copy(participants = event.participants))
                        }
                        CreateMeetingParticipantsViewModel.Event.Previous -> onPrevious()
                        is CreateMeetingParticipantsViewModel.Event.SelectTeam -> viewModel.getTeamMembers(event.id)
                    }
                },
                meeting = uiState.meeting,
                fixedOwners = uiState.fixedOwner,
                teams = uiState.teams,
                teamMembers = uiState.teamMembers,
                selectedUsers = selectedUsers,
                openDialog = { scope.launch { bottomSheet.animateTo(ModalBottomSheetValue.Expanded) } }
            )
        }
    )
}

@Composable
private fun CreateMeetingParticipantsScreen(
    event: (CreateMeetingParticipantsViewModel.Event) -> Unit = {},
    meeting: Meeting,
    fixedOwners: List<WorkspaceUser>,
    teams: List<Team>,
    teamMembers: List<WorkspaceUser>,
    selectedUsers: SnapshotStateList<WorkspaceUser>,
    openDialog: () -> Unit = {}
) {


    LaunchedEffect(fixedOwners.size) {
        selectedUsers.addAll(fixedOwners)
    }

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
            rightText = "완료",
            rightClick = { event(CreateMeetingParticipantsViewModel.Event.Next(selectedUsers)) },
            leftClick = { event(CreateMeetingParticipantsViewModel.Event.Previous) }
        ) {
            Column {
                CreateTitle(
                    title = R.string.create_participants,
                    selectedTime = meeting.time,
                    selectedDate = meeting.date.displayString(),
                    extraContents = {
                        CreateText(text = meeting.title)
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
                        text = String.format("%d명 선택됨", selectedUsers.size),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.dark_primary)
                    )
                    Image(
                        modifier = Modifier.clickable(onClick = openDialog),
                        painter = painterResource(id = R.drawable.ic_home_search),
                        contentDescription = "search"
                    )
                }

                Spacer(modifier = Modifier.padding(4.dp))
                TeamExpandItem(
                    teams = teams,
                    onSelectTeam = { event(CreateMeetingParticipantsViewModel.Event.SelectTeam(it)) }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                UserGrids(
                    displayUsers = teamMembers,
                    selectedUsers = selectedUsers,
                    ownerIds = meeting.ownerIds
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun TeamExpandItem(teams: List<Team>, onSelectTeam: (Long) -> Unit = {}) {
    var expanded by remember {
        mutableStateOf(true)
    }

    val firstTeam = teams.firstOrNull() ?: return
    val remainTeams = teams - firstTeam
    var selected by remember {
        mutableStateOf(firstTeam.id)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selected = firstTeam.id
                        onSelectTeam(firstTeam.id)
                    }
            ) {
                Text(
                    text = firstTeam.name,
                    fontSize = 18.sp,
                    color = colorResource(id = if (firstTeam.id == selected) R.color.dark_gray else R.color.gray)
                )
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 36.dp),
                verticalArrangement = Arrangement.spacedBy(35.dp)
            ) {
                items(remainTeams, key = { it.id }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selected = it.id
                                onSelectTeam(it.id)
                            }
                    ) {
                        Text(
                            text = it.name,
                            fontSize = 18.sp,
                            color = colorResource(id = if (it.id == selected) R.color.dark_gray else R.color.gray)
                        )
                    }
                }
            }
        }
    }


}


@Preview
@Composable
private fun Preview() {
    CreateMeetingParticipantsScreen(onAction = {}, onPrevious = {}, onNext = {})
}