package fourtune.meeron.presentation.ui.create.information

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox
import fourtune.meeron.presentation.ui.common.bottomsheet.NoneScreen
import fourtune.meeron.presentation.ui.common.bottomsheet.OwnersSelectScreen
import fourtune.meeron.presentation.ui.common.bottomsheet.TeamSelectScreen
import fourtune.meeron.presentation.ui.create.CreateTitle
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateMeetingInfoScreen(
    viewModel: CreateMeetingInfoViewModel = hiltViewModel(),
    onNext: () -> Unit = {},
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
                CreateMeetingInfoViewModel.BottomSheetState.Owner -> OwnersSelectScreen()
                CreateMeetingInfoViewModel.BottomSheetState.Team -> TeamSelectScreen()
                else -> NoneScreen()
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        content = {
            CreateMeetingInfoScreen(
                uiState = uiState,
                onLoad = onLoad,
                viewModel = viewModel,
                onPrevious = onPrevious,
                onNext = onNext,
                event = { event ->
                    currentBottomSheet = event
                    scope.launch { bottomSheetState.animateTo(ModalBottomSheetValue.Expanded) }
                }
            )
        }
    )

}

@Composable
private fun CreateMeetingInfoScreen(
    uiState: CreateMeetingInfoViewModel.UiState,
    onLoad: () -> Unit,
    viewModel: CreateMeetingInfoViewModel,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    event: (CreateMeetingInfoViewModel.BottomSheetState) -> Unit = {}
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
        MeeronButtonBackGround(
            modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
            leftClick = onPrevious,
            rightClick = onNext,
            rightEnable = uiState.isVerify
        ) {
            Column {
                InformationTitle(
                    selectedDate = uiState.date,
                    selectedTime = uiState.time,
                    onClick = onLoad
                )
                InformationFields(
                    listState = viewModel.listState,
                    clickModal = { info ->
                        when (info) {
                            CreateMeetingInfoViewModel.Info.Owners -> event(CreateMeetingInfoViewModel.BottomSheetState.Owner)
                            CreateMeetingInfoViewModel.Info.Team -> event(CreateMeetingInfoViewModel.BottomSheetState.Team)
                            else -> throw IllegalStateException("$info is Not Modal")
                        }
                        viewModel.clickModal(info)
                    },
                    onTextChange = viewModel::updateText
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
            MeeronActionBox(factory = factory, title = stringResource(id = info.title))
        }
    }
}


@Composable
private fun InformationTitle(selectedDate: String, selectedTime: String, onClick: () -> Unit) {
    CreateTitle(
        title = R.string.info_title,
        selectedDate = selectedDate,
        selectedTime = selectedTime,
        extraContents = {
            Button(
                onClick = onClick,
                contentPadding = PaddingValues(vertical = 3.dp, horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.medium_primary)),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = stringResource(R.string.load),
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.light_gray_white)
                )
            }
        }
    )
}

@Preview
@Composable
private fun CreateMeetingPrev() {
    CreateMeetingInfoScreen()
}