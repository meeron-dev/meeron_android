package forutune.meeron.domain.usecase.meeting

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class CreateMeetingUseCase @Inject constructor(
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase,
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase,
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meeting: Meeting) {
        val workspaceId = getLatestWorkspaceId()
        val meetingId = meetingRepository.createMeeting(
            workspaceId,
            meeting.copy(ownerIds = meeting.ownerIds - getMyWorkSpaceUser().workspaceUserId)
        )
        meetingRepository.addParticipants(meetingId, meeting)
        val agendaIds = meetingRepository.addAgenda(meetingId, meeting)
        agendaIds.forEachIndexed { index, agendaId ->
            meeting.agenda[index].fileInfos.forEach { fileInfo ->
                meetingRepository.addFiles(agendaId, fileInfo)
            }
        }
    }
}