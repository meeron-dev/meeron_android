package fourtune.merron.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import forutune.meeron.domain.model.Meeting

@Entity
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val personality: String = "",
    val owner: String = "",
    val team: String = "",
    val agenda: String = "",
    val participants: String = ""
) {
    fun toMeeting() = Meeting(title, date, time, personality, owner, team, agenda, participants)

    companion object {
        fun from(meeting: Meeting) =
            with(meeting) {
                MeetingEntity(
                    title = title,
                    date = date,
                    time = time,
                    personality = personality,
                    owner = owner,
                    team = team,
                    agenda = agenda,
                    participants = participants
                )
            }
    }
}

