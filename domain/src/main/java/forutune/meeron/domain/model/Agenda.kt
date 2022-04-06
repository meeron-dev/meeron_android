package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Agenda(
    val order: Int = 0,
    val name: String = "",
    val issues: List<Issue> = emptyList(),
    val fileInfos: List<FileInfo> = emptyList()
) : Serializable

@kotlinx.serialization.Serializable
data class AgendaInfo(
    val agendas: Int = 0,
    val checks: Int = 0,
    val files: Int = 0
) : Serializable