package fourtune.meeron.presentation.ui.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.workspace.GetNotJoinedTeamWorkspaceUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamMemberPickerViewModel @Inject constructor(
    private val getNotJoinedTeamWorkspaceUser: GetNotJoinedTeamWorkspaceUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(UiState(type = requireNotNull(savedStateHandle.get<Type>(Const.TeamMemberPickerType))))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(notJoinedTeamWorkspaceUser = getNotJoinedTeamWorkspaceUser())
            }
        }
    }

    data class UiState(
        val type: Type,
        val notJoinedTeamWorkspaceUser: List<WorkspaceUser> = emptyList()
    )

    enum class Type(val title: String) {
        Administer("팀원 추가하기"), Add("팀 생성하기")
    }

}
