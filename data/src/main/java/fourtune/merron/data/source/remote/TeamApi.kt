package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Teams
import fourtune.merron.data.model.dto.request.AddTeamMemberRequest
import fourtune.merron.data.model.dto.request.TeamRequest
import fourtune.merron.data.model.dto.response.TeamIdResponse
import fourtune.merron.data.model.dto.response.WorkSpaceUsersResponse
import retrofit2.Response
import retrofit2.http.*

interface TeamApi {
    @GET("/api/teams")
    suspend fun getTeams(
        @Query("workspaceId") workspaceId: Long
    ): Teams

    @POST("/api/teams")
    suspend fun createTeam(
        @Body teamRequest: TeamRequest
    ): TeamIdResponse

    @GET("api/teams/{teamId}/workspace-users")
    suspend fun getTeamMembers(
        @Path("teamId") teamId: Long
    ): WorkSpaceUsersResponse

    @PATCH("/api/workspace-users/{workspaceUserId}/team")
    suspend fun kickTeamMember(
        @Path("workspaceUserId") workspaceUserId: Long,
        @Body adminWorkspaceUserId: Long
    ): Response<Unit>

    @POST("/api/teams/{teamId}")
    suspend fun deleteTeam(@Path("teamId") teamId: Long, @Body workspaceUserId: Long): Response<Unit>

    @PATCH("/api/teams/{teamId}/workspace-users")
    suspend fun addTeamMember(
        @Path("teamId") teamId: Long,
        @Body addTeamMemberRequest: AddTeamMemberRequest
    ): Response<Unit>
}