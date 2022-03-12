package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class FileInfo(
    val uriString: String,
    val fileName: String
) : Serializable