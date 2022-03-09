package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Team(val name: String = "") : Serializable
