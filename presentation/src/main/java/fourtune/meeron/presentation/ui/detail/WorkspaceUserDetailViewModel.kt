package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WorkspaceUserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            workspaceUser = requireNotNull(savedStateHandle.get<WorkspaceUser>(Const.WorkspaceUser)),
            team = requireNotNull(savedStateHandle.get(Const.Team))
        )
    )
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val workspaceUser: WorkspaceUser,
        val userName: String = "",
        val team: Team = Team()
    )
}