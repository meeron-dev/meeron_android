package fourtune.merron.data.source.remote

import fourtune.merron.data.model.dto.MeetingDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface MeetingApi {
    @POST("/api/meetings")
    suspend fun createMeeting(@Body meetingDto: MeetingDto): Response<Long>
}