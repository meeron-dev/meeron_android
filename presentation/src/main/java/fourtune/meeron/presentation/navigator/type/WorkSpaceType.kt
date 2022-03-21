package fourtune.meeron.presentation.navigator.type

import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkSpace
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class WorkSpaceType : NavType<WorkSpace>(isNullableAllowed = false) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun get(bundle: Bundle, key: String): WorkSpace? {
        return bundle.getSerializable(Const.WorkSpace) as WorkSpace?
    }

    override fun parseValue(value: String): WorkSpace {
        return json.decodeFromString(value)
    }


    override fun put(bundle: Bundle, key: String, value: WorkSpace) {
        bundle.putSerializable(key, value)
    }

}
