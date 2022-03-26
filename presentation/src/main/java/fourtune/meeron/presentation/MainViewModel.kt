package fourtune.meeron.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.usecase.SettingAccountUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingAccount: SettingAccountUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            runCatching {
                settingAccount()
            }.onSuccess {
                _event.emit(Event.GoToHome)
            }.onFailure {
                Timber.tag("🔥zero:").e("$it")
                _event.emit(Event.GoToLogin)
            }
        }
    }

    data class UiState(
        val isReady: Boolean = false
    )

    sealed interface Event {
        object GoToHome : Event
        object GoToLogin : Event
    }
}