package fourtune.meeron.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.MeeronError
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.GetUserWorkspacesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserWorkspaces: GetUserWorkspacesUseCase,
    private val workSpaceRepository: WorkSpaceRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getMe: GetMeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getUserWorkspaces()
            }.onSuccess { workspaces ->
                if (workspaces.isEmpty()) {
                    _event.emit(Event.GoToCreateOrJoin)
                } else {
                    setCurrentWorkspaceInfo(workspaces)
                    _event.emit(Event.GoToHome)
                }
            }.onFailure {
                if (it is MeeronError) {
                    if (it.code == 1100) {
                        _event.emit(Event.GoToLogin)
                    } else {

                    }
                    _toast.emit(it.errorMessage)
                } else {
                    _toast.emit(it.message ?: "$it")
                }
                Timber.tag("🔥zero:asda").e("$it")
            }
        }
    }

    @Deprecated("멀티 스페이스로 된다면 변경해야 함")
    private suspend fun setCurrentWorkspaceInfo(workspaces: List<WorkSpaceInfo>) {
        kotlin.runCatching {
            val workspaceId = workspaces.first().workSpaceId
            val me = getMe()
            workSpaceRepository.setCurrentWorkspaceId(workspaceId)
            val workspaceUser =
                workspaceUserRepository.getMyWorkspaceUsers(me.userId).first { it.workspaceId == workspaceId }
            workspaceUserRepository.setCurrentWorkspaceUserId(workspaceUser.workspaceUserId)
        }.onFailure {
            Timber.tag("🔥zero:setCurrentWorkspaceInfo").d("$it")
            _toast.emit(it.message ?: "$it")
        }

    }

    data class UiState(
        val isReady: Boolean = false
    )

    sealed interface Event {
        object GoToHome : Event
        object GoToLogin : Event
        object GoToCreateOrJoin : Event
    }
}