package fourtune.meeron.presentation.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.usecase.team.CreateTeamUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import forutune.meeron.domain.usecase.workspace.GetWorkSpaceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddTeamViewModel @Inject constructor(
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase,
    private val getWorkSpace: GetWorkSpaceUseCase,
    private val createTeam: CreateTeamUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                val workspaceId = getLatestWorkspaceId()
                val workspaceName = getWorkSpace(workspaceId)
                _uiState.update {
                    it.copy(workspaceName = workspaceName.workSpaceName)
                }
            }.onFailure { Timber.tag("🔥zero:addTeamVM").e("$it") }
        }
    }

    fun addTeam(teamName: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                createTeam(teamName)
            }.onFailure {

            }.onSuccess {
                onComplete()
            }

        }
    }

    data class UiState(
        val workspaceName: String = ""
    )
}