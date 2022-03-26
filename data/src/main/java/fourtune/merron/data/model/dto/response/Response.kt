package fourtune.merron.data.model.dto.response

import forutune.meeron.domain.model.WorkspaceUser
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
        @SerialName("meetingStatus") val meetingStatus: String = "",
        @SerialName("workspaceId") val workspaceId: Long? = null,
        @SerialName("workspaceName") val workspaceName: String? = null,
    )
}

@Serializable
data class MeetingIdResponse(
    @SerialName("meetingId") val meetingId: Long
)

@Serializable
data class AgendaResponses(
    @SerialName("agendaResponses") val agendaResponses: List<AgendaResponse>
) {
    @Serializable
    data class AgendaResponse(
        @SerialName("agendaNumber") val agendaNumber: Long,
        @SerialName("createdAgendaId") val createdAgendaId: Long,
    )
}

@Serializable
data class YearCountsResponse(
    @SerialName("yearCounts") val yearCountResponses: List<YearCountResponse>
) {
    @Serializable
    data class YearCountResponse(
        @SerialName("year") val year: Int,
        @SerialName("count") val count: Int
    )
}

@Serializable
data class MonthCountsResponse(
    @SerialName("monthCounts") val monthCountResponses: List<MonthCountResponse>
) {
    @Serializable
    data class MonthCountResponse(
        @SerialName("month") val month: Int,
        @SerialName("count") val count: Int
    )
}

@Serializable
data class DaysResponse(
    @SerialName("days") val days: List<Int>
)

@Serializable
data class MyWorkSpacesResponse(
    @SerialName("myWorkspaces") val myWorkspaces: List<WorkSpaceResponse>
)

@Serializable
data class WorkSpaceResponse(
    @SerialName("workspaceId") val workspaceId: Long,
    @SerialName("workspaceName") val workspaceName: String,
    @SerialName("workspaceLogoUrl") val workspaceLogoUrl: String,
)

@Serializable
data class TeamIdResponse(
    @SerialName("createdTeamId") val createdTeamId: Long
)

@Serializable
data class WorkSpaceUsersResponse(
    @SerialName("workspaceUsers") val workspaceUsers: List<WorkspaceUser>
)
