package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Meeting(
    val meetingId: Long = -1,
    val title: String = "",
    val date: Date = Date(),
    val time: String = "",
    val purpose: String = "",
    val team: Team = Team(),
    val ownerIds: List<Long> = emptyList(),
    val mainAgendaId: Long? = null,
    val mainAgenda: String? = null,
    val agenda: List<Agenda> = emptyList(),
    val participants: List<WorkspaceUser> = emptyList(),
    val attends: Int = 0,
    val absents: Int = 0,
    val unknowns: Int = 0
) : Serializable

