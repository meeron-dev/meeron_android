package fourtune.merron.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import fourtune.merron.data.model.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    suspend fun getUser(email: String): UserEntity?
}