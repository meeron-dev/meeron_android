package fourtune.merron.data.repository

import forutune.meeron.domain.LoginUser
import forutune.meeron.domain.repository.LoginRepository
import fourtune.merron.data.dto.LoginUserDto
import fourtune.merron.data.source.LoginApi
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi
) : LoginRepository {
    override suspend fun login(loginUser: LoginUser) {
        loginApi.login(LoginUserDto.from(loginUser))
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }
}