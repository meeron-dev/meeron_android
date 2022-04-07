package fourtune.meeron.presentation.ui.createworkspace

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.EntryPointType
import forutune.meeron.domain.model.MeeronError
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.repository.AccountRepository
import forutune.meeron.domain.usecase.workspace.CreateWorkspaceUserUseCase
import forutune.meeron.domain.usecase.workspace.IsDuplicateNicknameUseCase
import fourtune.meeron.presentation.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateWorkspaceProfileViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val isDuplicateNickname: IsDuplicateNicknameUseCase,
    private val createWorkspaceUser: CreateWorkspaceUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val entryPointType = savedStateHandle.get<EntryPointType>(Const.EntryPointType) ?: EntryPointType.Normal

    private val _uiState =
        MutableStateFlow(UiState(workSpace = WorkSpace(workspaceName = savedStateHandle[Const.WorkspaceName] ?: "")))
    val uiState = _uiState.asStateFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    private var searchJob: Job? = null

    val workspaceInfoMap = Info.values()
        .associate { info ->
            info to ""
        }.toList()
        .toMutableStateMap()

    fun changeName(info: Info, nickname: String) {
        workspaceInfoMap[info] = nickname
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            val isDuplicateNickname = runCatching { isDuplicateNickname(nickname) }.getOrElse {
                if (it is NoSuchElementException) {
                    return@getOrElse false
                } else {
                    true
                }
            }

            _uiState.update {
                it.copy(
                    workSpace = it.workSpace.copy(
                        nickname = workspaceInfoMap[Info.NickName].orEmpty(),
                    ),
                    isVerify = isInsertedEssential()
                            && !isDuplicateNickname,
                    isDuplicateNickname = isDuplicateNickname
                )
            }
        }
    }

    fun changeText(info: Info, it: String) {
        workspaceInfoMap[info] = it
        _uiState.update {
            it.copy(
                isVerify = isInsertedEssential() && !it.isDuplicateNickname,
                workSpace = it.workSpace.copy(
                    position = workspaceInfoMap[Info.Position].orEmpty(),
                    email = workspaceInfoMap[Info.Email].orEmpty(),
                    phone = workspaceInfoMap[Info.PhoneNumber].orEmpty(),
                ),
            )
        }
    }

    private fun isInsertedEssential() = workspaceInfoMap
        .filter { it.key.isEssential }
        .all { it.value.isNotEmpty() }

    fun addImage(uri: Uri) {
        _uiState.update {
            it.copy(
                fileName = uri.toString(),
                workSpace = it.workSpace.copy(image = uri.toString())
            )
        }
    }

    fun createWorkspaceUser(goToHome: () -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                val workspaceId = accountRepository.getDynamicLink()
                createWorkspaceUser.invoke(uiState.value.workSpace.copy(workspaceId))
            }
                .onFailure {
                    Timber.tag("🔥createWUser(DL)").e("$it")
                    if (it is MeeronError) {
                        _toast.emit(it.errorMessage)
                    } else {
                        _toast.emit("${it.message} ?: $it")
                    }
                }
                .onSuccess { goToHome() }
        }
    }

    data class UiState(
        val isVerify: Boolean = false,
        val workSpace: WorkSpace = WorkSpace(),
        val fileName: String = "",
        val isDuplicateNickname: Boolean = false
    )

    enum class Info(
        @StringRes val title: Int,
        val isEssential: Boolean = false,
        val limit: Int = 0,
    ) {
        NickName(R.string.nickname_title, true, 5),
        Position(R.string.position, true, 5),
        Email(R.string.email, isEssential = false, limit = 0),
        PhoneNumber(R.string.phone_number, isEssential = false, limit = 0)
    }
}