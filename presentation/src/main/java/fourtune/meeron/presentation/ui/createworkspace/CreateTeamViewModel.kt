package fourtune.meeron.presentation.ui.createworkspace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.usecase.workspace.CreateWorkSpaceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel @Inject constructor(
    private val createWorkSpaceUseCase: CreateWorkSpaceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workSpace: WorkSpace = requireNotNull(savedStateHandle[Const.WorkSpace])

    private val _showLoading = MutableStateFlow(false)
    val showLoading = _showLoading.asStateFlow()

    fun createWorkSpace(teamName: String, onCreate: (workspaceId: Long) -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                _showLoading.emit(true)
                createWorkSpaceUseCase(workSpace, teamName)
            }.onSuccess { workspaceId ->
                _showLoading.emit(false)
                onCreate(workspaceId)
            }.onFailure {
                _showLoading.emit(false)
                Timber.tag("🔥zero:createWorkSpace").e("$it")
            }
        }
    }
}