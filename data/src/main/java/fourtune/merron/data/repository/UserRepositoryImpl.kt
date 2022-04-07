package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.Token
import forutune.meeron.domain.model.User
import forutune.meeron.domain.repository.UserRepository
import fourtune.merron.data.model.entity.UserEntity
import fourtune.merron.data.source.local.dao.UserDao
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.UserApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUser(): User {
        return userApi.getMe()
    }

    override suspend fun getUser(email: String): User {
        val userEntity = userDao.getUser(email)
        return if (userEntity != null) {
            User(
                userId = userEntity.id,
                loginEmail = userEntity.email,
                name = userEntity.name,
                profileImageUrl = userEntity.profile
            )
        } else {
            getUser()
        }
    }

    override suspend fun setUserId(userId: Long) {
        dataStore.edit {
            it[DataStoreKeys.User.id] = userId
        }
    }

    override suspend fun getUserId(): Long {
        return dataStore.data.map { it[DataStoreKeys.User.id] }.firstOrNull()
            ?: getUser().userId
    }

    override suspend fun createUserName(userName: String) {
        dataStore.edit {
            userApi.saveUserName(userName)
            it[DataStoreKeys.User.name] = userName
        }
    }

    override suspend fun setUser(user: User, token: Token) {
        userDao.insert(
            UserEntity(
                id = user.userId,
                name = user.name.orEmpty(),
                email = user.loginEmail,
                profile = user.profileImageUrl.orEmpty(),
                token = token
            )
        )
        dataStore.edit {
            it[DataStoreKeys.User.id] = user.userId
        }
    }

    override suspend fun withdrawal() {
        userApi.quit()
    }

}