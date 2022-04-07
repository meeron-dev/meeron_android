package forutune.meeron.domain.repository

interface AccountRepository {
    suspend fun isFirstVisitor(): Boolean
    suspend fun updateFirstVisitor()

    suspend fun setDynamicLink(workspaceId: Long)
    suspend fun getDynamicLink(): Long
}