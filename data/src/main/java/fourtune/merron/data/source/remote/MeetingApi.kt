package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.request.AgendaRequest
import fourtune.merron.data.model.dto.request.ChangeMeetingStateRequest
import fourtune.merron.data.model.dto.request.MeetingRequest
import fourtune.merron.data.model.dto.request.WorkSpaceUserIdsRequest
import fourtune.merron.data.model.dto.response.*
import fourtune.merron.data.model.dto.response.meeting.AgendaResponse
import fourtune.merron.data.model.dto.response.meeting.MeetingIdResponse
import fourtune.merron.data.model.dto.response.meeting.MeetingsResponse
import fourtune.merron.data.model.dto.response.meeting.TodayMeeting
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface MeetingApi {
    @POST("/api/meetings")
    suspend fun createMeeting(@Body meetingRequest: MeetingRequest): Response<MeetingIdResponse>

    @POST("/api/meetings/{meetingId}/attendees")
    suspend fun addParticipants(
        @Path("meetingId") meetingId: Long,
        @Body workspaceUserIds: WorkSpaceUserIdsRequest
    )

    @POST("/api/meetings/{meetingId}/agendas")
    suspend fun addAgendas(
        @Path("meetingId") meetingId: Long,
        @Body agendas: AgendaRequest
    ): CreateAgendaIdsResponse

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
    ): TodayMeeting

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

    @GET("/api/meetings/days")
    suspend fun getDateMeetingCounts(
        @Query("type") type: String,
        @Query("id") id: Long,
        @Query("date") date: String
    ): DaysResponse

    @GET("/api/meetings/day")
    suspend fun getDateMeeting(
        @Query("type") type: String,
        @Query("id") id: Long,
        @Query("date") date: String
    ): MeetingsResponse

    @GET("/api/meetings/{meetingId}/attendees/teams")
    suspend fun getTeamState(@Path("meetingId") meetingId: Long): ParticipantsResponse

    @GET("/api/meetings/{meetingId}/agendas/{agendaOrder}")
    suspend fun getAgenda(@Path("meetingId") meetingId: Long, @Path("agendaOrder") agendaOrder: Int): AgendaResponse

    @PATCH("/api/attendees/{workspaceUserId}")
    suspend fun changeMeetingState(
        @Path("workspaceUserId") workspaceUserId: Long,
        @Body request: ChangeMeetingStateRequest
    ): Response<Unit>

    @GET("/api/meetings/{meetingId}/attendees/teams/{teamId}")
    suspend fun getTeamMember(
        @Path("meetingId") meetingId: Long,
        @Path("teamId") teamId: Long
    ): TeamMemberInMeetingResponse
}