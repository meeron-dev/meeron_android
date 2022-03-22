package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Teams
import fourtune.merron.data.model.dto.request.TeamRequest
import fourtune.merron.data.model.dto.response.TeamIdResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TeamApi {
    @GET("/api/teams")
    suspend fun getTeams(
        @Query("workspaceId") workspaceId: Long
    ): Teams

    @POST("/api/teams")
    suspend fun createTeam(
        @Body teamRequest: TeamRequest
    ): TeamIdResponse
}