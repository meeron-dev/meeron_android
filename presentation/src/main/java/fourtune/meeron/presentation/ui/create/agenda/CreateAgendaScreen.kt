package fourtune.meeron.presentation.ui.create.agenda

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox
import fourtune.meeron.presentation.ui.create.CreateText
import fourtune.meeron.presentation.ui.create.CreateTitle
import fourtune.meeron.presentation.ui.create.agenda.CreateAgendaViewModel.Companion.MAX_AGENDA_SIZE
import fourtune.meeron.presentation.ui.create.agenda.CreateAgendaViewModel.Companion.MIN_AGENDA_SIZE
import fourtune.meeron.presentation.ui.theme.MeeronTheme

@Composable
fun CreateAgendaScreen(
    viewModel: CreateAgendaViewModel = hiltViewModel(),
    onAction: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateAgendaScreen(
        uiState = uiState,
        event = { event ->
            when (event) {
                CreateAgendaViewModel.Event.AddAgenda -> viewModel.addAgenda()
                is CreateAgendaViewModel.Event.DeleteAgenda -> viewModel.deleteAgenda(event.index)
                is CreateAgendaViewModel.Event.AgendaTextChanged -> viewModel.onAgendaTextChange(
                    event.selected,
                    event.text
                )
                is CreateAgendaViewModel.Event.AddIssue -> viewModel.addIssue(event.index)
                is CreateAgendaViewModel.Event.OnChangedIssue -> viewModel.onChangeIssue(
                    event.selectedAgenda,
                    event.selectedIssue,
                    event.text
                )
                is CreateAgendaViewModel.Event.DeleteIssue -> viewModel.deleteIssue(
                    event.selectedAgenda,
                    event.selectedIssue
                )
                is CreateAgendaViewModel.Event.AddFile -> {
                    viewModel.addFile(event.selectedAgenda)
                }
                is CreateAgendaViewModel.Event.DeleteFile -> viewModel.deleteFile(
                    event.selectedAgenda,
                    event.selectedFile
                )

                CreateAgendaViewModel.Event.Exit -> onAction()
                CreateAgendaViewModel.Event.Next -> onNext()
                CreateAgendaViewModel.Event.Previous -> onPrevious()

                is CreateAgendaViewModel.Event.AgendaSelected -> viewModel.selectAgenda(event.selected)
            }
        },
        agendas = viewModel.agendas
    )
}

@Composable
private fun CreateAgendaScreen(
    uiState: CreateAgendaViewModel.UiState,
    event: (CreateAgendaViewModel.Event) -> Unit,
    agendas: List<Agenda>
) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = { event(CreateAgendaViewModel.Event.Exit) },
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
            rightClick = { event(CreateAgendaViewModel.Event.Next) },
            leftClick = { event(CreateAgendaViewModel.Event.Previous) }
        ) {
            Column {
                CreateTitle(
                    title = R.string.info_title,
                    selectedDate = uiState.date.toString(),
                    selectedTime = "${uiState.startTime} ~ ${uiState.endTime}",
                    extraContents = { CreateText(text = uiState.title) }
                )
                Spacer(modifier = Modifier.padding(15.dp))
                AgendaBody(
                    agendas = agendas,
                    event = event,
                    selected = uiState.selectedAgenda,
                )
            }
        }
    }
}

@Composable
fun AgendaBody(
    agendas: List<Agenda>,
    event: (CreateAgendaViewModel.Event) -> Unit,
    selected: Int,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AgendaCountRow(agendas, event, selected, onClick = {
            event(CreateAgendaViewModel.Event.AgendaSelected(it))
        })
        Agenda(agendas, selected, event)
        Issues(agendas, selected, event)
        Files(agendas, selected, event)
        Spacer(modifier = Modifier.padding(30.dp))
        Text(
            modifier = Modifier.padding(vertical = 30.dp),
            text = "회의 생성 후에도 추가 및 편집이 가능합니다. ",
            fontSize = 14.sp,
            color = colorResource(id = R.color.gray)
        )
    }
}

