package forutune.meeron.domain.model

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Teams(
    @SerialName("teams") val teams: List<Team>
) : Serializable

@kotlinx.serialization.Serializable
data class Team(
    @SerialName("teamId") val id: Long = -1,
    @SerialName("teamName") val name: String = ""
) : Serializable
