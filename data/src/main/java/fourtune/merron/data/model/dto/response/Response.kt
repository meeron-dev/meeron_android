package fourtune.merron.data.model.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingsResponse(
    @SerialName("meetings") val meetings: List<MeetingResponse>
)

@Serializable
data class MeetingResponse(
    @SerialName("meetingId") val meetingId: Long,
    @SerialName("meetingName") val meetingName: String,
    @SerialName("meetingDate") val meetingDate: String,
    @SerialName("startTime") val startTime: String,
    @SerialName("endTime") val endTime: String,
    @SerialName("operationTeamId") val operationTeamId: Long,
    @SerialName("operationTeamName") val operationTeamName: String,
    @SerialName("meetingStatus") val meetingStatus: String,
)

@Serializable
data class MeetingIdResponse(
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
