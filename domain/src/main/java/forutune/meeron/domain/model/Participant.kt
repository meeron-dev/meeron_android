package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Participant(
    val name: String,
    val email: String,
    val profile: String,
    val isHost: Boolean
) : Serializable