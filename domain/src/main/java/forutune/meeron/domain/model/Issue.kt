package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Issue(
    val issue: String
) : Serializable