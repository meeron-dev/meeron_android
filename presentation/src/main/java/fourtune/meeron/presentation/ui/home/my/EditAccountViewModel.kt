package fourtune.meeron.presentation.ui.home.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.usecase.login.LogoutUseCase
import forutune.meeron.domain.usecase.workspace.GetCurrentWorkspaceInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val getCurrentWorkspaceInfo: GetCurrentWorkspaceInfoUseCase,
    private val logout: LogoutUseCase
) : ViewModel() {
    private val _workspaceInfo = MutableStateFlow(WorkSpaceInfo())
    val workspaceInfo = _workspaceInfo.asStateFlow()

    init {
        viewModelScope.launch {
            _workspaceInfo.update { getCurrentWorkspaceInfo() }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logout.invoke { UserApiClient.rx.unlink().await() }
        }
    }

    fun withdrawal() {

    }

}