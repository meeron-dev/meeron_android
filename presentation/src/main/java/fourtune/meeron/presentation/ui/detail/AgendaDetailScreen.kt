package fourtune.meeron.presentation.ui.detail

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AgendaDetailScreen(viewModel: AgendaDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            AgendaDetailTopBar()
        },
        content = {
            AgendaDetailContent()
        }
    )
}

@Composable
fun AgendaDetailTopBar() {

}

@Composable
fun AgendaDetailContent() {

}

@Preview
@Composable
private fun Preview1() {
    AgendaDetailTopBar()
}

@Preview
@Composable
private fun Preview2() {
    AgendaDetailContent()
}