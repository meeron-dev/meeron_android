package fourtune.meeron.presentation.ui.createworkspace

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.usecase.IsFirstVisitUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCompleteViewModel @Inject constructor(
    private val isFirstVisitUser: IsFirstVisitUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isFirstVisit = isFirstVisitUser()
            Firebase.dynamicLinks.createDynamicLink()
                .setLink(Uri.parse("${Const.DynamicLinkDomainUrl}?id=${savedStateHandle.get<String>(Const.WorkspaceId)}"))
                .setDomainUriPrefix(Const.DynamicLinkDomainUrl)
                .setIosParameters(DynamicLink.IosParameters.Builder(Const.IosBundleId).build())
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnSuccessListener { link ->
                    _uiState.update {
                        it.copy(
                            link = link.shortLink.toString(),
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