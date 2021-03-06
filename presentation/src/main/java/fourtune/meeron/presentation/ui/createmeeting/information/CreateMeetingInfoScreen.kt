package fourtune.meeron.presentation.ui.createmeeting.information

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox
import fourtune.meeron.presentation.ui.common.bottomsheet.NoneScreen
import fourtune.meeron.presentation.ui.common.bottomsheet.TeamSelectScreen
import fourtune.meeron.presentation.ui.common.bottomsheet.UserSelectScreen
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.createmeeting.CreateTitle
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateMeetingInfoScreen(
    viewModel: CreateMeetingInfoViewModel = hiltViewModel(),
    onAction: () -> Unit = {},
    onNext: (meeting: Meeting) -> Unit = {},
    onPrevious: () -> Unit = {},
    onLoad: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState().collectAsState()
    var currentBottomSheet: CreateMeetingInfoViewModel.BottomSheetState? by remember {
        mutableStateOf(null)
    }
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    var searchOwnerText by remember {
        mutableStateOf("")
    }

    val selectedOwners = remember {
        mutableStateListOf<WorkspaceUser>()
    }

    BackHandler {
        if (bottomSheetState.isVisible) {
            scope.launch { bottomSheetState.hide() }
        } else {
            onPrevious()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            when (currentBottomSheet) {
                CreateMeetingInfoViewModel.BottomSheetState.Owner -> {
                    UserSelectScreen(
                        title = stringResource(R.string.owner_select_title),
                        users = uiState.searchedUsers,
                        onSearch = {
                            searchOwnerText = it
                            viewModel.onSearch(it)
                        },
                        onComplete = { selectedOwners ->
                            scope.launch { bottomSheetState.hide() }
                            viewModel.selectOwner(selectedOwners)
                        },
                        selectedUsers = selectedOwners,
                        searchText = searchOwnerText,
                        ownerIds = uiState.meeting.ownerIds
                    )
                }
                CreateMeetingInfoViewModel.BottomSheetState.Team -> TeamSelectScreen(
                    teams = uiState.teams,
                    onSelectTeam = { selectedTeamId ->
                        scope.launch { bottomSheetState.hide() }
                        viewModel.selectTeam(selectedTeamId)
                    }
                )
                else -> NoneScreen()
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        content = {
            CreateMeetingInfoScreen(
                uiState = uiState,
                event = { event ->
                    when (event) {
                        CreateMeetingInfoViewModel.Event.Load -> onLoad()
                        CreateMeetingInfoViewModel.Event.Next -> {
                            viewModel.createMeeting(onNext)
                        }
                        CreateMeetingInfoViewModel.Event.Previous -> onPrevious()
                        is CreateMeetingInfoViewModel.Event.OnTextChange -> viewModel.updateText(
                            event.info,
                            event.input
                        )
                        CreateMeetingInfoViewModel.Event.Action -> onAction()
                    }
                },
                bottomSheetEvent = { event ->
                    currentBottomSheet = event
                    if (event == CreateMeetingInfoViewModel.BottomSheetState.Owner) {
                        viewModel.clearOwner()
                        searchOwnerText = ""
                        selectedOwners.clear()
                    }
                    scope.launch { bottomSheetState.animateTo(ModalBottomSheetValue.Expanded) }
                },
                listState = viewModel.listState
            )
        }
    )

}

@Composable
private fun CreateMeetingInfoScreen(
    uiState: CreateMeetingInfoViewModel.UiState,
    event: (CreateMeetingInfoViewModel.Event) -> Unit,
    bottomSheetEvent: (CreateMeetingInfoViewModel.BottomSheetState) -> Unit = {},
    listState: Map<CreateMeetingInfoViewModel.Info, String>
) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = { event(CreateMeetingInfoViewModel.Event.Action) },
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
        MeeronButtonBackGround(
            leftClick = { event(CreateMeetingInfoViewModel.Event.Previous) },
            rightClick = { event(CreateMeetingInfoViewModel.Event.Next) },
            rightEnable = uiState.isVerify
        ) {
            Column {
                InformationTitle(
                    selectedDate = uiState.meeting.date.displayString(),
                    selectedTime = uiState.meeting.time,
                    onClick = { event(CreateMeetingInfoViewModel.Event.Load) }
                )
                InformationFields(
                    listState = listState,
                    clickModal = { info ->
                        when (info) {
                            CreateMeetingInfoViewModel.Info.Owners -> bottomSheetEvent(CreateMeetingInfoViewModel.BottomSheetState.Owner)
                            CreateMeetingInfoViewModel.Info.Team -> bottomSheetEvent(CreateMeetingInfoViewModel.BottomSheetState.Team)
                            else -> throw IllegalStateException("$info is Not Modal")
                        }
                    },
                    onTextChange = { info, input -> event(CreateMeetingInfoViewModel.Event.OnTextChange(info, input)) }
                )
            }
        }
    }
}

@Composable
private fun InformationFields(
    listState: Map<CreateMeetingInfoViewModel.Info, String>,
    clickModal: (CreateMeetingInfoViewModel.Info) -> Unit,
    onTextChange: (CreateMeetingInfoViewModel.Info, String) -> Unit
) {
    Column(Modifier.verticalScroll(state = rememberScrollState())) {
        CreateMeetingInfoViewModel.Info.values().forEach { info ->
            Spacer(modifier = Modifier.padding(19.dp))
            val factory = if (info.isModal) {
                ContentFactory.ActionField(
                    isEssential = info.isEssential,
                    text = listState[info].orEmpty(),
                    onClick = { clickModal(info) }
                )
            } else {
                ContentFactory.LimitTextField(
                    text = listState[info].orEmpty(),
                    onValueChange = { onTextChange(info, it) },
                    isEssential = info.isEssential,
                    limit = info.limit,
                )
            }
            MeeronActionBox(
                factory = factory,
                title = stringResource(id = info.title) + if (info.isEssential) " *" else ""
            )
        }
    }
}


@Composable
private fun InformationTitle(selectedDate: String, selectedTime: String, onClick: () -> Unit) {
    CreateTitle(
        title = R.string.info_title,
        selectedDate = selectedDate,
        selectedTime = selectedTime,
//        extraContents = {
//            Button(
//                onClick = onClick,
//                contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
//                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.medium_primary)),
//                shape = RoundedCornerShape(30.dp)
//            ) {
//                Text(
//                    text = stringResource(R.string.load),
//                    fontSize = 15.sp,
//                    color = colorResource(id = R.color.light_gray_white)
//                )
//            }
//        }
    )
}

@Preview
@Composable
private fun CreateMeetingPrev() {
    CreateMeetingInfoScreen()
}

@Preview
@Composable
private fun Preview() {
    InformationTitle(selectedDate = "asdasd", selectedTime = "asdsad") {

    }
}