package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Agenda(
    val agenda: String,
    val issues: List<Issue>,
    val files: List<File>
) : Serializable