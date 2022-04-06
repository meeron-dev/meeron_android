package fourtune.merron.data.repository

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.model.Token
import forutune.meeron.domain.repository.LoginRepository
import fourtune.merron.data.model.dto.LoginUserRequest
import fourtune.merron.data.source.remote.LoginApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginApi: LoginApi,
) : LoginRepository {
    override suspend fun login(loginUser: LoginUser): Token = withContext(dispatcher) {
        return@withContext loginApi.login(LoginUserRequest.from(loginUser))
    }


    override suspend fun logout(token: String,refreshToken:String) = withContext(dispatcher) {
        loginApi.logout("Bearer $token","Bearer $refreshToken")
    }
}