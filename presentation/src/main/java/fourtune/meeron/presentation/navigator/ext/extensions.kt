package fourtune.meeron.presentation.navigator.ext

import android.net.Uri
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.encodeJson(): String {
    val encodeToString = Json.encodeToString(this)
    return Uri.encode(encodeToString)
}