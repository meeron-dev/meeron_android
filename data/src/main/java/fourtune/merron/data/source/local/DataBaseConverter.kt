package fourtune.merron.data.source.local

import androidx.room.TypeConverter
import forutune.meeron.domain.model.Token
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class DataBaseConverter {
    @TypeConverter
    fun tokenToJson(value: Token): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun stringToToken(value: String): Token {
        return Json.decodeFromString(value)
    }

}