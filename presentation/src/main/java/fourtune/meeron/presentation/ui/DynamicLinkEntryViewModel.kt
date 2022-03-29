package fourtune.meeron.presentation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.MeeronError
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
    val workspaceId = savedStateHandle.get<String?>("id").orEmpty()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    init {
        /**
         * 1. 카카오 로그인 x
         * 2. 유저 이름 x
         * 3. 워크스페이스 x
         * 4. 위 조건 모두 o or 사라진 워크스페이스
         */
        viewModelScope.launch {
            runCatching {
                getMe()
            }.onFailure {
                if (it is MeeronError) {
                    // 로그인 동선
                    _uiState.update { UiState.NotFound }
                } else {
                    //워크스페이스 가입 안된 동선
                    _uiState.update { UiState.GoToCreateProfile }
                    Timber.tag("🔥zero:DynamicLink").d("$it")
                }

            }.onSuccess { me ->
                if (me.name.isNullOrEmpty()) {
                    _uiState.update { UiState.NotFound }
                } else {
                    runCatching {
                        val workspaces = getUserWorkspaces()
                        val alreadyJoin = workspaces.map { it.workSpaceId }.contains(workspaceId.toLong())
                        if (alreadyJoin) {
                            _uiState.update { UiState.AlreadyJoinOrDeleted }
                        } else {
                            _uiState.update { UiState.GoToCreateProfile }
                        }
                    }.onFailure {
                        Timber.tag("🔥zero:동선 확인 필요(DM)").e("$it")
                        _uiState.update { UiState.AlreadyJoinOrDeleted }
                    }
                }

            }
        }
    }

    fun createWorkspaceUser(workSpace: WorkSpace, onCreate: () -> Unit = {}) {
        viewModelScope.launch {
            kotlin.runCatching {
                createWorkspaceUser.invoke(workSpace.copy(workspaceId.toLong()))
                settingAccountUseCase.invoke(workspaceId.toLong())
            }
                .onFailure {
                    Timber.tag("🔥createWUser(DL)").e("$it")
                    if (it is MeeronError) {
                        _toast.emit(it.errorMessage)
                    } else {
                        _toast.emit("${it.message} ?: $it")
                    }
                }
                .onSuccess { onCreate() }
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