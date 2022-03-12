package fourtune.merron.data.model.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingId(
    @SerialName("meetingId") val meetingId: Long
)

@Serializable
data class AgendaResponses(
    @SerialName("agendaResponses") val agendaResponses: List<AgendaResponse>
)

@Serializable
data class AgendaResponse(
    @SerialName("agendaNumber") val agendaNumber: Long,
    @SerialName("createdAgendaId") val createdAgendaId: Long,
)

