package fourtune.meeron.presentation.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.usecase.GetWorkSpaceTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(teams = getWorkSpaceTeamUseCase())
            }
        }
    }

    data class UiState(
        val teams: List<Team> = emptyList()
    )
}