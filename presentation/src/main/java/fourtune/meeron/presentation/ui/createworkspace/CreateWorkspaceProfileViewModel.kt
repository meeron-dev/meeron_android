package fourtune.meeron.presentation.ui.createworkspace

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkSpace
import fourtune.meeron.presentation.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateWorkspaceProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(UiState(workSpace = WorkSpace(workspaceName = requireNotNull(savedStateHandle[Const.WorkspaceName]))))
    val uiState = _uiState.asStateFlow()

    val workspaceInfoMap = Info.values()
        .associate { info ->
            info to ""
        }.toList()
        .toMutableStateMap()

    fun changeText(info: Info, it: String) {
        workspaceInfoMap[info] = it
        _uiState.update {
            it.copy(
                isVerify = workspaceInfoMap
                    .filter { it.key.isEssential }
                    .all { it.value.isNotEmpty() },
                workSpace = it.workSpace.copy(
                    nickname = workspaceInfoMap[Info.NickName].orEmpty(),
                    position = workspaceInfoMap[Info.Position].orEmpty(),
                    email = workspaceInfoMap[Info.Email].orEmpty(),
                    phone = workspaceInfoMap[Info.PhoneNumber].orEmpty()
                )
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
        val fileName: String = ""
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