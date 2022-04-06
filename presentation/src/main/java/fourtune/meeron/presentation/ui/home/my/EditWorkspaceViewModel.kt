package fourtune.meeron.presentation.ui.home.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import fourtune.meeron.presentation.DynamicLinkProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditWorkspaceViewModel @Inject constructor(
    private val dynamicLinkProvider: DynamicLinkProvider,
    private val getLatestWorkspaceIdUseCase: GetLatestWorkspaceIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dynamicLinkProvider(getLatestWorkspaceIdUseCase()) { link ->
                _uiState.update {
                    it.copy(link = link)
                }
            }
        }
    }

    data class UiState(
        val link: String = ""
    )

}