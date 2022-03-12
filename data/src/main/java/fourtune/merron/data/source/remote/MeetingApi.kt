package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.AgendaDto
import fourtune.merron.data.model.dto.MeetingDto
import fourtune.merron.data.model.dto.request.WorkSpaceUserIds
import fourtune.merron.data.model.dto.response.AgendaResponses
import okhttp3.MultipartBody
import retrofit2.http.*


interface MeetingApi {
    @POST("/api/meetings")
    suspend fun createMeeting(@Body meetingDto: MeetingDto): retrofit2.Response<fourtune.merron.data.model.dto.response.MeetingId>

    @POST("/api/meetings/{meetingId}/attendees")
    suspend fun addParticipants(
        @Path("meetingId") meetingId: Long,
        @Body workspaceUserIds: WorkSpaceUserIds
    )

    @POST("/api/meetings/{meetingId}/agendas")
    suspend fun addAgendas(
        @Path("meetingId") meetingId: Long,
        @Body agendas: AgendaDto
    ): AgendaResponses

    @Multipart
    @POST("/api/agendas/{agendaId}/files")
    suspend fun addFile(
        @Path("agendaId") agendaId: Long,
        @Part files: MultipartBody.Part
    )
}