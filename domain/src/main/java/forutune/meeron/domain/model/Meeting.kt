package forutune.meeron.domain.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Meeting(
    @SerialName("meetingId") val meetingId: Long = -1,
    @SerialName("startDate") val startDate: String = "",
    @SerialName("startTime") val startTime: String = "",
    @SerialName("endTime") val endTime: String = "",
    @SerialName("meetingName") val meetingName: String = "",
    @SerialName("purpose") val purpose: String = "",
    @SerialName("place") val place: String = "",
    val date: Date = Date(),
    val time: String = "",
    val team: Team = Team(),
    val ownerIds: List<Long> = emptyList(),
    val agenda: List<Agenda> = emptyList(),
    val participants: List<WorkspaceUser> = emptyList(),
    val attends: Int = 0,
    val absents: Int = 0,
    val unknowns: Int = 0
) : Serializable

