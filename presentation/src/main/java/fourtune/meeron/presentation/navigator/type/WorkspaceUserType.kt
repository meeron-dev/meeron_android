package fourtune.meeron.presentation.navigator.type

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.di.TypeInjector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


class WorkspaceUserType constructor(context: Context) : NavType<WorkspaceUser>(isNullableAllowed = false) {
    @Inject
    lateinit var json: Json

    init {
        TypeInjector(context).inject(this)
    }

    override fun get(bundle: Bundle, key: String): WorkspaceUser? {
        return bundle.getSerializable(Const.WorkspaceUser) as WorkspaceUser?
    }

    override fun parseValue(value: String): WorkspaceUser {
        return json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: WorkspaceUser) {
        bundle.putSerializable(key, value)
    }

}
