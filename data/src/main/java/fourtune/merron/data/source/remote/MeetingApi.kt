package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.request.AgendaRequest
import fourtune.merron.data.model.dto.request.MeetingRequest
import fourtune.merron.data.model.dto.request.WorkSpaceUserIdsRequest
import fourtune.merron.data.model.dto.response.AgendaResponses
import fourtune.merron.data.model.dto.response.MeetingsResponse
import fourtune.merron.data.model.dto.response.MonthCountsResponse
import fourtune.merron.data.model.dto.response.YearCountsResponse
import okhttp3.MultipartBody
import retrofit2.http.*


interface MeetingApi {
    @POST("/api/meetings")
    suspend fun createMeeting(@Body meetingRequest: MeetingRequest): retrofit2.Response<fourtune.merron.data.model.dto.response.MeetingIdResponse>

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
    suspend fun getTodayMeeting(
        @Query("workspaceId") workspaceId: Long,
        @Query("workspaceUserId") workspaceUserId: Long,
    ): MeetingsResponse

    @GET("/api/meetings/years")
    suspend fun getYearMeetingCount(
        @Query("type") type: String,
        @Query("id") id: Long
    ): YearCountsResponse

    @GET("/api/meetings/months")
    suspend fun getMonthMeetingCount(
        @Query("type") type: String,
        @Query("id") id: Long,
        @Query("year") year: Int
    ): MonthCountsResponse
}