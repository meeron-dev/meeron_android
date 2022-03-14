package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.MeetingDto
import fourtune.merron.data.model.dto.request.AgendaRequest
import fourtune.merron.data.model.dto.request.WorkSpaceUserIdsRequest
import fourtune.merron.data.model.dto.response.AgendaResponses
import fourtune.merron.data.model.dto.response.MeetingsResponse
import okhttp3.MultipartBody
import retrofit2.http.*


interface MeetingApi {
    @POST("/api/meetings")
    suspend fun createMeeting(@Body meetingDto: MeetingDto): retrofit2.Response<fourtune.merron.data.model.dto.response.MeetingIdResponse>

    @POST("/api/meetings/{meetingId}/attendees")
    suspend fun addParticipants(
        @Path("meetingId") meetingId: Long,
        @Body workspaceUserIds: WorkSpaceUserIdsRequest
    )

    @POST("/api/meetings/{meetingId}/agendas")
    suspend fun addAgendas(
        @Path("meetingId") meetingId: Long,
        @Body agendas: AgendaRequest
    ): AgendaResponses

    @Multipart
    @POST("/api/agendas/{agendaId}/files")
    suspend fun addFile(
        @Path("agendaId") agendaId: Long,
        @Part files: MultipartBody.Part
    )

    @GET("/api/meetings/today")
    suspend fun getMeeting(
        @Query("workspaceId") workspaceId: Long,
        @Query("workspaceUserId") workspaceUserId: Long,
    ): MeetingsResponse
}