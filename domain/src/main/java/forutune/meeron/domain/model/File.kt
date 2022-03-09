package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class File(
    val path: String
) : Serializable