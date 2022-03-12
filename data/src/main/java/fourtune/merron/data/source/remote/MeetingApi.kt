package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.AgendaDto
import fourtune.merron.data.model.dto.MeetingDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface MeetingApi {
    @POST("/api/meetings")
    suspend fun createMeeting(@Body meetingDto: MeetingDto): Response<Long>

    @POST("/api/meetings/{meetingId}}/attendees")
    suspend fun addParticipants(
        @Path("meetingId") meetingId: Long,
        @Body workspaceUserIds: List<Long>
    )

    @POST("/api/meetings/{meetingId}/agendas")
    suspend fun addAgendas(
        @Path("meetingId") meetingId: Long,
        @Body agendas: AgendaDto
    )

    @Multipart
    @POST("/api/agendas/{meetingId}/files")
    suspend fun addFile(
        @Path("meetingId") meetingId: Long,
        @Part files: MultipartBody.Part
    )
}