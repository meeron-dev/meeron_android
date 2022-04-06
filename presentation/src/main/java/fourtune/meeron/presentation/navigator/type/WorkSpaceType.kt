package fourtune.meeron.presentation.navigator.type

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkSpace
import fourtune.meeron.presentation.di.TypeInjector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WorkSpaceType constructor(context: Context) : NavType<WorkSpace>(isNullableAllowed = false) {
    @Inject
    lateinit var json: Json

    init {
        TypeInjector.invoke(context).inject(this)
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
