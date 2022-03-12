package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Agenda(
    val order: Long = 0,
    val name: String,
    val issues: List<Issue>,
    val fileInfos: List<FileInfo>
) : Serializable