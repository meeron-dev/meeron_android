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
) {
    fun toMeeting() = Meeting(title, date, time, personality)

    companion object {
        fun from(meeting: Meeting) =
            with(meeting) {
                MeetingEntity(
                    title = title,
                    date = date,
                    time = time,
                    personality = personality,
                )
            }
    }
}

