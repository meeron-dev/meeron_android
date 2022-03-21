package fourtune.meeron.presentation.ui.createworkspace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.usecase.workspace.CreateWorkSpaceUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel @Inject constructor(
    private val createWorkSpaceUseCase: CreateWorkSpaceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workSpace: WorkSpace = requireNotNull(savedStateHandle[Const.WorkSpace])

    fun createWorkSpace(teamName: String) {
        viewModelScope.launch {
            createWorkSpaceUseCase(workSpace, teamName)
        }
    }
}