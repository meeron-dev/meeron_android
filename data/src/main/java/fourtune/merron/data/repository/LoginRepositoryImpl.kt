package fourtune.merron.data.repository

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.model.Token
import forutune.meeron.domain.repository.LoginRepository
import forutune.meeron.domain.repository.TokenRepository
import fourtune.merron.data.model.dto.LoginUserRequest
import fourtune.merron.data.model.entity.UserEntity
import fourtune.merron.data.source.local.dao.UserDao
import fourtune.merron.data.source.remote.LoginApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginApi: LoginApi,
    private val userDao: UserDao,
    private val tokenRepository: TokenRepository,
) : LoginRepository {
    override suspend fun login(loginUser: LoginUser) {
        withContext(dispatcher) {
            val token = loginApi.login(LoginUserRequest.from(loginUser))
            updateToken(loginUser, token)
        }
    }

    private suspend fun updateToken(
        loginUser: LoginUser,
        token: Token
    ) {
        val user = userDao.getUser(loginUser.email)
        if (user == null) {
            userDao.insert(
                UserEntity(
                    name = loginUser.nickname,
                    email = loginUser.email,
                    profile = loginUser.profileImageUrl.orEmpty(),
                    token = token
                )
            )
        } else {
            userDao.updateToken(user.id, token)
        }
        tokenRepository.saveToken(token)
    }


    override suspend fun logout() = withContext(dispatcher) {
        loginApi.logout()
    }
}