package forutune.meeron.domain.repository

interface AccountRepository {
    suspend fun isFirstVisitor(): Boolean
}