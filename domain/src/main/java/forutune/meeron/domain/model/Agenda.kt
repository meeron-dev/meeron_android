package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Agenda(
    val order: Int = 0,
    val name: String = "",
    val issues: List<Issue> = emptyList(),
    val fileInfos: List<FileInfo> = emptyList()
) : Serializable