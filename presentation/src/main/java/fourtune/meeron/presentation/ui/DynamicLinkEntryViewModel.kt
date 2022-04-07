package fourtune.meeron.presentation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.MeeronError
import forutune.meeron.domain.repository.AccountRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.GetUserWorkspacesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DynamicLinkEntryViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    getMe: GetMeUseCase,
    getUserWorkspaces: GetUserWorkspacesUseCase,
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
                accountRepository.setDynamicLink(workspaceId.toLong())
                getMe()
            }.onFailure {
                if (it is MeeronError) {
                    // 로그인 동선
                    _uiState.update { UiState.NotFound }
                } else {
                    //워크스페이스 가입 안된 동선
                    _event.emit(Event.GoToCreateProfile)
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
                            _event.emit(Event.GoToCreateProfile)
                        }
                    }.onFailure {
                        Timber.tag("🔥zero:동선 확인 필요(DM)").e("$it")
                        _uiState.update { UiState.AlreadyJoinOrDeleted }
                    }
                }

            }
        }
    }


    sealed interface UiState {
        object Loading : UiState
        object NotFound : UiState
        object AlreadyJoinOrDeleted : UiState
    }

    sealed interface Event {
        object GoToTOS : Event
        object GoToCreateProfile : Event
    }
}