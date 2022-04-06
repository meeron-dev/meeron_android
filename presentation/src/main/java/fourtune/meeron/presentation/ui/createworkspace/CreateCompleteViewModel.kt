package fourtune.meeron.presentation.ui.createworkspace

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.usecase.IsFirstVisitUserUseCase
import fourtune.meeron.presentation.DynamicLinkProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCompleteViewModel @Inject constructor(
    private val isFirstVisitUser: IsFirstVisitUserUseCase,
    private val dynamicLinkProvider: DynamicLinkProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isFirstVisit = isFirstVisitUser()
            dynamicLinkProvider(requireNotNull(savedStateHandle.get<String>(Const.WorkspaceId)).toLong()) { link ->
                _uiState.update {
                    it.copy(
                        link = link,
                        showOnBoarding = isFirstVisit
                    )
                }
            }
        }
    }

    data class UiState(
        val link: String = "",
        val showOnBoarding: Boolean = false
    )

}