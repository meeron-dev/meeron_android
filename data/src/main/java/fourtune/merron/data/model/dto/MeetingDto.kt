package fourtune.merron.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("workspaceId") val workspaceId: Long,
    @SerialName("meetingDate") val meetingDate: String,
    @SerialName("startTime") val startTime: String,
    @SerialName("endTime") val endTime: String,
    @SerialName("meetingName") val meetingName: String,
    @SerialName("meetingPurpose") val meetingPurpose: String,
    @SerialName("operationTeamId") val operationTeamId: Long,
    @SerialName("meetingAdminIds") val meetingAdminIds: List<Long>
)