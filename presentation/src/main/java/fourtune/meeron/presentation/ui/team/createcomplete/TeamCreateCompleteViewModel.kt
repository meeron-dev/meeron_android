package fourtune.meeron.presentation.ui.team.createcomplete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.usecase.team.GetTeamMemberUseCase
import forutune.meeron.domain.usecase.team.GetTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamCreateCompleteViewModel @Inject constructor(
    private val getTeamMember: GetTeamMemberUseCase,
    private val getTeam: GetTeamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val teamId = requireNotNull(savedStateHandle.get<Long>(Const.TeamId))
            val teamMember = getTeamMember(teamId)
            val team = getTeam(teamId = teamId)
            _uiState.update {
                it.copy(
                    teamName = team.name,
                    teamMemberCount = teamMember.size
                )
            }
        }
    }

    data class UiState(
        val teamName: String = "",
        val teamMemberCount: Int = 0
    )
}