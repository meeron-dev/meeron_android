package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkspaceUserDetailViewModel @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            workspaceUser = requireNotNull(savedStateHandle.get<WorkspaceUser>(Const.WorkspaceUser)),
            team = requireNotNull(savedStateHandle.get(Const.Team))
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            workspaceUserRepository.getUserProfile(uiState.value.workspaceUser.workspaceUserId) { file ->
                _uiState.update {
                    it.copy(
                        image = file
                    )
                }
            }
        }
    }

    data class UiState(
        val workspaceUser: WorkspaceUser,
        val image: Any? = null,
        val userName: String = "",
        val team: Team = Team()
    )
}