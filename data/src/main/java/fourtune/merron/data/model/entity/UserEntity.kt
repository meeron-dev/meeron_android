package fourtune.merron.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import forutune.meeron.domain.model.Token

@Entity(
//    foreignKeys = [
//        ForeignKey(
//            entity = TeamEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["teamId"],
//            onDelete = CASCADE
//        )
//    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
//    val teamId: Long = 0,
    val name: String,
    val email: String,
    val profile: String,
    val token: Token
)
