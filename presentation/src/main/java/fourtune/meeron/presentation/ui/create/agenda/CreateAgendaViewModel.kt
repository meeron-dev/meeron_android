package fourtune.meeron.presentation.ui.create.agenda

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.FileProvider
import forutune.meeron.domain.model.FileInfo
import forutune.meeron.domain.model.Issue
import forutune.meeron.domain.model.Meeting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AgendaState(
    val name: String = "",
    val issue: SnapshotStateList<String> = mutableStateListOf(""),
    val file: SnapshotStateList<FileInfo> = mutableStateListOf()
)

@HiltViewModel
class CreateAgendaViewModel @Inject constructor(
    private val fileProvider: FileProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val agendas = mutableStateListOf(AgendaState())

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    meeting = requireNotNull(savedStateHandle[Const.Meeting])
                )
            }
        }
    }

    fun addAgenda() {
        if (agendas.size < MAX_AGENDA_SIZE) {
            agendas.add(AgendaState())
            selectAgenda(agendas.size - 1)
        }
    }

    fun deleteAgenda(index: Int) {
        if (agendas.size > MIN_AGENDA_SIZE) {
            agendas.removeAt(index)
            selectAgenda(agendas.size - 1)
        }
    }

    fun onAgendaTextChange(selected: Int, text: String) {
        agendas[selected] = agendas[selected].copy(name = text)
    }

    fun addIssue(selected: Int) {
        agendas[selected].issue.add("")
    }

    fun deleteIssue(selectedAgenda: Int, selectedIssue: Int) {
        if (agendas[selectedAgenda].issue.size > 1) {
            agendas[selectedAgenda].issue.removeAt(selectedIssue)
        }
    }

    fun addFile(uri: Uri) {
        val fileName = fileProvider.getFileName(uri.toString())
        agendas[uiState.value.selectedAgenda].file.add(FileInfo(uri.toString(), fileName))
    }

    fun deleteFile(selected: Int, selectedFile: Int) {
        if (agendas[selected].file.size >= 1) {
            agendas[selected].file.removeAt(selectedFile)
        }
    }

    fun onChangeIssue(selectedAgenda: Int, selectedIssue: Int, text: String) {
        agendas[selectedAgenda].issue[selectedIssue] = text
    }

    fun selectAgenda(selected: Int) {
        _uiState.update {
            it.copy(selectedAgenda = selected)
        }
    }

    fun saveSnapShot(): Meeting {
        return uiState.value.meeting.copy(
            agenda = agendas.mapIndexed { index, agenda ->
                forutune.meeron.domain.model.Agenda(
                    order = index.toLong(),
                    name = agenda.name,
                    issues = agenda.issue.map(::Issue),
                    fileInfos = agenda.file
                )
            }
        )
    }

    data class UiState(
        val meeting: Meeting = Meeting(),
        val selectedAgenda: Int = 0
    )

    sealed interface Event {
        object Exit : Event
        object Previous : Event
        object Next : Event

        object AddAgenda : Event
        class DeleteAgenda(val index: Int) : Event
        class AgendaTextChanged(val selected: Int, val text: String) : Event

        class AddIssue(val index: Int) : Event
        class OnChangedIssue(val selectedAgenda: Int, val selectedIssue: Int, val text: String) : Event
        class DeleteIssue(val selectedAgenda: Int, val selectedIssue: Int) : Event

        object AddFile : Event
        class DeleteFile(val selectedAgenda: Int, val selectedFile: Int) : Event

        class AgendaSelected(val selected: Int) : Event
    }

    companion object {
        const val MAX_AGENDA_SIZE = 5
        const val MIN_AGENDA_SIZE = 1
    }
}