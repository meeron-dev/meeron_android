package fourtune.meeron.presentation.ui.createworkspace

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.usecase.workspace.IsDuplicateNicknameUseCase
import fourtune.meeron.presentation.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkspaceProfileViewModel @Inject constructor(
    private val isDuplicateNickname: IsDuplicateNicknameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(UiState(workSpace = WorkSpace(workspaceName = savedStateHandle[Const.WorkspaceName] ?: "")))
    val uiState = _uiState.asStateFlow()

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
            val isDuplicateNickname = runCatching { isDuplicateNickname(nickname) }.getOrDefault(true)

            _uiState.update {
                it.copy(
                    workSpace = it.workSpace.copy(
                        nickname = workspaceInfoMap[Info.NickName].orEmpty(),
                    ),
                    isVerify = workspaceInfoMap
                        .filter { it.key.isEssential }
                        .all { it.value.isNotEmpty() }
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
                workSpace = it.workSpace.copy(
                    position = workspaceInfoMap[Info.Position].orEmpty(),
                    email = workspaceInfoMap[Info.Email].orEmpty(),
                    phone = workspaceInfoMap[Info.PhoneNumber].orEmpty(),
                ),
            )
        }
    }

    fun addImage(uri: Uri) {
        _uiState.update {
            it.copy(
                fileName = uri.toString(),
                workSpace = it.workSpace.copy(image = uri.toString())
            )
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