package fourtune.meeron.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.usecase.user.CreateUserUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NameInitViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    fun saveName(userName: String) {
        viewModelScope.launch {
            createUserUseCase(userName)
        }
    }

}