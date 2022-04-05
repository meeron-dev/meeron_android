package fourtune.merron.data.model.dto.response

import forutune.meeron.domain.model.TeamState
import forutune.meeron.domain.model.WorkspaceUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAgendaIdsResponse(
    @SerialName("createdAgendaIds") val createdAgendaIds: List<Long>
)

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
data class MyWorkspaceUserResponse(
    @SerialName("myWorkspaceUsers") val myWorkspaceUsers: List<WorkspaceUser>
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

@Serializable
data class DuplicateResponse(
    @SerialName("duplicate") val duplicate: Boolean
)

@Serializable
data class ParticipantsResponse(
    @SerialName("attendees") val teamStates: List<TeamState>
)

@Serializable
data class TeamMemberInMeetingResponse(
    @SerialName("attends") val attends: List<WorkspaceUser>,
    @SerialName("absents") val absents: List<WorkspaceUser>,
    @SerialName("unknowns") val unknowns: List<WorkspaceUser>
)
