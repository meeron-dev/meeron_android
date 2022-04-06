package fourtune.meeron.presentation.navigator.type

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavType
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Date
import fourtune.meeron.presentation.di.TypeInjector
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


class DateType constructor(context: Context) : NavType<Date>(isNullableAllowed = false) {
    @Inject
    lateinit var json: Json

    init {
        TypeInjector.invoke(context).inject(this)
    }

    override fun get(bundle: Bundle, key: String): Date? {
        return bundle.getSerializable(Const.Date) as Date?
    }

    override fun parseValue(value: String): Date {
        return json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Date) {
        bundle.putSerializable(key, value)
    }

}