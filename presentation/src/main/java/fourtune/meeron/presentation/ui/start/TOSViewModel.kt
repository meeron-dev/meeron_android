package fourtune.meeron.presentation.ui.start

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.EntryPointType
import javax.inject.Inject

@HiltViewModel
class TOSViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val entryPointType = savedStateHandle.get<EntryPointType>(Const.EntryPointType) ?: EntryPointType.Normal
}