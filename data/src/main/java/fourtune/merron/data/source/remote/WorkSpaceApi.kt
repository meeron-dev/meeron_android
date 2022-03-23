package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.response.MyWorkSpacesResponse
import fourtune.merron.data.model.dto.response.WorkSpaceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WorkSpaceApi {

    @GET("/api/users/{userId}/workspaces")
    suspend fun getUserWorkspaces(@Path("userId") userId: Long): MyWorkSpacesResponse

    @GET("/api/workspaces/{workspaceId}")
    suspend fun getWorkSpace(@Path("workspaceId") workspaceId: Long): WorkSpaceResponse

    @POST("/api/workspaces")
    suspend fun createWorkSpace(@Body name: String): WorkSpaceResponse
}