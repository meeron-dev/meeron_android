package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Teams
import fourtune.merron.data.model.dto.request.TeamRequest
import fourtune.merron.data.model.dto.response.TeamIdResponse
import fourtune.merron.data.model.dto.response.WorkSpaceUsersResponse
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
}