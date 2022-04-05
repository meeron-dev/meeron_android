package fourtune.meeron.presentation.ui.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Agenda
import forutune.meeron.domain.model.FileInfo
import forutune.meeron.domain.model.Issue
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.topbar.DetailTopBar

@Composable
fun AgendaDetailScreen(viewModel: AgendaDetailViewModel = hiltViewModel(), onBack: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri == null) {
                //todo file download (데이터 레이어로 넘기자..!!!!)
            } else {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                //todo 우선 내 파일 열리는지 확인하기.
            }
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    var selected by remember {
        mutableStateOf(1)
    }
    Scaffold(
        topBar = {
            AgendaDetailTopBar(
                agendas = uiState.meeting.agenda,
                selectedIndex = selected,
                onSelect = { selected = it },
                onBack = onBack
            )
        },
        content = {
            AgendaDetailContent(
                agenda = uiState.meeting.agenda[selected],
                openFile = { launcher.launch("*/*") }
            )
        }
    )
}

@Composable
fun AgendaDetailTopBar(
    agendas: List<Agenda> = emptyList(),
    selectedIndex: Int,
    onSelect: (Int) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val agendaOrder = remember {
        agendas.map { it.order }.toMutableStateList()
    }


    DetailTopBar("아젠다", onBack) {
        Spacer(modifier = Modifier.padding(10.dp))
        LazyRow(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
            itemsIndexed(agendaOrder) { index, order ->
                Text(
                    modifier = Modifier.clickable { onSelect(index) },
                    text = "$order",
                    fontSize = 24.sp,
                    color = colorResource(id = if (index == selectedIndex) R.color.dark_primary else R.color.gray),
                    fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.ExtraLight
                )
            }
        }
    }
}

@Composable
fun AgendaDetailContent(agenda: Agenda, openFile: (url: String) -> Unit = {}) {
    Column {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.padding(25.dp))
            Text(
                text = agenda.name,
                fontSize = 16.sp,
                color = colorResource(id = R.color.black),
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.padding(25.dp))
        }
        Divider(color = Color(0xffe7e7e7))
        LazyColumn(contentPadding = PaddingValues(vertical = 30.dp, horizontal = 20.dp)) {
            itemsIndexed(agenda.issues) { index, issue ->
                IssueItem(index, issue)
            }
            item {
                Spacer(modifier = Modifier.padding(30.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.ic_meeting_clip), contentDescription = null)
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = "자료", fontSize = 18.sp, color = colorResource(id = R.color.black))
                }
                Spacer(modifier = Modifier.padding(14.dp))
            }
            items(agenda.fileInfos) { fileInfo ->
                Text(
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .clickable { openFile(fileInfo.uriString) },
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(fileInfo.fileName)
                        }
                    }, fontSize = 15.sp,
                    color = colorResource(id = R.color.dark_primary)
                )
            }
        }
    }
}

@Composable
private fun IssueItem(index: Int, issue: Issue) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${index + 1}", fontSize = 15.sp, color = colorResource(id = R.color.black))
        Spacer(modifier = Modifier.padding(19.dp))
        Text(text = issue.issue, fontSize = 15.sp, color = colorResource(id = R.color.black))
    }
}

@Preview
@Composable
private fun Preview1() {
    AgendaDetailTopBar(
        agendas = listOf(
            Agenda(1, "agenda1"),
            Agenda(2, "agenda2"),
            Agenda(3, "agenda3"),
            Agenda(4, "agenda4"),
        ),
        selectedIndex = 1
    )
}

@Preview
@Composable
private fun Preview2() {
    AgendaDetailContent(
        Agenda(
            order = 1,
            name = "agenda1",
            issues = listOf(
                Issue("첫번 째 이슈입니다."),
                Issue("두번 째 이슈입니다."),
                Issue("세번 째 이슈입니다."),
            ),
            fileInfos = listOf(
                FileInfo("", "fileName.mp4"),
                FileInfo("", "zero.jpg"),
                FileInfo("", "adsasdjdhfdjlafhakdfhkfadfafdadf.avi"),
            )
        )
    )
}