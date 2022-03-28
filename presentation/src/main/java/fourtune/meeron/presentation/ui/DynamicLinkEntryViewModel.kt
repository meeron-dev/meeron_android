package fourtune.meeron.presentation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.usecase.SettingAccountUseCase
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.CreateWorkspaceUserUseCase
import forutune.meeron.domain.usecase.workspace.GetUserWorkspacesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DynamicLinkEntryViewModel @Inject constructor(
    getMe: GetMeUseCase,
    getUserWorkspaces: GetUserWorkspacesUseCase,
    private val createWorkspaceUser: CreateWorkspaceUserUseCase,
    private val settingAccountUseCase: SettingAccountUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workspaceId = savedStateHandle.get<String?>("id").orEmpty()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        /**
         * 1. 카카오 로그인 x
         * 2. 유저 이름 x
         * 3. 워크스페이스 x
         * 4. 위 조건 모두 o or 사라진 워크스페이스
         */
        viewModelScope.launch {
            runCatching {
                settingAccountUseCase.invoke(workspaceId.toLong())
                getMe()
            }.onFailure {
                _uiState.update { UiState.NotFound }
            }.onSuccess {
                if (it.name.isNullOrEmpty()) {
                    _uiState.update { UiState.NotFound }
                } else {
                    runCatching {
                        val workspaces = getUserWorkspaces()
                        val isJoin = workspaces.map { it.workSpaceId }.contains(workspaceId.toLong())
                        if (isJoin) {
                            _uiState.update { UiState.AlreadyJoinOrDeleted }
                        } else {
                            _uiState.update { UiState.GoToCreateProfile }
                        }
                    }.onFailure {
                        _uiState.update { UiState.AlreadyJoinOrDeleted }
                    }
                }

            }
        }
    }

    fun createWorkspaceUser(workSpace: WorkSpace) {
        viewModelScope.launch {
            kotlin.runCatching {
                createWorkspaceUser.invoke(workSpace.copy(workspaceId.toLong()))
            }
                .onFailure { Timber.tag("🔥zero:create").e("$it") }
        }
    }


    sealed interface UiState {
        object Loading : UiState
        object NotFound : UiState
        object AlreadyJoinOrDeleted : UiState
        object GoToCreateProfile : UiState
    }

    sealed interface Event {
        object GoToTOS : Event
    }
}