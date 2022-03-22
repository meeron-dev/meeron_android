package fourtune.merron.data.model.dto.request

import forutune.meeron.domain.model.Agenda
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkSpaceUserIdsRequest(
    @SerialName("workspaceUserIds") val workspaceUserIds: List<Long>
)

@Serializable
data class AgendaRequest(
    val agendas: List<Agenda>
)

@Serializable
data class MeetingRequest(
    @SerialName("workspaceId") val workspaceId: Long,
    @SerialName("meetingDate") val meetingDate: String,
    @SerialName("startTime") val startTime: String,
    @SerialName("endTime") val endTime: String,
    @SerialName("meetingName") val meetingName: String,
    @SerialName("meetingPurpose") val meetingPurpose: String,
    @SerialName("operationTeamId") val operationTeamId: Long,
    @SerialName("meetingAdminIds") val meetingAdminIds: List<Long>
)

@Serializable
data class WorkSpaceRequest(
    @SerialName("workspaceId") val workspaceId: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("position") val position: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String,
)

@Serializable
data class TeamRequest(
    @SerialName("workspaceId") val workspaceId: Long,
    @SerialName("teamName") val teamName: String,
)