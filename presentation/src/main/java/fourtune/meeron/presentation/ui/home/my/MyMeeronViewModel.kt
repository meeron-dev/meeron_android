package fourtune.meeron.presentation.ui.home.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyMeeronViewModel @Inject constructor(
    private val getMeUseCase: GetMeUseCase,
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase,
    private val workspaceUserRepository: WorkspaceUserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val myWorkspaceUser = getMyWorkSpaceUser()
            val myName = getMeUseCase().name.orEmpty()
            workspaceUserRepository.getUserProfile(myWorkspaceUser.workspaceUserId) { file ->
                _uiState.update {
                    it.copy(
                        myName = myName,
                        profileImage = file
                    )
                }
            }

        }
    }

    data class UiState(
        val myName: String = "",
        val profileImage: File? = null
    )
}