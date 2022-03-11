package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Meeting(
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val purpose: String = "",
    val ownerIds: List<Long> = emptyList(),
    val team: Team = Team(),
    val agenda: List<Agenda> = emptyList(),
    val participants: List<Participant> = emptyList()
) : Serializable