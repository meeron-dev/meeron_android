package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.Teams
import retrofit2.http.GET
import retrofit2.http.Query

interface TeamApi {
    @GET("/api/teams")
    suspend fun getTeams(
        @Query("workspaceId") workspaceId: Long
    ): Teams
}