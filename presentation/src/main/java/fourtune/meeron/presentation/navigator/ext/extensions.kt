package fourtune.meeron.presentation.navigator.ext

import android.net.Uri
import forutune.meeron.domain.model.Meeting
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Meeting.encodeJson(): String {
    val encodeToString = Json.encodeToString(this)
    return Uri.encode(encodeToString)
}