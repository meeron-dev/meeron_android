package fourtune.merron.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import forutune.meeron.domain.model.Token
import fourtune.merron.data.model.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("UPDATE UserEntity SET token = :token WHERE id = :userId ")
    suspend fun updateToken(userId: Long, token: Token)

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    suspend fun getUser(email: String): UserEntity?
}