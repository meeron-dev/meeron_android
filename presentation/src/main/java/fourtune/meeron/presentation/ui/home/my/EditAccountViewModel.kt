package fourtune.meeron.presentation.ui.home.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.login.LogoutUseCase
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.me.WithdrawalUseCase
import forutune.meeron.domain.usecase.workspace.GetCurrentWorkspaceInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val getCurrentWorkspaceInfo: GetCurrentWorkspaceInfoUseCase,
    private val logout: LogoutUseCase,
    private val withdrawal: WithdrawalUseCase,
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    workspaceInfo = getCurrentWorkspaceInfo(),
                    me = getMyWorkSpaceUser()
                )
            }
        }
    }

    fun logout(goToLogin: () -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                logout.invoke { UserApiClient.rx.unlink().await() }
            }.onSuccess {
                goToLogin()
            }.onFailure { Timber.tag("🔥zero:logout").e("$it") }
        }
    }

    fun withdrawal(goToLogin: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                withdrawal.invoke {
                    UserApiClient.rx.unlink().await()
                }
            }
                .onSuccess {
                    goToLogin()
                }.onFailure {
                    Timber.tag("🔥zero:withdrawal").e("$it")
                }
        }
    }

    data class UiState(
        val workspaceInfo: WorkSpaceInfo = WorkSpaceInfo(),
        val me: WorkspaceUser = WorkspaceUser()
    )
}