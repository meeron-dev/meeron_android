package fourtune.meeron.presentation.ui.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.TeamRepository
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.workspace.GetNotJoinedTeamWorkspaceUserUseCase
import forutune.meeron.domain.usecase.workspace.GetWorkSpaceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamMemberPickerViewModel @Inject constructor(
    private val getNotJoinedTeamWorkspaceUser: GetNotJoinedTeamWorkspaceUserUseCase,
    private val teamRepository: TeamRepository,
    private val getMyWorkSpaceUserUseCase: GetMyWorkSpaceUserUseCase,
    private val getWorkSpaceUseCase: GetWorkSpaceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            UiState(
                type = requireNotNull(savedStateHandle.get<Type>(Const.TeamMemberPickerType)),
                teamId = requireNotNull(savedStateHandle.get(Const.TeamId)),
            )
        )

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    notJoinedTeamWorkspaceUser = getNotJoinedTeamWorkspaceUser(),
                    workspaceName = getWorkSpaceUseCase(getMyWorkSpaceUserUseCase().workspaceId).workSpaceName
                )
            }
        }
    }

    fun addTeamMember(selectedTeamMember: List<WorkspaceUser>, onComplete: () -> Unit) {
        if (selectedTeamMember.isNotEmpty()) {
            viewModelScope.launch {
                runCatching {
                    teamRepository.addTeamMember(
                        uiState.value.teamId,
                        getMyWorkSpaceUserUseCase().workspaceUserId,
                        selectedTeamMember.map { it.workspaceUserId }
                    )
                }.onSuccess {
                    onComplete()
                }
            }
        } else {
            onComplete()
        }
    }

    fun deleteTeamIfAdded() {
        if (uiState.value.type == Type.Add) {
            viewModelScope.launch {
                teamRepository.deleteTeam(uiState.value.teamId, getMyWorkSpaceUserUseCase().workspaceUserId)
            }
        }
    }

    data class UiState(
        val type: Type,
        val notJoinedTeamWorkspaceUser: List<WorkspaceUser> = emptyList(),
        val teamId: Long = 0L,
        val workspaceName: String = ""
    )

    enum class Type(val title: String) {
        Administer("팀원 추가하기"), Add("팀 생성하기")
    }

}
