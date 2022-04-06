package fourtune.merron.data.model.dto.response.meeting

import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class TodayMeeting(
    @SerialName("meetings") val meetings: List<MeetingInfo>
) {
    @kotlinx.serialization.Serializable
    data class MeetingInfo(
        @SerialName("meeting") val meeting: MeetingResponse,
        @SerialName("team") val team: Team,
        @SerialName("agendas") val agendas: List<TodayAgendas>,
        @SerialName("admins") val admins: List<WorkspaceUser>,
        @SerialName("attendCount") val attendCount: AttendCounts,
    )

    @kotlinx.serialization.Serializable
    data class MeetingResponse(
        @SerialName("meetingId") val meetingId: Long = -1,
        @SerialName("startDate") val startDate: String = "",
        @SerialName("startTime") val startTime: String = "",
        @SerialName("endTime") val endTime: String = "",
        @SerialName("meetingName") val meetingName: String = "",
        @SerialName("purpose") val purpose: String = "",
        @SerialName("place") val place: String = ""
    )
}

@Serializable
data class TodayAgendas(
    @SerialName("agendaId") val agendaId: Int = 0,
    @SerialName("agendaOrder") val agendaOrder: Int = 0,
    @SerialName("agendaName") val agendaName: String = "",
    @SerialName("agendaResult") val agendaResult: String? = null,

    )

@Serializable
data class AttendCounts(
    @SerialName("attend") val attend: Int = 0,
    @SerialName("absent") val absent: Int = 0,
    @SerialName("unknown") val unknown: Int = 0
)