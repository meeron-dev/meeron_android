package fourtune.meeron.presentation.ui.start

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.EntryPointType
import forutune.meeron.domain.usecase.user.CreateUserUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NameInitViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val entryPointType = savedStateHandle.get<EntryPointType>(Const.EntryPointType) ?: EntryPointType.Normal

    fun saveName(userName: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                createUserUseCase(userName)
            }.onFailure {
                Timber.tag("🔥zero:saveName").e("$it")
            }
        }
    }

}