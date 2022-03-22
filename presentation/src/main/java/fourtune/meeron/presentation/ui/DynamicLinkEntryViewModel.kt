package fourtune.meeron.presentation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DynamicLinkEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val workspaceId = savedStateHandle.get<String?>("id").orEmpty()

    init {
        Timber.tag("🔥zero:$").d("$workspaceId")
    }
}