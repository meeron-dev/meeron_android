package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.model.WorkspaceUsers
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkspaceUserApi {

    @GET("/api/users/{userId}/workspace-users")
    suspend fun getWorkspaceUsers(@Path("userId") userId: Long): List<WorkspaceUser>

    @GET("/api/workspace-users/{workspaceUserId}")
    suspend fun getWorkspaceUser(@Path("workspaceUserId") workspaceUserId: Long): WorkspaceUser

    @GET("/api/workspace-users")
    suspend fun getUsers(
        @Query("workspaceId") workspaceId: Long,
        @Query("nickname") nickname: String
    ): WorkspaceUsers

    @GET("/api/teams/{teamId}/workspace-users")
    suspend fun getTeamUser(@Path("teamId") teamId: Long): WorkspaceUsers

}