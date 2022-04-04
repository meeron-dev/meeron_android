package forutune.meeron.domain.usecase.meeting.agenda

import forutune.meeron.domain.model.Agenda
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetAgendaUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long, agendaOrder: Int): Agenda {
        return meetingRepository.getAgenda(meetingId, agendaOrder)
    }
}