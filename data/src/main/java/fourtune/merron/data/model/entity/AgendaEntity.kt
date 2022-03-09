package fourtune.merron.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MeetingEntity::class,
            parentColumns = ["id"],
            childColumns = ["meetingId"],
            onDelete = CASCADE
        )
    ]
)
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val meetingId: Long,
    val agenda: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AgendaEntity::class,
            parentColumns = ["id"],
            childColumns = ["agendaId"],
            onDelete = CASCADE
        )
    ]
)
data class Issue(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val agendaId: Long,
    val issue: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AgendaEntity::class,
            parentColumns = ["id"],
            childColumns = ["agendaId"],
            onDelete = CASCADE
        )
    ]
)
data class File(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val agendaId: Long,
    val path: String
)