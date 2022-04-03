package forutune.meeron.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamState(
    val teamId: Long,
    val teamName: String,
    val attends: Int,
    val absents: Int,
    val unknowns: Int
) : java.io.Serializable
