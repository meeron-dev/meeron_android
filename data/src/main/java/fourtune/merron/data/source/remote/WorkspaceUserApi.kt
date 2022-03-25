package fourtune.merron.data.source.remote

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.model.WorkspaceUsers
import okhttp3.MultipartBody
import retrofit2.http.*

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

    @Multipart
    @POST("/api/workspace-users/admin")
    suspend fun createWorkSpaceAdmin(
        @Part files: MultipartBody.Part? = null,
        @Part request: MultipartBody.Part
    )

    @Multipart
    @POST("/api/workspace-users")
    suspend fun createWorkSpaceUser(
        @Part files: MultipartBody.Part? = null,
        @Part request: MultipartBody.Part
    )

}