@Composable
private fun AgendaCountRow(
    agendas: List<Agenda>,
    event: (CreateAgendaViewModel.Event) -> Unit,
    selected: Int,
    onClick: (Int) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            itemsIndexed(agendas) { index, _ ->
                AgendaCountItem(index = index, isSelected = selected, onClick = onClick)
            }
        }
        Row {
            if (agendas.size != MAX_AGENDA_SIZE) {
                IconButton(onClick = { event(CreateAgendaViewModel.Event.AddAgenda) }) {
                    Image(painter = painterResource(id = R.drawable.ic_agenda_plus), contentDescription = null)
                }
            }
            if (agendas.size != MIN_AGENDA_SIZE) {
                IconButton(onClick = { event(CreateAgendaViewModel.Event.DeleteAgenda(selected)) }) {
                    Image(painter = painterResource(id = R.drawable.ic_agenda_delete), contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun Agenda(
    agendas: List<Agenda>,
    selected: Int,
    event: (CreateAgendaViewModel.Event) -> Unit
) {
    Spacer(modifier = Modifier.padding(15.dp))
    MeeronActionBox(
        factory =
        ContentFactory.LimitTextField(
            text = agendas[selected].name,
            onValueChange = {
                event(CreateAgendaViewModel.Event.AgendaTextChanged(selected, it))
            },
            isEssential = false,
            easyDelete = false,
            limit = 48,
        ), title = stringResource(id = if (selected == 0) R.string.core_agenda else R.string.agenda)
    )
}

@Composable
private fun Issues(
    agendas: List<Agenda>,
    selected: Int,
    event: (CreateAgendaViewModel.Event) -> Unit
) {
    Spacer(modifier = Modifier.padding(15.dp))
    agendas[selected].issue.forEachIndexed { index, s ->
        Spacer(modifier = Modifier.padding(10.dp))
        if (index == 0) {
            MeeronActionBox(
                factory = ContentFactory.LimitTextField(
                    text = s,
                    onValueChange = { text ->
                        event(
                            CreateAgendaViewModel.Event.OnChangedIssue(
                                selected,
                                index,
                                text
                            )
                        )
                    },
                    isEssential = false,
                    easyDelete = true,
                    onClickDelete = { event(CreateAgendaViewModel.Event.DeleteIssue(selected, index)) },
                    limit = 0
                ), title = stringResource(id = R.string.issue),
                showIcon = true,
                onClick = { event(CreateAgendaViewModel.Event.AddIssue(selected)) }
            )
        } else {
            ContentFactory.LimitTextField(
                text = s,
                onValueChange = { text ->
                    event(
                        CreateAgendaViewModel.Event.OnChangedIssue(
                            selected,
                            index,
                            text
                        )
                    )
                },
                isEssential = false,
                easyDelete = true,
                onClickDelete = { event(CreateAgendaViewModel.Event.DeleteIssue(selected, index)) },
                limit = 0
            ).Create()
        }
    }
}

@Composable
private fun Files(
    agendas: List<Agenda>,
    selected: Int,
    event: (CreateAgendaViewModel.Event) -> Unit
) {
    Spacer(modifier = Modifier.padding(15.dp))
    agendas[selected].file.forEachIndexed { index, s ->
        Spacer(modifier = Modifier.padding(10.dp))
        if (index == 0) {
            MeeronActionBox(factory = ContentFactory.ActionField(
                text = s,
                onClick = { /* Do Nothing */ },
                easyDelete = true,
                onClickDelete = { CreateAgendaViewModel.Event.DeleteFile(selected, index) }
            ), title = stringResource(id = R.string.add_file),
                showIcon = true,
                onClick = { event(CreateAgendaViewModel.Event.AddFile(selected)) }
            )
        } else {
            ContentFactory.ActionField(
                text = s,
                onClick = { /* Do Nothing */ },
                easyDelete = true,
                onClickDelete = { CreateAgendaViewModel.Event.DeleteFile(selected, index) }
            ).Create()
        }
    }
}

@Composable
fun AgendaCountItem(index: Int, isSelected: Int, onClick: (Int) -> Unit) {
    Text(
        modifier = Modifier
            .clickable { onClick(index) }
            .padding(end = 23.dp),
        text = "${index + 1}",
        fontSize = 30.sp,
        color = colorResource(id = if (isSelected == index) R.color.primary else R.color.gray),
        fontWeight = if (isSelected == index) FontWeight.Bold else FontWeight.Medium
    )
}

@Preview
@Composable
private fun Preview() {
    MeeronTheme {
        CreateAgendaScreen()
    }
}