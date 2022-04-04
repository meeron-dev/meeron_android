package fourtune.merron.data.model.dto.response.meeting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingsResponse(
    @SerialName("meetings") val meetings: List<MeetingResponse>
) {
    @Serializable
    data class MeetingResponse(
        @SerialName("meetingId") val meetingId: Long = -1,
        @SerialName("meetingName") val meetingName: String = "",
        @SerialName("meetingDate") val meetingDate: String = "",
        @SerialName("startTime") val startTime: String = "",
        @SerialName("endTime") val endTime: String = "",
        @SerialName("operationTeamId") val operationTeamId: Long = -1,
        @SerialName("operationTeamName") val operationTeamName: String = "",
        @SerialName("workspaceId") val workspaceId: Long? = null,
        @SerialName("workspaceName") val workspaceName: String? = null,
        @SerialName("mainAgendaId") val mainAgendaId: Long? = null,
        @SerialName("mainAgenda") val mainAgenda: String? = null,
        @SerialName("attends") val attends: Int = 0,
        @SerialName("absents") val absents: Int = 0,
        @SerialName("unknowns") val unknowns: Int = 0
    )
}


@Serializable
data class MeetingIdResponse(
    @SerialName("meetingId") val meetingId: Long
)