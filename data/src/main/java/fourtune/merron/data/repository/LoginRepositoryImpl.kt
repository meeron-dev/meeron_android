package fourtune.merron.data.repository

import forutune.meeron.domain.LoginUser
import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.repository.LoginRepository
import fourtune.merron.data.dto.LoginUserDto
import fourtune.merron.data.source.LoginApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginApi: LoginApi
) : LoginRepository {
    override suspend fun login(loginUser: LoginUser) {
        withContext(dispatcher) {
            loginApi.login(LoginUserDto.from(loginUser))
        }
    }

    override suspend fun logout() = withContext(dispatcher) {
        loginApi.logout()
    }
}